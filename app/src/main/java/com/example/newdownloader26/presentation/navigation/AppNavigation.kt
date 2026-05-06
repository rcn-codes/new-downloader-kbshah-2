package com.example.newdownloader26.presentation.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.newdownloader26.R
import com.example.newdownloader26.core.localization.AppLocale
import com.example.newdownloader26.presentation.components.AppBottomBar
import com.example.newdownloader26.presentation.components.AppTopBar
import com.example.newdownloader26.presentation.components.BottomDestination
import com.example.newdownloader26.presentation.downloads.DownloadsScreen
import com.example.newdownloader26.presentation.downloads.DownloadsViewModel
import com.example.newdownloader26.presentation.downloader.DownloadPlatform
import com.example.newdownloader26.presentation.downloader.DownloaderScreen
import com.example.newdownloader26.presentation.downloader.DownloaderViewModel
import com.example.newdownloader26.presentation.downloader.FacebookPlatformScreen
import com.example.newdownloader26.presentation.downloader.InstagramPlatformScreen
import com.example.newdownloader26.presentation.downloader.LinkedInPlatformScreen
import com.example.newdownloader26.presentation.downloader.TikTokPlatformScreen
import com.example.newdownloader26.presentation.downloader.XPlatformScreen
import com.example.newdownloader26.presentation.onboarding.LanguageOption
import com.example.newdownloader26.presentation.onboarding.LanguageScreen
import com.example.newdownloader26.presentation.onboarding.LanguageViewModel
import com.example.newdownloader26.presentation.onboarding.SplashScreen
import com.example.newdownloader26.presentation.player.VideoPlayerScreen
import com.example.newdownloader26.presentation.pro.ProUpgradeScreen
import com.example.newdownloader26.presentation.settings.SettingsScreen
import com.example.newdownloader26.presentation.settings.SettingsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Map.entry

// ─── Routes ───────────────────────────────────────────────────────────────────

@Serializable private object SplashRoute : NavKey
@Serializable private object LanguageRoute : NavKey
@Serializable private object HomeRoute : NavKey
@Serializable private object DownloadsRoute : NavKey
@Serializable private object TikTokRoute : NavKey
@Serializable private object InstagramRoute : NavKey
@Serializable private object FacebookRoute : NavKey
@Serializable private object LinkedInRoute : NavKey
@Serializable private object XRoute : NavKey
@Serializable private object PlayerRoute : NavKey
@Serializable private object SettingsRoute : NavKey
@Serializable private object ProUpgradeRoute : NavKey

// ─── Animation ────────────────────────────────────────────────────────────────

/**
 * Single duration used by TopBar, BottomBar AND NavDisplay so every layer
 * animates in perfect sync — no bar-before-screen or screen-before-bar jank.
 */
private const val NAV_ANIM_MS = 0

private val <S> AnimatedContentTransitionScope<S>.sharedFadeSpec: ContentTransform
    get() = fadeIn(tween(NAV_ANIM_MS)) togetherWith fadeOut(tween(NAV_ANIM_MS))

// ─── TopBar state key ─────────────────────────────────────────────────────────

private sealed interface TopBarKey {
    data object Hidden : TopBarKey
    data object Language : TopBarKey
    data class Main(val route: NavKey) : TopBarKey
}

private fun topBarKey(route: NavKey?): TopBarKey = when (route) {
    null, SplashRoute, ProUpgradeRoute -> TopBarKey.Hidden
    LanguageRoute -> TopBarKey.Language
    else -> TopBarKey.Main(route)
}

// ─── Root composable ──────────────────────────────────────────────────────────

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(SplashRoute)
    val currentRoute = backStack.lastOrNull()
    var selectedVideoUri by remember { mutableStateOf<String?>(null) }

    // Language
    val initialTag = remember { AppLocale.getSavedLanguageTag(context) ?: "en" }
    val languageViewModel: LanguageViewModel =
        koinViewModel(parameters = { parametersOf(initialTag) })
    val selectedLanguageTag by languageViewModel.selectedTag.collectAsStateWithLifecycle()

    // ✅ Decorators called directly in composable scope — NOT inside remember { }
    val saveableDecorator = rememberSaveableStateHolderNavEntryDecorator<NavKey>()
    val viewModelDecorator = rememberViewModelStoreNavEntryDecorator<NavKey>()
    val entryDecorators = remember(saveableDecorator, viewModelDecorator) {
        listOf(saveableDecorator, viewModelDecorator)
    }

    // ✅ Stable entry provider — plain remember, no backStack dependency
    val provider = remember {
        entryProvider {
            entry<SplashRoute> {

                SplashScreen( onFinished = { if (AppLocale.getSavedLanguageTag(context).isNullOrBlank()) { backStack.add(LanguageRoute) } else { backStack.add(HomeRoute) } } )
            }

            entry<LanguageRoute> {
                LanguageScreen(
                    selectedTag = selectedLanguageTag,
                    onSelect = { option: LanguageOption ->
                        languageViewModel.selectLanguage(option.languageTag)
                    }
                )
            }

            entry<HomeRoute> {
                HomeRouteScreen(
                    onOpenPlatform = { platform ->
                        when (platform) {
                            DownloadPlatform.TIKTOK    -> backStack.add(TikTokRoute)
                            DownloadPlatform.INSTAGRAM -> backStack.add(InstagramRoute)
                            DownloadPlatform.FACEBOOK  -> backStack.add(FacebookRoute)
                            DownloadPlatform.LINKEDIN  -> backStack.add(LinkedInRoute)
                            DownloadPlatform.X         -> backStack.add(XRoute)
                        }
                    },
                    onViewDownloads = {
                        backStack.removeIf { it != HomeRoute }
                        backStack.add(DownloadsRoute)
                    }
                )
            }

            entry<DownloadsRoute> {
                DownloadsRouteScreen(
                    onOpenVideo = { uri ->
                        selectedVideoUri = uri
                        backStack.add(PlayerRoute)
                    }
                )
            }

            entry<TikTokRoute>    { TikTokPlatformScreen() }
            entry<InstagramRoute> { InstagramPlatformScreen() }
            entry<FacebookRoute>  { FacebookPlatformScreen() }
            entry<LinkedInRoute>  { LinkedInPlatformScreen() }
            entry<XRoute>         { XPlatformScreen() }

            entry<PlayerRoute> {
                selectedVideoUri?.let { VideoPlayerScreen(videoUri = it) }
            }

            entry<SettingsRoute> {
                SettingsRouteScreen(
                    onChangeLanguage = { backStack.add(LanguageRoute) },
                    onOpenProUpgrade = { backStack.add(ProUpgradeRoute) }
                )
            }

            entry<ProUpgradeRoute> {
                val ctx = LocalContext.current
                ProUpgradeScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onBuyNow = {
                        Toast.makeText(
                            ctx,
                            ctx.getString(R.string.settings_try_pro_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onOpenTerms = {
                        ctx.openExternalUrl(ctx.getString(R.string.pro_terms_url))
                    },
                    onOpenPrivacy = {
                        ctx.openExternalUrl(ctx.getString(R.string.settings_privacy_policy_url))
                    },
                    onRestorePurchase = {
                        Toast.makeText(
                            ctx,
                            ctx.getString(R.string.pro_restore_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }

    val entries = rememberDecoratedNavEntries(
        backStack       = backStack,
        entryDecorators = entryDecorators,
        entryProvider   = provider
    )

    // Derived display state
    val showBottomBar = currentRoute == HomeRoute || currentRoute == DownloadsRoute
    val bottomDest    = if (currentRoute == DownloadsRoute) BottomDestination.DOWNLOADS
    else BottomDestination.HOME
    val topKey        = topBarKey(currentRoute)

    // ✅ Stable back handler
    val onBack: () -> Unit = remember(backStack) {
        {
            if (backStack.size > 1) backStack.removeLastOrNull()
            if (backStack.isEmpty()) backStack.add(HomeRoute)
        }
    }

    // ✅ MutableInteractionSource hoisted here — safe composable scope
    val languageInteraction = remember { MutableInteractionSource() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // ── TopBar — synced to NAV_ANIM_MS ────────────────────────────
            AnimatedContent(
                targetState  = topKey,
                transitionSpec = { sharedFadeSpec },
                label        = "nav_top_bar",
            ) { key ->
                when (key) {
                    TopBarKey.Hidden -> Unit

                    TopBarKey.Language -> {
                        AppTopBar(
                            title       = null,
                            showBack    = true,
                            onBackClick = { backStack.removeLastOrNull() },
                            showPremium = false,
                            trailingContent = {
                                Icon(
                                    painter            = painterResource(R.drawable.ic_next_language),
                                    contentDescription = stringResource(R.string.language_confirm),
                                    tint               = Color.Unspecified,
                                    modifier           = Modifier
                                        .size(width = 47.dp, height = 32.dp)
                                        .clickable(
                                            interactionSource = languageInteraction,
                                            indication        = null
                                        ) {
                                            AppLocale.applyLanguageTag(context, selectedLanguageTag)
                                            AppLocale.saveLanguageTag(context, selectedLanguageTag)
                                            if (backStack.any { it == SettingsRoute }) {
                                                backStack.removeLastOrNull()
                                            } else {
                                                backStack.removeIf { it != HomeRoute }
                                                if (backStack.none { it == HomeRoute }) {
                                                    backStack.add(HomeRoute)
                                                }
                                            }
                                        }
                                )
                            }
                        )
                    }

                    is TopBarKey.Main -> {
                        MainScaffoldTopBar(
                            route     = key.route,
                            backStack = backStack,
                            onBack    = onBack,
                        )
                    }
                }
            }
        },
        bottomBar = {
            // ── BottomBar — synced to NAV_ANIM_MS ─────────────────────────
            AnimatedContent(
                targetState  = showBottomBar,
                transitionSpec = { sharedFadeSpec },
                label        = "nav_bottom_bar",
            ) { visible ->
                if (visible) {
                    AppBottomBar(
                        selected         = bottomDest,
                        onHomeClick      = {
                            if (currentRoute != HomeRoute) {
                                backStack.removeIf { it != HomeRoute }
                                if (backStack.none { it == HomeRoute }) backStack.add(HomeRoute)
                            }
                        },
                        onDownloadsClick = {
                            if (currentRoute != DownloadsRoute) {
                                backStack.removeIf { it != HomeRoute }
                                backStack.add(DownloadsRoute)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // ── Screen content — NavDisplay crossfade matches NAV_ANIM_MS ─────
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            entries  = entries,
            onBack   = onBack
        )
    }
}

// ─── TopBar for main routes ───────────────────────────────────────────────────

@Composable
private fun MainScaffoldTopBar(
    route: NavKey,
    backStack: NavBackStack<NavKey>,
    onBack: () -> Unit,
) {
    val platformRoutes = remember {
        setOf(TikTokRoute, InstagramRoute, FacebookRoute, LinkedInRoute, XRoute, PlayerRoute)
    }

    // ✅ All stringResource calls at composable scope — never inside lambdas
    val titleTikTok    = stringResource(R.string.topbar_tiktok_downloader)
    val titleInstagram = stringResource(R.string.topbar_instagram_downloader)
    val titleFacebook  = stringResource(R.string.topbar_facebook_downloader)
    val titleLinkedIn  = stringResource(R.string.topbar_linkedin_downloader)
    val titleX         = stringResource(R.string.topbar_x_downloader)
    val titlePlayer    = stringResource(R.string.topbar_video_player)
    val titleSettings  = stringResource(R.string.settings_title)

    val title = when (route) {
        SettingsRoute  -> titleSettings
        TikTokRoute    -> titleTikTok
        InstagramRoute -> titleInstagram
        FacebookRoute  -> titleFacebook
        LinkedInRoute  -> titleLinkedIn
        XRoute         -> titleX
        PlayerRoute    -> titlePlayer
        else           -> null
    }

    val isPlatform = route in platformRoutes
    val showTopBar = route == HomeRoute || route == DownloadsRoute ||
            (route !in setOf(SplashRoute, LanguageRoute) && (title != null || isPlatform))

    if (!showTopBar) return

    AppTopBar(
        title          = title,
        showBack       = isPlatform || route == SettingsRoute,
        onBackClick    = onBack,
        showPremium    = route != SettingsRoute,
        onPremiumClick = if (route != SettingsRoute) {
            { backStack.add(ProUpgradeRoute) }
        } else null,
        onMenuClick    = if (route == HomeRoute || route == DownloadsRoute) {
            { backStack.add(SettingsRoute) }
        } else null
    )
}

// ─── Route screens (ViewModel wiring) ────────────────────────────────────────

@Composable
private fun HomeRouteScreen(
    onOpenPlatform: (DownloadPlatform) -> Unit,
    onViewDownloads: () -> Unit,
) {
    val viewModel: DownloaderViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    DownloaderScreen(
        state                     = state,
        effect                    = viewModel.effect,
        onIntent                  = viewModel::onIntent,
        onPlatformSelected        = onOpenPlatform,
        onViewDownloads           = onViewDownloads,
        onResumeAutoPaste         = viewModel::tryAutoPasteFromClipboard,
    )
}

@Composable
private fun DownloadsRouteScreen(onOpenVideo: (String) -> Unit) {
    val viewModel: DownloadsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    DownloadsScreen(
        state       = state,
        onIntent    = viewModel::onIntent,
        onOpenVideo = { video -> video.playUri?.let(onOpenVideo) },
    )
}

@Composable
private fun SettingsRouteScreen(
    onChangeLanguage: () -> Unit,
    onOpenProUpgrade: () -> Unit,
) {
    val viewModel: SettingsViewModel = koinViewModel()
    val autoDetectEnabled by viewModel.autoDetectEnabled.collectAsStateWithLifecycle()
    SettingsScreen(
        autoDetectEnabled  = autoDetectEnabled,
        onAutoDetectChange = viewModel::setAutoDetectEnabled,
        onChangeLanguage   = onChangeLanguage,
        onOpenProUpgrade   = onOpenProUpgrade,
    )
}

// ─── Helpers ──────────────────────────────────────────────────────────────────

private fun Context.openExternalUrl(url: String) {
    runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
}