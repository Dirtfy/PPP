package com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble

import com.dirtfy.ppp.ui.presenter.viewmodel.Bubble
import kotlinx.coroutines.flow.MutableStateFlow

class IsAccountCreateModeBubble: Bubble<Boolean> {
    override val backingProperty: MutableStateFlow<Boolean>
    = MutableStateFlow(false)

}