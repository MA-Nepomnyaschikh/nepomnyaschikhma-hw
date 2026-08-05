#!/bin/bash

set -e
export MSYS_NO_PATHCONV=1

TEST_PROFILE=${1:-all}

IMAGE_NAME="nbank-tests"
TAG="latest"
TIMESTAMP=$(date +"%Y%m%d_%H%M")
TEST_OUTPUT_DIR=./test-output/$TIMESTAMP

mkdir -p "$TEST_OUTPUT_DIR/logs"
mkdir -p "$TEST_OUTPUT_DIR/results"
mkdir -p "$TEST_OUTPUT_DIR/report"

echo ""
echo "======================================"
echo "Запуск тестового контейнера..."
echo "Образ: $IMAGE_NAME:$TAG"

set +e

docker run --rm \
  --network nbank-network \
  -v "$TEST_OUTPUT_DIR/logs":/app/logs \
  -v "$TEST_OUTPUT_DIR/results":/app/target/surefire-reports \
  -v "$TEST_OUTPUT_DIR/report":/app/target/site \
  -e TEST_PROFILE="$TEST_PROFILE" \
  -e APIBASEURL=http://backend:4111 \
  -e UIBASEURL=http://frontend  \
  -e UIREMOTE=http://selenoid:4444/wd/hub \
  "$IMAGE_NAME:$TAG"

TEST_RESULT=$?

set -e

if [ -f "$TEST_OUTPUT_DIR/logs/run.log" ]; then
    echo ""
    echo "Логи:"
    echo "$TEST_OUTPUT_DIR/logs/run.log"
    echo ""
    echo "Результаты тестов:"
    echo "$TEST_OUTPUT_DIR/results"
    echo ""
    echo "Отчет:"
    echo "$TEST_OUTPUT_DIR/report"
fi

exit $TEST_RESULT