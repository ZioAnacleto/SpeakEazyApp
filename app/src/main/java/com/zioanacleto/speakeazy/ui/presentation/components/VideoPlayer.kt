package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.annotation.OptIn
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.compose.ContentFrame
import org.koin.androidx.compose.get

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    modifier: Modifier,
    videoUrl: String,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val exoPlayer = buildExoPlayer(videoUrl)

    // this boolean will tell us if the video actually started on the view
    var isFirstFrameRendered by remember { mutableStateOf(false) }
    // this boolean is bounded to lifecycle states ON_PAUSE and ON_RESUME
    var isLifecycleActive by remember { mutableStateOf(true) }

    DisposableEffect(exoPlayer) {
        val playerListener = object : Player.Listener {
            override fun onRenderedFirstFrame() {
                isFirstFrameRendered = true
            }
        }

        val lifecycleObserver = exoPlayer.lifecycleObserver(
            onResume = { isLifecycleActive = true },
            onPause = { isLifecycleActive = false }
        )

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        exoPlayer.addListener(playerListener)

        onDispose {
            exoPlayer.removeListener(playerListener)
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            exoPlayer.stop()
            exoPlayer.clearMediaItems()
            exoPlayer.release()
        }
    }

    // this helps in the VideoPlayer composable appearance transition when player is ready
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isFirstFrameRendered && isLifecycleActive) 0f else 1f,
        animationSpec = tween(500, delayMillis = 200),
        label = "overlayAlpha"
    )

    Box(
        modifier = modifier
    ) {
        if (isLifecycleActive) {
            Media3ContentFrame(
                modifier,
                exoPlayer
            ) {
                Box(
                    Modifier
                        .matchParentSize()
                        .graphicsLayer { alpha = overlayAlpha }
                        .background(Color.Black)
                )
            }
        } else {
            Box(
                Modifier
                    .matchParentSize()
                    .background(Color.Black)
            )
        }
    }
}

@Composable
private fun Media3ContentFrame(
    modifier: Modifier = Modifier,
    player: ExoPlayer?,
    shutter: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        ContentFrame(
            player = player,
            modifier = modifier,
            contentScale = ContentScale.Crop,
            shutter = shutter
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun buildExoPlayer(videoUrl: String): ExoPlayer {
    val context = LocalContext.current
    val simpleCache: SimpleCache = get()

    // cacheing video to avoid multiple download of it
    val dataSourceFactory = remember { buildCachedDataSource(simpleCache) }
    val mediaSource = remember(videoUrl) {
        ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoUrl))
    }

    return remember(videoUrl) {
        ExoPlayer
            .Builder(context)
            .build().apply {
                setMediaSource(mediaSource)
                repeatMode = Player.REPEAT_MODE_ONE
                volume = 0f
                prepare()
                playWhenReady = true
            }
    }
}

private fun ExoPlayer.lifecycleObserver(
    onResume: () -> Unit,
    onPause: () -> Unit
) = LifecycleEventObserver { _, event ->
    when (event) {
        Lifecycle.Event.ON_RESUME -> {
            onResume()
            play()
        }

        Lifecycle.Event.ON_PAUSE -> {
            onPause()
            pause()
        }

        else -> Unit
    }
}

@OptIn(UnstableApi::class)
private fun buildCachedDataSource(
    cache: SimpleCache
): DataSource.Factory {
    val upstreamFactory = DefaultHttpDataSource.Factory()

    return CacheDataSource.Factory()
        .setCache(cache)
        .setUpstreamDataSourceFactory(upstreamFactory)
        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
}
