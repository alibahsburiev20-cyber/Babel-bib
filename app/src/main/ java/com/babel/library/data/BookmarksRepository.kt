package com.babel.library.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.babel.library.core.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "babel_library_prefs")

/**
 * Хранит закладки как набор строк-адресов ("7F-3A-12-06-0042") в DataStore.
 * Простое персистентное хранилище, без базы данных — достаточно для MVP.
 */
class BookmarksRepository(private val context: Context) {

    private val BOOKMARKS_KEY = stringSetPreferencesKey("bookmarks")

    val bookmarksFlow: Flow<List<Address>> = context.dataStore.data.map { prefs ->
        val raw = prefs[BOOKMARKS_KEY] ?: emptySet()
        raw.mapNotNull { Address.parse(it) }
    }

    suspend fun addBookmark(address: Address) {
        context.dataStore.edit { prefs ->
            val current = prefs[BOOKMARKS_KEY] ?: emptySet()
            prefs[BOOKMARKS_KEY] = current + address.toDisplayString()
        }
    }

    suspend fun removeBookmark(address: Address) {
        context.dataStore.edit { prefs ->
            val current = prefs[BOOKMARKS_KEY] ?: emptySet()
            prefs[BOOKMARKS_KEY] = current - address.toDisplayString()
        }
    }

    suspend fun isBookmarked(address: Address): Boolean {
        var result = false
        context.dataStore.data.map { prefs ->
            (prefs[BOOKMARKS_KEY] ?: emptySet()).contains(address.toDisplayString())
        }.collectFirstInto { result = it }
        return result
    }

    // Небольшой хелпер, чтобы не тащить весь Flow.first() импорт отдельно
    private suspend fun Flow<Boolean>.collectFirstInto(setter: (Boolean) -> Unit) {
        kotlinx.coroutines.flow.firstOrNull(this)?.let(setter)
    }
}

/** Настройки приложения (переключатель "гарантировать слово" и т.д.) */
class SettingsRepository(private val context: Context) {

    private val GUARANTEE_WORD_KEY = androidx.datastore.preferences.core.booleanPreferencesKey("guarantee_word")

    val guaranteeWordFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[GUARANTEE_WORD_KEY] ?: false
    }

    suspend fun setGuaranteeWord(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[GUARANTEE_WORD_KEY] = value
        }
    }
}
