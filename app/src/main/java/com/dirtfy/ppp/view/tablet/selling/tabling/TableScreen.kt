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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dirtfy.ppp.contract.DummyUser
import com.dirtfy.ppp.contract.User
import com.dirtfy.ppp.contract.view.tabling.TableScreenContract
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object TableScreen: TableScreenContract.API {
    @Composable
    override fun Table(
        table: TableScreenContract.DTO.Table,
        user: User,
        modifier: Modifier
    ) {
        Box(
            modifier = modifier
        ) {
            Text(text = table.number)
        }
    }

    @Composable
    override fun TableLayout(
        tableList: List<TableScreenContract.DTO.Table>,
        user: User,
        modifier: Modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(10),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier
        ) {
            items(tableList) {
                Table(
                    table = it,
                    user = user,
                    modifier = Modifier
                        .size(35.dp)
                        .background(Color(it.color))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TableScreenPreview() {
    val tableList: List<TableScreenContract.DTO.Table> =
        listOf(
            11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 2,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 1
        ).map {
            TableScreenContract.DTO.Table("$it", Color.LightGray.value)
        }

    PPPTheme {
        TableScreen.TableLayout(
            tableList = tableList,
            user = DummyUser,
            modifier = Modifier
        )
    }
}
