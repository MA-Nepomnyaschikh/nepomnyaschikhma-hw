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
echo "Запуск Docker  Compose..."
docker compose -f "$COMPOSE_FILE" up -d

echo ""
echo "Docker Compose успешно запущен"