package com.babel.library.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.babel.library.core.Address
import com.babel.library.core.SearchEncoder
import com.babel.library.data.BookmarksRepository
import com.babel.library.data.SettingsRepository
import com.babel.library.ui.screens.*
import kotlinx.coroutines.launch

private object Routes {
    const val SPLASH = "splash"
    const val MAIN = "main"
    const val SEARCH = "search"
    const val SEARCH_RESULTS = "search_results"
    const val RANDOM = "random"
    const val PAGE_VIEW = "page_view"
    const val ADDRESS_INPUT = "address_input"
    const val BOOKMARKS = "bookmarks"
    const val SETTINGS = "settings"
}

@Composable
fun BabelNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val bookmarksRepo = remember { BookmarksRepository(context) }
    val settingsRepo = remember { SettingsRepository(context) }
    val scope = rememberCoroutineScope()

    var lastAddress by remember { mutableStateOf<Address?>(null) }
    var currentSearchQuery by remember { mutableStateOf("") }
    var currentSearchResults by remember { mutableStateOf<List<SearchEncoder.SearchResult>>(emptyList()) }
    var currentHighlight by remember { mutableStateOf<IntRange?>(null) }
    var pendingRandomAddress by remember { mutableStateOf(Address.random()) }

    val bookmarks by bookmarksRepo.bookmarksFlow.collectAsState(initial = emptyList())
    val guaranteeWord by settingsRepo.guaranteeWordFlow.collectAsState(initial = false)

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(onFinished = {
                navController.navigate(Routes.MAIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }

        composable(Routes.MAIN) {
            ScaffoldWithBottomBar(
                navController = navController,
                current = null
            ) {
                MainScreen(
                    lastAddress = lastAddress,
                    onSearchClick = { navController.navigate(Routes.SEARCH) },
                    onGenerateRandom = {
                        pendingRandomAddress = Address.random()
                        navController.navigate(Routes.RANDOM)
                    },
                    onOpenAddress = {
                        currentHighlight = null
                        navController.navigate(Routes.PAGE_VIEW)
                    },
                    onSettingsClick = { navController.navigate(Routes.SETTINGS) }
                )
            }
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onSearch = { query ->
                    currentSearchQuery = query
                    currentSearchResults = SearchEncoder.search(query)
                    navController.navigate(Routes.SEARCH_RESULTS)
                }
            )
        }

        composable(Routes.SEARCH_RESULTS) {
            SearchResultsScreen(
                query = currentSearchQuery,
                results = currentSearchResults,
                onBack = { navController.popBackStack() },
                onOpenResult = { result ->
                    lastAddress = result.address
                    currentHighlight = result.matchStart until result.matchEnd
                    navController.navigate(Routes.PAGE_VIEW)
                }
            )
        }

        composable(Routes.RANDOM) {
            RandomPageScreen(
                address = pendingRandomAddress,
                onBack = { navController.popBackStack() },
                onGenerationComplete = {
                    lastAddress = pendingRandomAddress
                    currentHighlight = null
                    navController.navigate(Routes.PAGE_VIEW) {
                        popUpTo(Routes.RANDOM) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.PAGE_VIEW) {
            val address = lastAddress ?: pendingRandomAddress
            val isBookmarked = bookmarks.any { it == address }
            PageViewScreen(
                address = address,
                highlightRange = currentHighlight,
                isBookmarked = isBookmarked,
                onBack = { navController.popBackStack() },
                onToggleBookmark = {
                    scope.launch {
                        if (isBookmarked) bookmarksRepo.removeBookmark(address)
                        else bookmarksRepo.addBookmark(address)
                    }
                },
                onPrevPage = {
                    val current = address
                    if (current.page > 0) {
                        lastAddress = current.copy(page = current.page - 1)
                        currentHighlight = null
                    }
                },
                onNextPage = {
                    val current = address
                    if (current.page < 409) {
                        lastAddress = current.copy(page = current.page + 1)
                        currentHighlight = null
                    }
                }
            )
        }

        composable(Routes.ADDRESS_INPUT) {
            ScaffoldWithBottomBar(navController = navController, current = BabelDestination.ADDRESS) {
                AddressInputScreen(
                    onBack = { navController.popBackStack() },
                    onNavigate = { address ->
                        lastAddress = address
                        currentHighlight = null
                        navController.navigate(Routes.PAGE_VIEW)
                    }
                )
            }
        }

        composable(Routes.BOOKMARKS) {
            ScaffoldWithBottomBar(navController = navController, current = BabelDestination.BOOKMARKS) {
                BookmarksScreen(
                    bookmarks = bookmarks,
                    onOpen = { address ->
                        lastAddress = address
                        currentHighlight = null
                        navController.navigate(Routes.PAGE_VIEW)
                    },
                    onRemove = { address ->
                        scope.launch { bookmarksRepo.removeBookmark(address) }
                    }
                )
            }
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                guaranteeWord = guaranteeWord,
                onGuaranteeWordChange = { value ->
                    scope.launch { settingsRepo.setGuaranteeWord(value) }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun ScaffoldWithBottomBar(
    navController: NavHostController,
    current: BabelDestination?,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BabelBottomBar(
                current = current ?: BabelDestination.SEARCH,
                onSelect = { dest ->
                    val route = when (dest) {
                        BabelDestination.SEARCH -> Routes.SEARCH
                        BabelDestination.RANDOM -> Routes.MAIN
                        BabelDestination.BOOKMARKS -> Routes.BOOKMARKS
                        BabelDestination.ADDRESS -> Routes.ADDRESS_INPUT
                    }
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            content()
        }
    }
}
