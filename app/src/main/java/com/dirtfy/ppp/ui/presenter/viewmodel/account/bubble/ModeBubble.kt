package com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble

import com.dirtfy.ppp.ui.dto.UiAccountMode
import com.dirtfy.ppp.ui.presenter.viewmodel.Bubble
import kotlinx.coroutines.flow.MutableStateFlow

class ModeBubble: Bubble<UiAccountMode> {
    override val backingProperty: MutableStateFlow<UiAccountMode>
    = MutableStateFlow(UiAccountMode.Main)
}