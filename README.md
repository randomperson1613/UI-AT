# UI автотесты для practice.expandtesting.com

Проект содержит UI-автотесты для учебного сайта [practice.expandtesting.com](https://practice.expandtesting.com). Тесты написаны на Java 21 с использованием JUnit 5, Selenide и Allure.

## Что покрыто тестами

Всего реализовано 11 UI-тестов, разделённых по бизнес-логике:

- `AuthTest` - вход, негативный сценарий входа, регистрация нового пользователя.
- `FormsTest` - поля ввода, очистка данных, выпадающие списки, загрузка файла.
- `ElementsTest` - чекбоксы, добавление/удаление элементов, динамические контролы, таблицы.

## Требования

- JDK 21.
- Локально установленный Chrome для локального запуска.
- Доступ в интернет, так как тестируется внешний сайт.
- Allure CLI отдельно устанавливать не нужно: генерация и запуск отчёта выполняются через Gradle-плагин.

Gradle устанавливать отдельно не нужно, используется Gradle Wrapper: `./gradlew`.

## Структура проекта

```text
src/test/java/ru/at/ui/BaseTest.java              # базовая конфигурация Selenide и Allure
src/test/java/ru/at/ui/helpers                   # helpers для Allure-вложений и ожиданий
src/test/java/ru/at/ui/pages                     # PageObject-классы
src/test/java/ru/at/ui/tests                     # тестовые классы
src/test/resources/allure.properties             # директория Allure results
src/test/resources/upload/diploma-upload.txt     # файл для проверки upload-сценария
```

## Быстрый запуск

Запуск всех тестов в headless-режиме:

```bash
./gradlew clean test -Dheadless=true
```

Запуск всех тестов с видимым браузером:

```bash
./gradlew clean test
```

Запуск конкретного тестового класса:

```bash
./gradlew test --tests ru.at.ui.tests.AuthTest -Dheadless=true
```

Запуск конкретного теста по имени метода:

```bash
./gradlew test --tests ru.at.ui.tests.AuthTest.shouldLoginWithValidCredentials -Dheadless=true
```

## Параметры запуска

Конфигурация передаётся через `SystemProperties`.

| Параметр | Значение по умолчанию | Описание |
| --- | --- | --- |
| `baseUrl` | `https://practice.expandtesting.com` | Базовый URL тестируемого сайта |
| `browser` | `chrome` | Браузер для запуска |
| `browserVersion` | пусто | Версия браузера, обычно используется для Selenoid |
| `browserSize` | `1920x1080` | Размер окна браузера |
| `remote` | пусто | Remote WebDriver URL, например Selenoid |
| `remoteUser` | пусто | Логин для remote URL; также можно передать через `REMOTE_USER` |
| `remotePassword` | пусто | Пароль для remote URL; также можно передать через `REMOTE_PASSWORD` |
| `headless` | `false` | Запуск без UI браузера |
| `timeout` | `10000` | Таймаут ожиданий Selenide в миллисекундах |
| `pageLoadTimeout` | `30000` | Таймаут загрузки страницы в миллисекундах |
| `pageLoadStrategy` | `normal` | Стратегия загрузки страницы Selenium |
| `reportsFolder` | `build/selenide/reports` | Папка отчётов Selenide |
| `downloadsFolder` | `build/selenide/downloads` | Папка скачанных файлов |
| `chromeNoSandbox` | `false` | Добавляет Chrome-аргумент `--no-sandbox` |
| `blockAds` | `true` | Блокирует рекламные домены через Chrome host resolver rules |
| `enableVnc` | `true` | Включает VNC capability для Selenoid |
| `enableVideo` | `true` | Включает запись видео в Selenoid |
| `videoBaseUrl` | пусто | Базовый URL для ссылки на видео; если не задан, берётся из `remote` без логина и пароля |
| `sessionName` | пусто | Имя сессии в Selenoid UI |

Пример запуска с переопределением параметров:

```bash
./gradlew clean test \
  -Dheadless=true \
  -Dbrowser=chrome \
  -DbrowserSize=1366x768 \
  -Dtimeout=15000
```

Также поддерживается формат `selenide.*`, например:

```bash
./gradlew clean test -Dselenide.browser=chrome -Dselenide.headless=true
```

## Запуск через Selenoid

В `remote` нужно передавать WebDriver endpoint. Для `selenoid.autotests.cloud` это URL с `/wd/hub`:

```bash
https://selenoid.autotests.cloud/wd/hub
```

Локальный запуск без записи логина и пароля в команду:

```bash
export REMOTE_USER=user1
export REMOTE_PASSWORD=1234

./gradlew clean test \
  -Dremote=https://selenoid.autotests.cloud/wd/hub \
  -Dbrowser=chrome \
  -DbrowserSize=1920x1080 \
  -DenableVideo=true
```

При наличии `remote` проект автоматически добавляет capability `selenoid:options` для Selenoid:

- `enableVNC=true`
- `enableVideo=true`

После каждого теста в Allure добавляются скриншот страницы, HTML страницы и логи браузера.
Видео прохождения теста добавляется только при запуске через Selenoid с параметром `remote`, потому что локальный WebDriver не создает видео-артефакт.
Если `remote` не передан, видео не прикладывается, а остальные вложения остаются доступными.

Если нужно передать URL одной строкой, рабочий формат такой:

```bash
./gradlew clean test \
  -Dremote=https://user1:1234@selenoid.autotests.cloud/wd/hub \
  -Dbrowser=chrome \
  -DenableVideo=true
```

Для Jenkins лучше использовать `REMOTE_USER` и `REMOTE_PASSWORD` из Jenkins Credentials, а не хранить логин и пароль в репозитории.
По умолчанию логин и пароль не попадают в HTML-вложение Allure с видео. Если ваш Selenoid закрывает `/video/...` basic auth, настройте отдельный публичный/proxy URL через `-DvideoBaseUrl=...` или задайте credentialed `videoBaseUrl` осознанно.

## Отчёты

После запуска Gradle HTML-отчёт доступен по пути:

```text
build/reports/tests/test/index.html
```

Allure results сохраняются в:

```text
build/allure-results
```

Сгенерировать Allure HTML-отчёт из уже существующих результатов:

```bash
./gradlew allureReport
```

Готовый Allure HTML-отчёт будет лежать здесь:

```text
build/reports/allure-report/allureReport
```

Запустить тесты и сразу собрать Allure HTML-отчёт:

```bash
./gradlew clean allureReport --depends-on-tests -Dheadless=true
```

Открыть Allure-отчёт через локальный сервер Gradle-плагина:

```bash
./gradlew allureServe
```

## Jenkins и Selenoid

`Jenkinsfile` запускает тесты через Selenoid по умолчанию. Для этого в Jenkins нужно создать credential:

- Kind: `Username with password`.
- ID: `selenoid-autotests-cloud`.
- Username: `user1`.
- Password: `1234`.

Параметры job:

- `REMOTE_DRIVER=true` - запускать тесты через Selenoid.
- `SELENOID_URL=https://selenoid.autotests.cloud/wd/hub` - URL без логина и пароля.
- `BROWSER=chrome`.
- `BROWSER_VERSION` - можно оставить пустым, тогда используется версия по умолчанию на Selenoid.
- `BROWSER_SIZE=1920x1080`.
- `HEADLESS=false` - для видео в Selenoid лучше оставлять `false`.
- `ENABLE_VIDEO=true`.

Для локального запуска на Jenkins-agent без Selenoid установите `REMOTE_DRIVER=false` и `HEADLESS=true`; тогда агенту нужен установленный Chrome.

## Telegram-уведомления в Jenkins

После выполнения тестов Jenkins генерирует Allure HTML-отчёт и запускает
`notifications/allure-notifications-4.11.0.jar`. Конфиг лежит в
`notifications/config.json`, но токен бота и chat id в него не записываются.

В Jenkins создайте два `Secret text` credentials:

- `telegram-bot-token-rodneystone` - токен Telegram-бота.
- `telegram-chat-id-rodneystone` - id чата, куда бот отправляет отчёт.

После этого запустите job. Уведомление будет отправлено из блока `post` в
`Jenkinsfile`, если найден файл:

```text
build/reports/allure-report/allureReport/widgets/summary.json
```

## Allure-разметка

В проекте используются:

- `@Epic`, `@Feature`, `@Story` для группировки тестов.
- `@DisplayName`, `@Owner`, `@Severity` для описания тест-кейсов.
- `@Step` для отображения действий в Allure-отчёте.
- Вложения после каждого теста: скриншот, HTML страницы, логи браузера, видео Selenoid при remote-запуске через `-Dremote=...`.

Названия тест-кейсов, группировок и шагов указаны на русском языке.


## Полная проверка проекта

Для финальной проверки используйте:

```bash
./gradlew clean test -Dheadless=true
```

Ожидаемый результат:

```text
BUILD SUCCESSFUL
```
