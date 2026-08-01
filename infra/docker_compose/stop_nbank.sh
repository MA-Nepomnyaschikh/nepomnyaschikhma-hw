#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="$SCRIPT_DIR/docker-compose.yml"

echo ""
echo "======================================"
echo "Остановка тестового окружения..."
docker compose -f $COMPOSE_FILE down

echo ""
echo "Окружение успешно остановлено"