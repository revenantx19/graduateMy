Реализация backend части приложения по продаже новых и б/у вещей.

Приложение подключается к Frontend используя Docker.

В командной строке необходимо выполнить команду:

docker run -p 3000:3000 --rm ghcr.io/dmitry-bizin/front-react-avito:v1.21

Особенности
Реализовано через Spring Boot, Spring Security и Spring Data JPA.
Микросервисная архитектура с разделением на слои: контроллеры, сервисы, репозитории.
Поддержка работы с изображениями и файлами через эндпоинты API.
Модульная структура, легко расширяемая для добавления новых функций.

