#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BROWSERS_CONFIG="$SCRIPT_DIR/nbank-chart/config/browsers.json"
CHART_DIRECTORY="$SCRIPT_DIR/nbank-chart"

echo ""
echo "======================================"
echo "Остановка  Kubernetes..."
echo ""
echo "Закрытие портов..."
taskkill //F //IM kubectl.exe || true
echo ""
echo "Удаление NBank..."
helm uninstall nbank || true
echo ""
echo "Удаление Selenoid ConfigMap..."
kubectl delete configmap selenoid-config || true
echo ""
echo "Остановка Minikube..."
minikube stop || true
echo ""
echo "Kubernetes успешно остановлен"

echo ""
echo "======================================"
echo "Запуск Kubernetes..."
echo ""
echo "Запуск Minikube..."
minikube start --driver=docker
echo ""
echo "Создание Selenoid ConfigMap..."
kubectl create configmap selenoid-config --from-file=browsers.json="$BROWSERS_CONFIG"
echo ""
echo "Установка NBank..."
helm install nbank "$CHART_DIRECTORY"
echo ""
echo "Запуск Pod'ов..."
kubectl wait --for=condition=Available deployment --all --timeout=120s
echo ""
echo "Проброс портов..."
kubectl port-forward svc/backend 4111:4111 &
kubectl port-forward svc/frontend 3000:80 &
kubectl port-forward svc/selenoid 4444:4444 &
kubectl port-forward svc/selenoid-ui 8080:8080 &
echo ""
echo "Kubernetes успешно запущен"