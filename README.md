Для запуска необходима Java 21.

Запуск приложения:

```bash
./gradlew bootRun
```

Точка входа в приложение является REST-сервис.

```bash
curl -X POST http://localhost:8080/api/v1/okved/find?phone=89000011112
```

# Сcылки на лицензии:
- mockito (MIT): https://github.com/mockito/mockito/blob/main/LICENSE
- junit (EPL): https://github.com/junit-team/junit-framework/blob/main/LICENSE.md
- spring (Apache): https://github.com/spring-projects/spring-framework/blob/main/LICENSE.txt


## GitJira:
1. testcase-1: Обновить логу поиска ОКВЕДа
