package com.dirtfy.ppp.ui.presenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.holder.MenuViewModel

class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Main(
                viewModel = MenuViewModel()
            )
        }
    }

    @Composable
    fun Main(
        viewModel: MenuViewModel
    ) {
        val menuListState by viewModel.menuList.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = viewModel) {
            viewModel.updateMenuList()
        }

        when(menuListState) {
            is FlowState.Loading -> {
                Surface {
                    Text(text = "loading..")
                }
            }
            is FlowState.Success -> {
                val menuList = (menuListState as FlowState.Success<List<MenuViewModel.Menu>>).data

                LazyColumn {
                    items(menuList) {
                        Row {
                            Text(text = it.name)
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(text = it.price)
                        }
                    }
                }
            }
            is FlowState.Failed -> {

            }
        }
    }
}