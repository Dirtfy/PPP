package com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble

import com.dirtfy.ppp.ui.dto.UiNewAccount
import com.dirtfy.ppp.ui.presenter.viewmodel.Bubble
import kotlinx.coroutines.flow.MutableStateFlow

class NewAccountBubble: Bubble<UiNewAccount> {
    override val backingProperty: MutableStateFlow<UiNewAccount>
    = MutableStateFlow(
        UiNewAccount()
    )
}