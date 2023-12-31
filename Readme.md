# Servlet with db

## Описание
Этот проект - Java-приложение, использующее Gradle, совместимое с Java 17. Оно реализует паттерны LRU и LFU для кэширования.
Использует сервлеты для работы с сервисом.

## Особенности
- **Кэширование**: Поддержка алгоритмов LRU и LFU.
- **Слои приложения**: Слой `service` вызывает слой `daoproxy`, который синхронизирует работу кэша и dao.
- **CRUD операции**: Поддержка операций создания, чтения, обновления и удаления.
- **Синхронизация с кэшем**: Автоматическое кэширование результатов DAO.
- **Конфигурация**: Настройки через `application.yml`.
- **Тестирование**: Unit тесты для проверки работоспособности.
- **Документация**: Подробный `README.md` и Javadoc.
- **XML Сериализация**: XmlSerializer класс для сериализации объектов в xml.
- **PDF конвертация**: PdfSerializer класс для сериализации объектов в pdf и пдф сохраняется в корневую папку pdf

## Установка и запуск
    Запустите Docker, используя команду docker-compose up. Это создаст базу данных и tomcat сервер.
    Далее используйте запросы для получения информации: 
    - (GET) http://localhost:8080/myapp/products?pageSize=5&pageNumber=1 получение списка продуктов.
    - (GET) http://localhost:8080/myapp/products/{uuid} получение продукта по uuid
    - (DELETE) http://localhost:8080/myapp/products/dcce95ba-46ea-4739-887b-1de051755ac7 удаление продукта
    - (POST) http://localhost:8080/myapp/products создание продукта
    - (PUT) http://localhost:8080/myapp/products обновление продукта
    - (GET) http://localhost:8080/myapp/pdf/{uuid} создание пдф продукта
