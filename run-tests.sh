#!/bin/bash

set -e
export MSYS_NO_PATHCONV=1

TEST_PROFILE=${1:-api}

IMAGE_NAME="nbank-tests"
TAG="latest"
TIMESTAMP=$(date +"%Y%m%d_%H%M")
TEST_OUTPUT_DIR=./test-output/$TIMESTAMP

mkdir -p "$TEST_OUTPUT_DIR/logs"
mkdir -p "$TEST_OUTPUT_DIR/results"
mkdir -p "$TEST_OUTPUT_DIR/report"

echo ""
echo "======================================"
echo "Сборка Docker образа $IMAGE_NAME:$TAG..."
echo ""

docker build -t "$IMAGE_NAME:$TAG" -q .

echo ""
echo "Образ успешно собран"

echo ""
echo "======================================"
echo "Запуск окружения для тестов..."
echo "Образ: $IMAGE_NAME:$TAG"

docker run --rm \
  -v "$TEST_OUTPUT_DIR/logs":/app/logs \
  -v "$TEST_OUTPUT_DIR/results":/app/target/surefire-reports \
  -v "$TEST_OUTPUT_DIR/report":/app/target/site \
  -e TEST_PROFILE="$TEST_PROFILE" \
  -e APIBASEURL=http://host.docker.internal:4111 \
  -e UIBASEURL=http://192.168.3.12:3000 \
  "$IMAGE_NAME:$TAG"

echo ""
echo "Логи:"
echo "$TEST_OUTPUT_DIR/logs/run.log"
echo ""
echo "Результаты тестов:"
echo "$TEST_OUTPUT_DIR/results"
echo ""
echo "Отчет:"
echo "$TEST_OUTPUT_DIR/report"