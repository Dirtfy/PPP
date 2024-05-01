package com.dirtfy.ppp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dirtfy.ppp.accounting.accounting.view.AccountTest
import com.dirtfy.ppp.ui.theme.PPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PPPTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen("Android")
                }
            }
        }
    }
}

@Composable
fun MainScreen(name: String, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PPPScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = PPPScreen.Start.name) {
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Button(onClick = {
                        navController.navigate(PPPScreen.Account.name)
                    }) {
                        Text(text = "account test")
                    }
                    Button(onClick = {
                        navController.navigate(PPPScreen.AccountRecord.name)
                    }) {
                        Text(text = "account record test")
                    }
                }
            }
            composable(route = PPPScreen.Account.name) {
                AccountTest()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    PPPTheme {
        MainScreen("Android")
    }
}