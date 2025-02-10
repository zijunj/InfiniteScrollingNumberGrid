package com.example.infinitescrollingnumbergrid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InfiniteNumberGrid() {
    var numbers by remember { mutableStateOf((1..30).toMutableList()) }
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title and Reset Button in the Same Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Infinite Number Grid",
                fontSize = 24.sp,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = { numbers = (1..30).toMutableList() },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Reset")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = gridState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(numbers) { number ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(65.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = number.toString(), fontSize = 20.sp)
                }
            }
        }

        // Coroutine to dynamically add 30 more numbers when scrolling to the bottom
        LaunchedEffect(gridState) {
            snapshotFlow { gridState.layoutInfo.visibleItemsInfo }
                .collect { visibleItems ->
                    val lastVisibleIndex = visibleItems.lastOrNull()?.index ?: 0
                    if (lastVisibleIndex >= numbers.size - 6) { // Load more when near the bottom
                        coroutineScope.launch {
                            delay(1000) // Simulate network delay
                            numbers = (numbers + (numbers.size + 1..numbers.size + 30).toList()).toMutableList()
                        }
                    }
                }
        }
    }
}

