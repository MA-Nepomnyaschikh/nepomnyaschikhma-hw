#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="$SCRIPT_DIR/docker-compose.yml"
JSON_FILE="$SCRIPT_DIR/config/browsers.json"

echo ""
echo "======================================"
echo "Остановка Docker  Compose..."
docker compose -f "$COMPOSE_FILE" down

echo ""
echo "Docker Compose успешно остановлен"

echo ""
echo "======================================"
echo "Обновление Docker образов..."

## Проверяем, что jq установлен
#if ! command -v jq &> /dev/null; then
#    echo "❌ jq is not installed. Please install jq and try again."
#    exit 1
#fi
#
## Извлекаем все значения .image через jq
#images=$(jq -r '.. | objects | select(.image) | .image' "$JSON_FILE")
#
## Пробегаем по каждому образу и выполняем docker pull
#for image in $images; do
#    echo "Pulling $image..."
#    docker pull "$image"
#done

docker compose -f "$COMPOSE_FILE" pull

echo ""
echo "Необходимые образы успешно загружены"

echo ""
echo "======================================"
echo "Запуск Docker  Compose..."
docker compose -f "$COMPOSE_FILE" up -d

echo ""
echo "Запущенные контейнеры:"
docker compose -f "$COMPOSE_FILE" ps

echo ""
echo "Используемые образы:"
docker compose -f "$COMPOSE_FILE" images

echo ""
echo "Docker Compose успешно запущен"