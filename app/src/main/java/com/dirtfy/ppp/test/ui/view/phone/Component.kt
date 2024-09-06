package com.dirtfy.ppp.test.ui.view.phone

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object Component {

    @Composable
    fun SearchBar(
        searchClue: String,
        onClueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeholder: String = "",
        trailingContent: @Composable () -> Unit = {},
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            Card(
                modifier = Modifier.padding(20.dp, 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                        .wrapContentWidth()
                ) {
                    val searchIcon = Icons.Filled.Search
                    Icon(imageVector = searchIcon, contentDescription = searchIcon.name)
                    TextField(
                        value = searchClue,
                        onValueChange = onClueChanged,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = { Text(text = placeholder) },
                        singleLine = true,
                        modifier = Modifier.widthIn(200.dp, 400.dp)
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    trailingContent()
                }
            }
        }


    }

}