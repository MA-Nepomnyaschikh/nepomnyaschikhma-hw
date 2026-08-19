#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BROWSERS_CONFIG="$SCRIPT_DIR/nbank-chart/config/browsers.json"
NBANK_CHART_DIRECTORY="$SCRIPT_DIR/nbank-chart"
LOGGING_CHART_DIRECTORY="$SCRIPT_DIR/logging-chart"
MONITORING_VALUES="$SCRIPT_DIR/monitoring-values.yaml"

echo ""
echo "======================================"
echo "Остановка  NBank..."

taskkill //F //IM kubectl.exe || true

kubectl delete configmap selenoid-config || true

minikube stop || true
echo ""
echo "NBank успешно остановлен"

echo ""
echo "======================================"
echo "Запуск NBank..."

minikube start --driver=docker
echo ""
kubectl create configmap selenoid-config --from-file=browsers.json="$BROWSERS_CONFIG"
echo ""
helm upgrade --install nbank "$NBANK_CHART_DIRECTORY"
echo ""
kubectl wait --for=condition=available deployment/backend -n default --timeout=120s
kubectl wait --for=condition=ready pod -l app=backend -n default --timeout=120s

kubectl wait --for=condition=available deployment/frontend -n default --timeout=120s
kubectl wait --for=condition=ready pod -l app=frontend -n default --timeout=120s

kubectl wait --for=condition=available deployment/selenoid -n default --timeout=120s
kubectl wait --for=condition=ready pod -l app=selenoid -n default --timeout=120s

kubectl wait --for=condition=available deployment/selenoid-ui -n default --timeout=120s
kubectl wait --for=condition=ready pod -l app=selenoid-ui -n default --timeout=120s

kubectl rollout status deployment/selenoid -n default --timeout=120s
kubectl rollout status deployment/selenoid-ui -n default --timeout=120s

echo ""
echo "Проброс портов для NBank..."
kubectl port-forward svc/backend 4111:4111 &
kubectl port-forward svc/frontend 3000:80 &
kubectl port-forward svc/selenoid 4444:4444 &
kubectl port-forward svc/selenoid-ui 8080:8080 &

echo ""
echo "Запуск Prometheus и Grafana..."
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts || true
helm repo update prometheus-community

helm upgrade --install monitoring prometheus-community/kube-prometheus-stack -n monitoring --create-namespace -f "$MONITORING_VALUES"
echo ""
kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=prometheus -n monitoring --timeout=120s
kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=grafana -n monitoring --timeout=120s

kubectl create secret generic backend-basic-auth --from-literal=username=admin --from-literal=password=admin -n monitoring --dry-run=client -o yaml | kubectl apply -f -
kubectl apply -f "$SCRIPT_DIR/spring-monitoring.yaml"

echo ""
echo "Запуск Elasticsearch и Kibana..."
helm upgrade --install logging "$LOGGING_CHART_DIRECTORY" -n monitoring --create-namespace
echo ""
kubectl wait --for=condition=available deployment/elasticsearch -n monitoring --timeout=300s
kubectl wait --for=condition=ready pod -l app=elasticsearch -n monitoring --timeout=300s

kubectl wait --for=condition=available deployment/kibana -n monitoring --timeout=300s
kubectl wait --for=condition=ready pod -l app=kibana -n monitoring --timeout=300s

kubectl rollout status daemonset/filebeat -n monitoring --timeout=300s
kubectl wait --for=condition=ready pod -l app=filebeat -n monitoring --timeout=300s

echo ""
echo "Проброс портов для мониторинга и логирования..."
kubectl port-forward svc/monitoring-kube-prometheus-prometheus -n monitoring 3001:9090 &
kubectl port-forward svc/monitoring-grafana -n monitoring 3002:80 &
kubectl port-forward svc/elasticsearch -n monitoring 9200:9200 &
kubectl port-forward svc/kibana -n monitoring 5601:5601 &

echo ""
echo "Kubernetes успешно запущен"