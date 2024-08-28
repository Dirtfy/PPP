package com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiAccountRecord
import com.dirtfy.ppp.ui.presenter.viewmodel.FlowStateBubble
import kotlinx.coroutines.flow.MutableStateFlow

class AccountRecordListBubble: FlowStateBubble<List<UiAccountRecord>> {
    override val backingProperty: MutableStateFlow<FlowState<List<UiAccountRecord>>>
    = MutableStateFlow(FlowState.loading())
    override var value: List<UiAccountRecord>
    = emptyList()
}