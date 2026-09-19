package com.babel.library.core

/**
 * Алфавит Вавилонской библиотеки.
 * Русские буквы + цифры + базовая пунктуация + пробел.
 * Индекс символа в этой строке = его "цифра" в системе счисления с основанием ALPHABET.length
 */
object Alphabet {

    // Русский алфавит (33 буквы, включая ё)
    private const val RUSSIAN_LETTERS = "абвгдежзийклмнопрстуфхцчшщъыьэюя"

    // Цифры для дат/времени/чисел
    private const val DIGITS = "0123456789"

    // Знаки препинания и пробел
    private const val PUNCTUATION = " ,.:-"

    /** Полный алфавит библиотеки. Порядок фиксирован — менять нельзя, иначе все старые адреса "поплывут". */
    val CHARACTERS: String = RUSSIAN_LETTERS + DIGITS + PUNCTUATION

    val SIZE: Int = CHARACTERS.length

    /** Сколько символов помещается на одну страницу */
    const val CHARS_PER_PAGE: Int = 3200 // 40 строк по 80 символов, как в классической концепции

    /** Сколько страниц в одной книге */
    const val PAGES_PER_BOOK: Int = 410

    /** Иерархия расположения: сколько книг на полке, полок на стене, стен в шестиграннике */
    const val BOOKS_PER_SHELF: Int = 32
    const val SHELVES_PER_WALL: Int = 5
    const val WALLS_PER_HEXAGON: Int = 6

    fun indexOf(char: Char): Int {
        val idx = CHARACTERS.indexOf(char)
        return if (idx >= 0) idx else CHARACTERS.indexOf(' ') // неизвестный символ -> пробел
    }

    fun charAt(index: Int): Char = CHARACTERS[index % SIZE]

    /** Проверяет, что строка состоит только из символов алфавита библиотеки */
    fun isValid(text: String): Boolean = text.lowercase().all { CHARACTERS.contains(it) }

    /** Приводит произвольный ввод пользователя к валидному для библиотеки виду (нижний регистр, замена неизвестных символов на пробел) */
    fun sanitize(text: String): String {
        val lower = text.lowercase().replace('ё', 'е')
        return buildString {
            for (c in lower) {
                append(if (CHARACTERS.contains(c)) c else ' ')
            }
        }
    }
}
