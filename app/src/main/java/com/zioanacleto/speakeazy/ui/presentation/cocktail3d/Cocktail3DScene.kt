package com.zioanacleto.speakeazy.ui.presentation.cocktail3d

import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.BuildConfig
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.controller.Cocktail3DSceneController
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.models.CocktailUiScene
import com.zioanacleto.speakeazy.ui.presentation.cocktail3d.models.toUiScene
import io.github.sceneview.Scene
import io.github.sceneview.animation.Transition.animateRotation
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.CameraNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberOnGestureListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@Composable
fun Cocktail3DScene(
    modifier: Modifier = Modifier,
    controller: Cocktail3DSceneController = koinInject(),
    onModelLoaded: (Boolean) -> Unit
) {
    // 1. Get a coroutine scope that is tied to this Composable's lifecycle
    val coroutineScope = rememberCoroutineScope()
    // 2. A reference to the timer job, so we can cancel it if needed
    var resetJob: Job? = null

    Box(modifier = modifier) {
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val environmentLoader = rememberEnvironmentLoader(engine) // todo: add environment

        val startPosition = Position(x = 0f, y = 0.33f, z = 0f)

        val centerNode = rememberNode(engine) {
            position = startPosition
        }

        val cameraNode = rememberCameraNode(engine) {
            position = Position(y = 0.5f, z = 2.0f)
            lookAt(centerNode)
            centerNode.addChildNode(this)
        }

        val cameraTransition = rememberInfiniteTransition(label = "CameraTransition")
        val cameraRotation by cameraTransition.animateRotation(
            initialValue = Rotation(y = 0.0f),
            targetValue = Rotation(y = 360.0f),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 7.seconds.toInt(DurationUnit.MILLISECONDS))
            )
        )

        val currentModel by controller.currentModel.collectAsState()
        val currentScene by controller.currentScene.collectAsState()

        // Serve un side effect quando cambia currentScene
        LaunchedEffect(currentScene) {
            AnacletoLogger.mumbling(
                mumble = "Scene changed to $currentScene"
            )
            (centerNode to cameraNode).animateScene(currentScene.toUiScene())
        }

        Scene(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            cameraNode = cameraNode,
            childNodes = listOf(
                centerNode,
                rememberNode {
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance(
                            assetFileLocation = "models/${currentModel.sceneModelName}.glb"
                        ),
                        scaleToUnits = 0.85f
                    )
                }),
            // environment?
            /*environment = environmentLoader.createHDREnvironment(
                assetFileLocation = "environments/sky_2k.hdr"
            )!!,*/
            onFrame = {
                onModelLoaded(true)
                // Only rotate if no one is interacting
                if (resetJob?.isCompleted != false) {
                    centerNode.rotation = cameraRotation
                }
                cameraNode.lookAt(centerNode)
            },
            onGestureListener = rememberOnGestureListener(
                onMoveBegin = { _, _, _ ->
                    // 4. Cancel any pending reset timer when user interaction starts
                    resetJob?.cancel()
                },
                onDoubleTap = { _, node ->
                    node?.apply {
                        scale *= 2.0f
                    }
                },
                onMoveEnd = { _, _, _ ->
                    AnacletoLogger.mumbling(
                        mumble = "We've finished scrolling, starting timer"
                    )
                    // 5. Start a new reset timer when the user stops moving the camera
                    resetJob = coroutineScope.launch {
                        delay(5.seconds)

                        // Reset the camera's position after the delay
                        cameraNode.apply {
                            position = Position(y = 0.5f, z = 2.0f)
                            lookAt(centerNode)
                        }
                    }
                }
            )
        )

        if (BuildConfig.DEBUG) {
            Text(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 50.dp, end = 10.dp),
                text = currentScene::class.simpleName ?: "Unknown scene",
                color = Color.White
            )
        }
    }
}

typealias Camera = Pair<Node, CameraNode>

private suspend fun Camera.animateScene(scene: CocktailUiScene) = coroutineScope {
    val (centerNode, cameraNode) = this@animateScene

    launch { centerNode.animateToPositionSmooth(scene.centerPosition) }
    launch { cameraNode.animateToPositionSmooth(scene.cameraPosition) }
}

private suspend fun Node.animateToPositionSmooth(
    target: Position,
    duration: Long = 600L
) {
    val start = position
    val steps = 60
    val deltaTime = duration / steps

    for (i in 0..steps) {
        val t = i / steps.toFloat()
        val eased = t * t * (3 - 2 * t) // smoothstep
        position = Position(
            x = start.x + (target.x - start.x) * eased,
            y = start.y + (target.y - start.y) * eased,
            z = start.z + (target.z - start.z) * eased,
        )
        delay(deltaTime)
    }
}