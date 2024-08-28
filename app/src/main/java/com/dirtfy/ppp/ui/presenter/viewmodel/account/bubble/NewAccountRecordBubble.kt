package com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble

import com.dirtfy.ppp.ui.dto.UiNewAccountRecord
import com.dirtfy.ppp.ui.presenter.viewmodel.Bubble
import kotlinx.coroutines.flow.MutableStateFlow

class NewAccountRecordBubble: Bubble<UiNewAccountRecord> {
    override val backingProperty: MutableStateFlow<UiNewAccountRecord>
    = MutableStateFlow(UiNewAccountRecord())
}