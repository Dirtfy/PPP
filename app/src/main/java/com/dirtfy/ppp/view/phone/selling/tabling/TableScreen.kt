package com.dirtfy.ppp.view.phone.selling.tabling

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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dirtfy.ppp.contract.view.tabling.TableScreenContract
import com.dirtfy.ppp.contract.viewmodel.TablingContract
import com.dirtfy.ppp.contract.viewmodel.user.DummyUser
import com.dirtfy.ppp.contract.viewmodel.user.User
import com.dirtfy.ppp.view.ui.theme.PPPTheme

object TableScreen: TableScreenContract.API {
    @Composable
    override fun Table(
        table: TablingContract.DTO.Table,
        user: User,
        modifier: Modifier
    ) {
        Box(
            modifier = modifier
                .size(35.dp)
                .background(Color(table.color))
        ) {
            Text(text = table.number)
        }
    }

    @Composable
    override fun TableLayout(
        tableList: List<TablingContract.DTO.Table>,
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
                when(it.number.toInt()) {
                    0 -> {
                        Table(
                            table = TablingContract.DTO.Table(
                                number = "",
                                color = Color.Transparent.value
                            ),
                            user = user,
                            modifier = Modifier
                        )
                    }
                    else -> {
                        Table(
                            table = it,
                            user = user,
                            modifier = Modifier
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun TableScreenPreview() {
    val tableList: List<TablingContract.DTO.Table> =
        listOf(
            11, 10, 9, 8, 7, 6, 5, 4, 3, 0,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 2,
            0,  0, 0, 0, 0, 0, 0, 0, 0, 1
        ).map {
            TablingContract.DTO.Table("$it", Color.LightGray.value)
        }

    PPPTheme {
        TableScreen.TableLayout(
            tableList = tableList,
            user = DummyUser,
            modifier = Modifier
        )
    }
}
