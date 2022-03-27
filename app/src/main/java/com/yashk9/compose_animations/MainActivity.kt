package com.yashk9.compose_animations

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yashk9.compose_animations.ui.screen.AStar
import com.yashk9.compose_animations.ui.screen.FlashCards
import com.yashk9.compose_animations.ui.screen.WeatherAnimation
import com.yashk9.compose_animations.ui.theme.BlueBg
import com.yashk9.compose_animations.ui.theme.ComposeanimationsTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeanimationsTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(Color.White)
                Surface(color = MaterialTheme.colors.background) {
                    val pagerState = rememberPagerState()
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        HorizontalPager(
                            modifier = Modifier.fillMaxSize(),
                            count = 3, state = pagerState,
                        ) { page ->
                            when(page){
                                0 -> FlashCards()
                                1 -> AStar()
                                2 -> WeatherAnimation()
                            }
                        }
                    }
                }
            }
        }
    }
}