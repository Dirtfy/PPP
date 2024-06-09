package com.dirtfy.ppp.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.dirtfy.ppp.contract.user.DummyUser
import com.dirtfy.ppp.view.tablet.TabletScreen

class MainActivity: ComponentActivity() {

    private val isTablet: Boolean
        get() {
            val screenSizeType =
                resources.configuration.screenLayout and
                        Configuration.SCREENLAYOUT_SIZE_MASK

            return screenSizeType == Configuration.SCREENLAYOUT_SIZE_XLARGE ||
                    screenSizeType == Configuration.SCREENLAYOUT_SIZE_LARGE
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            if (isTablet) {
                TabletScreen.Main(
                    user = DummyUser,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}