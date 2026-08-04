#!/bin/bash

set -e

if [ -z "$DOCKERHUB_TOKEN" ]; then
    echo "Ошибка: DOCKERHUB_TOKEN не задан"
    exit 1
fi

IMAGE_NAME="nbank-tests"
DOCKERHUB_USERNAME="nepomnyaschikhma"
TAG="latest"

echo ""
echo "======================================"
echo "Авторизация в Docker Hub..."
echo ""

echo "$DOCKERHUB_TOKEN" | docker login \
  -u "$DOCKERHUB_USERNAME" \
  --password-stdin

echo ""
echo "======================================"
echo "Тегирование образа..."
echo ""
echo "$IMAGE_NAME:$TAG -> $DOCKERHUB_USERNAME/$IMAGE_NAME:$TAG"

docker tag "$IMAGE_NAME:$TAG" "$DOCKERHUB_USERNAME/$IMAGE_NAME:$TAG"

echo ""
echo "======================================"
echo "Загрузка образа в Docker Hub..."
echo ""

docker push "$DOCKERHUB_USERNAME/$IMAGE_NAME:$TAG"

echo ""
echo "======================================"
echo "Образ успешно опубликован"
echo ""
echo "Для скачивания выполните команду:"
echo "docker pull $DOCKERHUB_USERNAME/$IMAGE_NAME:$TAG"