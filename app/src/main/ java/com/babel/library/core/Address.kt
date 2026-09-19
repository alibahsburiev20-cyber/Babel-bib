package com.babel.library.core

import java.math.BigInteger

/**
 * Координаты книги/страницы в библиотеке.
 * hexagon может быть сколь угодно большим числом (BigInteger) — пространство библиотеки огромно.
 */
data class Address(
    val hexagon: BigInteger,
    val wall: Int,       // 0..5
    val shelf: Int,      // 0..4
    val volume: Int,     // 0..31
    val page: Int         // 0..409
) {

    /** Уникальный seed для PRNG — комбинация всех координат в одно большое число */
    fun seed(): BigInteger {
        var s = hexagon
        s = s.multiply(BigInteger.valueOf(Alphabet.WALLS_PER_HEXAGON.toLong())).add(BigInteger.valueOf(wall.toLong()))
        s = s.multiply(BigInteger.valueOf(Alphabet.SHELVES_PER_WALL.toLong())).add(BigInteger.valueOf(shelf.toLong()))
        s = s.multiply(BigInteger.valueOf(Alphabet.BOOKS_PER_SHELF.toLong())).add(BigInteger.valueOf(volume.toLong()))
        s = s.multiply(BigInteger.valueOf(Alphabet.PAGES_PER_BOOK.toLong())).add(BigInteger.valueOf(page.toLong()))
        return s
    }

    /** Seed для обложки/заголовка книги — не зависит от конкретной страницы */
    fun bookSeed(): BigInteger {
        var s = hexagon
        s = s.multiply(BigInteger.valueOf(Alphabet.WALLS_PER_HEXAGON.toLong())).add(BigInteger.valueOf(wall.toLong()))
        s = s.multiply(BigInteger.valueOf(Alphabet.SHELVES_PER_WALL.toLong())).add(BigInteger.valueOf(shelf.toLong()))
        s = s.multiply(BigInteger.valueOf(Alphabet.BOOKS_PER_SHELF.toLong())).add(BigInteger.valueOf(volume.toLong()))
        return s
    }

    /** Человекочитаемый компактный вид адреса, например "7F-3A-12-06-0042" */
    fun toDisplayString(): String {
        val hexPart = hexagon.toString(16).uppercase().let { if (it.length < 2) "0$it" else it }
        val wallPart = ('A' + wall)
        val shelfPart = shelf.toString().padStart(2, '0')
        val volumePart = volume.toString().padStart(2, '0')
        val pagePart = page.toString().padStart(4, '0')
        return "$hexPart$wallPart-$shelfPart-$volumePart-$pagePart"
    }

    /** Адрес без номера страницы (для отображения "книги") */
    fun toBookDisplayString(): String {
        val hexPart = hexagon.toString(16).uppercase().let { if (it.length < 2) "0$it" else it }
        val wallPart = ('A' + wall)
        val shelfPart = shelf.toString().padStart(2, '0')
        val volumePart = volume.toString().padStart(2, '0')
        return "$hexPart$wallPart-$shelfPart-$volumePart"
    }

    companion object {

        /** Парсит строку вида "7F-3A-12-06-0042" обратно в адрес. Возвращает null при ошибке формата. */
        fun parse(display: String): Address? {
            return try {
                val clean = display.trim().uppercase()
                val parts = clean.split("-")
                if (parts.size != 4) return null

                val hexWall = parts[0]
                val wallChar = hexWall.last()
                val hexStr = hexWall.dropLast(1)

                val hexagon = BigInteger(hexStr, 16)
                val wall = wallChar - 'A'
                val shelf = parts[1].toInt()
                val volume = parts[2].toInt()
                val page = parts[3].toInt()

                if (wall !in 0 until Alphabet.WALLS_PER_HEXAGON) return null
                if (shelf !in 0 until Alphabet.SHELVES_PER_WALL) return null
                if (volume !in 0 until Alphabet.BOOKS_PER_SHELF) return null
                if (page !in 0 until Alphabet.PAGES_PER_BOOK) return null

                Address(hexagon, wall, shelf, volume, page)
            } catch (e: Exception) {
                null
            }
        }

        /** Генерирует случайный адрес */
        fun random(): Address {
            val hexagon = BigInteger(160, java.security.SecureRandom()) // огромное случайное число
            return Address(
                hexagon = hexagon,
                wall = (0 until Alphabet.WALLS_PER_HEXAGON).random(),
                shelf = (0 until Alphabet.SHELVES_PER_WALL).random(),
                volume = (0 until Alphabet.BOOKS_PER_SHELF).random(),
                page = (0 until Alphabet.PAGES_PER_BOOK).random()
            )
        }
    }
}
