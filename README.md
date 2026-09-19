# Babel Library / Библиотека Вавилона

Android-приложение на Kotlin + Jetpack Compose, реализующее концепцию
Вавилонской библиотеки Борхеса с русским алфавитом.

## Как собрать APK через GitHub Actions

1. Создай новый репозиторий на github.com (например `babel-library`)
2. Загрузи туда все файлы из этой папки, сохранив структуру:
   - через веб-интерфейс GitHub: "Add file" → "Upload files" → перетащи всю папку
   - или через git: `git init`, `git add .`, `git commit -m "init"`,
     `git remote add origin <ссылка>`, `git push -u origin main`
3. Убедись, что ветка называется `main` (или поправь `branches: [ "main" ]`
   в `.github/workflows/build-apk.yml` под свою ветку)
4. Зайди во вкладку **Actions** репозитория — сборка запустится автоматически
   после пуша. Можно также запустить вручную кнопкой "Run workflow"
5. Дождись зелёной галочки, зайди в завершённый workflow run →
   раздел **Artifacts** внизу → скачай `babel-library-debug-apk`
6. Внутри архива — файл `app-debug.apk`, устанавливай прямо на телефон
   (может понадобиться разрешить установку из неизвестных источников)

## Структура проекта

- `app/src/main/java/com/babel/library/core/` — логика генерации и поиска
  (Alphabet, Address, Library, SearchEncoder)
- `app/src/main/java/com/babel/library/ui/screens/` — все экраны приложения
- `app/src/main/java/com/babel/library/ui/theme/` — цвета и типографика
- `app/src/main/java/com/babel/library/ui/navigation/` — навигация между экранами
- `app/src/main/java/com/babel/library/data/` — локальное хранилище закладок/настроек

## Известные ограничения MVP

- Поиск работает для фраз длиной до одной страницы (3200 символов)
- Изображения и ДНК-режим не реализованы — только текст
- Иконка приложения — простая заглушка (векторная, золото на тёмном фоне)
