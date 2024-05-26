package com.dirtfy.ppp.view.tablet.selling.tabling

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dirtfy.ppp.view.ui.theme.PPPTheme

@Composable
fun Table(
    tableNumber: Int,
    tableColor: Color
) {
    Box(
        modifier = Modifier
            .size(35.dp)
            .background(tableColor)
    ) {
        Text(text = "$tableNumber")
    }
}

@Composable
fun TableScreen(
    tableStateList: List<Int>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(10),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(tableStateList) {
            Table(it, Color.LightGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TableScreenPreview() {
    val testList by remember {
        mutableStateOf(
            listOf(
                11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
                 0,  0, 0, 0, 0, 0, 0, 0, 0, 2,
                 0,  0, 0, 0, 0, 0, 0, 0, 0, 1
            )
        )
    }

    PPPTheme {
        TableScreen(testList)
    }
}
