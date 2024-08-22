package com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble

import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.presenter.viewmodel.Bubble
import kotlinx.coroutines.flow.MutableStateFlow

class NowAccountBubble: Bubble<UiAccount> {
    override val backingProperty: MutableStateFlow<UiAccount>
    = MutableStateFlow(UiAccount(
        "","","","",""
    ))
}