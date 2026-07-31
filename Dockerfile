# Базовый докер образ
FROM maven:3.9.9-eclipse-temurin-21

# Дефолтные значения аргументов
ARG TEST_PROFILE=api
ARG APIBASEURL=http://localhost:4111
ARG UIBASEURL=http://localhost:3000

# Переменные окружения для контейнера
ENV TEST_PROFILE=${TEST_PROFILE}
ENV APIBASEURL=${APIBASEURL}
ENV UIBASEURL=${UIBASEURL}

# Установка рабочей директории
WORKDIR /app

# Копирование помника
COPY pom.xml .

# Загрузка зависимостей + кэширование
RUN mvn dependency:go-offline

# Копирование всего проекта
COPY . .

USER root

CMD ["/bin/bash", "-c", "\
    echo ''; \
    echo 'Окружение успешно запущено'; \
    mkdir -p /app/logs; \
    echo ''; \
    echo '======================================'; \
    echo 'Запуск тестов...'; \
    echo 'Профиль: '${TEST_PROFILE}; \
    \
    mvn test -P ${TEST_PROFILE} > /app/logs/run.log 2>&1; \
    TEST_RESULT=$?; \
    \
    echo ''; \
    if [ $TEST_RESULT -eq 0 ]; then \
        echo 'Выполнение тестов завершено успешно'; \
    else \
        echo 'Выполнение тестов завершено с ошибкой'; \
    fi; \
    \
    mvn -DskipTests=true surefire-report:report >> /app/logs/run.log 2>&1; \
    \
    exit $TEST_RESULT"]