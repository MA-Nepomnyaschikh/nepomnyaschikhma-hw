#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="$SCRIPT_DIR/docker-compose.yml"

echo ""
echo "======================================"
echo "Запуск окружения для тестов..."
docker compose -f "$COMPOSE_FILE" up -d

echo ""
echo "Окружение успешно запущено"