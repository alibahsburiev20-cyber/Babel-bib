package com.babel.library.core

import java.math.BigInteger

/**
 * Ядро генерации содержимого библиотеки.
 * Детерминированный PRNG: один и тот же адрес ВСЕГДА даёт одну и ту же страницу.
 *
 * Используем LCG (linear congruential generator) с 64-битным состоянием,
 * инициализированным из seed адреса — быстро и полностью воспроизводимо.
 */
object Library {

    // Константы LCG (как в glibc) — проверенные параметры с хорошим периодом
    private val LCG_MULTIPLIER = BigInteger.valueOf(6364136223846793005L)
    private val LCG_INCREMENT = BigInteger.valueOf(1442695040888963407L)
    private val LCG_MODULUS = BigInteger.ONE.shiftLeft(64) // 2^64

    /**
     * Генерирует детерминированную последовательность "случайных" индексов алфавита
     * заданной длины, начиная от seed.
     */
    private fun generateIndices(seed: BigInteger, count: Int): IntArray {
        var state = seed.mod(LCG_MODULUS)
        // "Прогрев" генератора, чтобы избежать корреляции для близких seed-ов
        repeat(4) {
            state = state.multiply(LCG_MULTIPLIER).add(LCG_INCREMENT).mod(LCG_MODULUS)
        }

        val result = IntArray(count)
        for (i in 0 until count) {
            state = state.multiply(LCG_MULTIPLIER).add(LCG_INCREMENT).mod(LCG_MODULUS)
            // Берём старшие биты состояния — они статистически более качественные для LCG
            val topBits = state.shiftRight(33).toLong() and 0x7FFFFFFFL
            result[i] = (topBits % Alphabet.SIZE).toInt()
        }
        return result
    }

    /** Генерирует полный текст страницы по адресу */
    fun generatePage(address: Address): String {
        val indices = generateIndices(address.seed(), Alphabet.CHARS_PER_PAGE)
        return buildString {
            for (idx in indices) append(Alphabet.charAt(idx))
        }
    }

    /** Генерирует "заголовок" книги (случайная последовательность фиксированной длины) */
    fun generateTitle(address: Address, length: Int = 24): String {
        val indices = generateIndices(address.bookSeed().add(BigInteger.valueOf(777)), length)
        return buildString {
            for (idx in indices) append(Alphabet.charAt(idx))
        }.trim().ifEmpty { "безымянный том" }
    }

    /** Генерирует "автора" книги */
    fun generateAuthor(address: Address, length: Int = 16): String {
        val indices = generateIndices(address.bookSeed().add(BigInteger.valueOf(999)), length)
        return buildString {
            for (idx in indices) append(Alphabet.charAt(idx))
        }.trim().ifEmpty { "неизвестен" }
    }

    /**
     * Генерирует страницу, гарантированно содержащую хотя бы одно настоящее слово
     * из встроенного словаря (используется в режиме "без чистого шума").
     */
    fun generatePageWithGuaranteedWord(address: Address): String {
        val page = generatePage(address).toCharArray()
        val word = SEED_WORDS.random()
        val maxStart = (page.size - word.length).coerceAtLeast(0)
        val start = if (maxStart > 0) (0 until maxStart).random() else 0
        for (i in word.indices) {
            if (start + i < page.size) page[start + i] = word[i]
        }
        return String(page)
    }

    private val SEED_WORDS = listOf(
        "человек", "время", "книга", "судьба", "истина", "хаос", "порядок",
        "вечность", "смысл", "тень", "свет", "память", "слово", "тишина"
    )
}
