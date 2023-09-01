package com.example.stopwatch

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stopwatch.ui.theme.MainViewModel
import com.example.stopwatch.ui.theme.StopWatchTheme
import kotlin.time.ExperimentalTime

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTime::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopWatchTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
MainApp(viewModel = MainViewModel())
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun MainApp(viewModel: MainViewModel) {
    MainApp(
        isPlaying = viewModel.isPlaying,
        seconds = viewModel.seconds,
        minutes = viewModel.minutes,
        hours = viewModel.hours,
        onStart = { viewModel.start() },
        onPause = { viewModel.pause() },
        onStop = { viewModel.stop() }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MainApp(
    isPlaying: Boolean,
    seconds: String,
    minutes: String,
    hours: String,
    onStart: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
) {
    Scaffold {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Row {
                val numberTransitionSpec: AnimatedContentScope<String>.() -> ContentTransform = {
                    slideInVertically(
                        initialOffsetY = { it }
                    ) + fadeIn() with slideOutVertically(
                        targetOffsetY = { -it }
                    ) + fadeOut() using SizeTransform(
                        false
                    )
                }

                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.headlineLarge) {
                    AnimatedContent(targetState = hours, transitionSpec = numberTransitionSpec) {
                        Text(text = it)
                    }
                    Text(text = ":")
                    AnimatedContent(targetState = minutes, transitionSpec = numberTransitionSpec) {
                        Text(text = it)
                    }
                    Text(text = ":")
                    AnimatedContent(targetState = seconds, transitionSpec = numberTransitionSpec) {
                        Text(text = it)
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.background(Color.LightGray, RoundedCornerShape(50))
            ) {
                AnimatedContent(targetState = isPlaying) {
                    if (it)
                        IconButton(onClick = onPause) {
                            Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "")
                        }
                    else
                        IconButton(onClick = onStart) {
                            Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "")
                        }
                }
                IconButton(onClick = onStop) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}