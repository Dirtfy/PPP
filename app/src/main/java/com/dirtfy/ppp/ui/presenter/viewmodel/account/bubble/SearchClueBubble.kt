package com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble

import com.dirtfy.ppp.ui.presenter.viewmodel.Bubble
import kotlinx.coroutines.flow.MutableStateFlow

class SearchClueBubble: Bubble<String> {
    override val backingProperty: MutableStateFlow<String>
    = MutableStateFlow(
        ""
    )
}