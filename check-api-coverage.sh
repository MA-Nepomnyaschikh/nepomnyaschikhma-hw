#!/bin/bash

set -e

RESULT_FILE="swagger-coverage-results.json"

if [ ! -f "$RESULT_FILE" ]; then
  echo "❌ Ошибка: Файл с результатами покрытия API не найден: $RESULT_FILE"
  exit 1
fi

covered=$(jq '.conditionCounter.covered' "$RESULT_FILE")
all=$(jq '.conditionCounter.all' "$RESULT_FILE")

if [ "$all" -eq 0 ]; then
  echo "❌ Ошибка: Количество проверяемых условий равно 0"
  exit 1
fi

coverage=$(( covered * 100 / all ))

echo "Покрыто API: $covered из $all"
echo "Процент покрытия API: $coverage%"

if [ "$coverage" -lt 50 ]; then
  echo "❌ Проверка качества не пройдена: покрытие API меньше 50%"
  exit 1
fi

echo "✅ Проверка качества пройдена: Покрытие API больше 50%"