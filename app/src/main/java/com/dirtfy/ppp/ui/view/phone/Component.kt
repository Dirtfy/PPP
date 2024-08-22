package com.dirtfy.ppp.ui.view.phone

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object Component {

    @Composable
    fun SearchBar(
        searchClue: String,
        onClueChanged: (String) -> Unit,
        onBarcodeIconClick: () -> Unit,
        onAddIconClick: () -> Unit
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
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
                        placeholder = { Text(text = "account number") },
                        modifier = Modifier.widthIn(200.dp, 400.dp)
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    val barcodeIcon = Icons.Filled.Menu
                    Icon(
                        imageVector = barcodeIcon, contentDescription = barcodeIcon.name,
                        modifier = Modifier
                            .rotate(90f)
                            .clickable {
                                onBarcodeIconClick()
                            }
                    )
                    Spacer(modifier = Modifier.size(10.dp))

                    val addIcon = Icons.Filled.AddCircle
                    Icon(
                        imageVector = addIcon, contentDescription = addIcon.name,
                        modifier = Modifier.clickable {
                            onAddIconClick()
                        }
                    )
                }
            }
        }


    }

    @Composable
    fun InputCard(
        dataList: List<String>,
        labelNameList: List<String>,
        onDataChangedList: List<(String) -> Unit>,
        onAddButtonClicked: (List<String>) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier
        ) {
            Card(
                modifier = Modifier.padding(10.dp),
            ) {
                Box(
                    modifier = Modifier.padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(5.dp)
                        ) {
                            itemsIndexed(labelNameList) {index, item ->
                                TextField(
                                    label = { Text(text = item) },
                                    value = dataList[index],
                                    onValueChange = {
                                        onDataChangedList[index](it)
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Button(
                            onClick = { onAddButtonClicked(dataList) }
                        ) {
                            Text(text = "add")
                        }
                    }

                }

            }
        }
    }
}