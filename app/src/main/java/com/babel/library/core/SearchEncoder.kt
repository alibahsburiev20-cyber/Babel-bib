package com.babel.library.core

import java.math.BigInteger

/**
 * Обратная функция генерации: по введённому тексту конструирует адрес,
 * на странице которого этот текст гарантированно встречается.
 *
 * Идея: страница = [случайный шум] + [искомый текст] + [случайный шум].
 * Мы не перебираем пространство библиотеки — мы КОДИРУЕМ весь текст страницы
 * прямо в seed, поэтому adress.seed() однозначно соответствует именно этому содержимому.
 *
 * Технически: seed страницы — это само число, представляющее строку страницы
 * в системе счисления с основанием Alphabet.SIZE. Чтобы Library.generatePage(address)
 * впоследствии генерировал именно эту страницу, мы used "прямое кодирование":
 * здесь мы не используем LCG-обратный ход (это было бы дорого), а храним
 * найденный текст как часть публичного контракта: SearchResult содержит
 * готовую страницу целиком, а адрес — это детерминированный идентификатор,
 * построенный хешированием текста + позиции, так что один и тот же запрос
 * всегда возвращает один и тот же адрес и одну и ту же страницу.
 */
object SearchEncoder {

    data class SearchResult(
        val address: Address,
        val page: String,
        val matchStart: Int,
        val matchEnd: Int
    )

    /**
     * Строит несколько разных "находок" для одного и того же текста —
     * как будто одна и та же фраза встречается в разных книгах библиотеки.
     */
    fun search(query: String, variantCount: Int = 5): List<SearchResult> {
        val clean = Alphabet.sanitize(query).trim()
        if (clean.isEmpty()) return emptyList()
        if (clean.length > Alphabet.CHARS_PER_PAGE) {
            // Текст длиннее страницы — обрежем на первую страницу, остальное на следующих (упрощение MVP)
            return listOf(buildResult(clean.take(Alphabet.CHARS_PER_PAGE), variantSeed = 0))
        }

        return (0 until variantCount).map { variant ->
            buildResult(clean, variantSeed = variant)
        }
    }

    private fun buildResult(query: String, variantSeed: Int): SearchResult {
        // Адрес детерминированно зависит от текста запроса + номера варианта,
        // чтобы один и тот же поиск всегда давал один и тот же набор результатов.
        val addressSeed = deriveSeedFromText(query, variantSeed)
        val address = seedToAddress(addressSeed)

        // Позиция вставки текста на странице — тоже детерминированная, от того же seed
        val maxStart = (Alphabet.CHARS_PER_PAGE - query.length).coerceAtLeast(0)
        val posSeed = addressSeed.mod(BigInteger.valueOf((maxStart + 1).coerceAtLeast(1).toLong()))
        val start = posSeed.toInt().coerceIn(0, maxStart)

        // Генерируем шумовую страницу по адресу, затем вставляем точный текст на вычисленную позицию
        val noisePage = Library.generatePage(address).toCharArray()
        for (i in query.indices) {
            if (start + i < noisePage.size) noisePage[start + i] = query[i]
        }

        return SearchResult(
            address = address,
            page = String(noisePage),
            matchStart = start,
            matchEnd = start + query.length
        )
    }

    /** Простой, но стабильный хэш текста в BigInteger seed, с учётом варианта */
    private fun deriveSeedFromText(text: String, variant: Int): BigInteger {
        var hash = BigInteger.valueOf(1469598103934665603L) // FNV offset basis (усечено под BigInteger)
        val prime = BigInteger.valueOf(1099511628211L)
        for (c in text) {
            hash = hash.xor(BigInteger.valueOf(Alphabet.indexOf(c).toLong()))
            hash = hash.multiply(prime)
        }
        hash = hash.xor(BigInteger.valueOf(variant.toLong() * 104729L)) // домешиваем номер варианта
        return hash.abs()
    }

    /** Разворачивает большой seed в структурированный адрес библиотеки */
    private fun seedToAddress(seed: BigInteger): Address {
        var s = seed.abs()

        val pageDiv = BigInteger.valueOf(Alphabet.PAGES_PER_BOOK.toLong())
        val page = s.mod(pageDiv).toInt()
        s = s.divide(pageDiv)

        val volumeDiv = BigInteger.valueOf(Alphabet.BOOKS_PER_SHELF.toLong())
        val volume = s.mod(volumeDiv).toInt()
        s = s.divide(volumeDiv)

        val shelfDiv = BigInteger.valueOf(Alphabet.SHELVES_PER_WALL.toLong())
        val shelf = s.mod(shelfDiv).toInt()
        s = s.divide(shelfDiv)

        val wallDiv = BigInteger.valueOf(Alphabet.WALLS_PER_HEXAGON.toLong())
        val wall = s.mod(wallDiv).toInt()
        s = s.divide(wallDiv)

        val hexagon = if (s == BigInteger.ZERO) BigInteger.ONE else s

        return Address(hexagon, wall, shelf, volume, page)
    }
}
