package com.yashk9.compose_animations.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yashk9.compose_animations.util.Util
import com.yashk9.compose_animations.ui.components.DraggableCard
import com.yashk9.compose_animations.ui.theme.ComposeanimationsTheme

// Checkout DraggableCard from the Compose Cookbook
// https://github.com/Gurupreet/ComposeCookBook/tree/master/demos/datingapp/src/main/java/com/guru/composecookbook/datingapp/components/home

@Composable
fun FlashCards() {
    ComposeanimationsTheme {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val cardHeight = screenHeight - 500.dp

        Surface(modifier = Modifier.fillMaxSize()) {
            val cards = Util.flashCards
            val listEmpty = remember { mutableStateOf(false) }

            Scaffold(topBar = {
                TopAppBar(
                    title = { Text("Flash Cards") },
                )
            }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        cards.forEachIndexed { index, card ->
                            DraggableCard(
                                card = card,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(cardHeight)
                                    .padding(
                                        top = 16.dp + (index + 2).dp,
                                        bottom = 16.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    ),
                                onSwiped = { _, swipedCard ->
                                    if (cards.isNotEmpty()) {
                                        cards.remove(swipedCard)
                                        if (cards.isEmpty()) {
                                            listEmpty.value = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (listEmpty.value) Text("Well Done!", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}