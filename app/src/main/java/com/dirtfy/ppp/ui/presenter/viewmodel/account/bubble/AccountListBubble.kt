package com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble

import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.ui.dto.UiAccount
import com.dirtfy.ppp.ui.presenter.viewmodel.FlowStateBubble
import kotlinx.coroutines.flow.MutableStateFlow

class AccountListBubble: FlowStateBubble<List<UiAccount>> {
    override val backingProperty: MutableStateFlow<FlowState<List<UiAccount>>>
    = MutableStateFlow(FlowState.loading())
    override var value: List<UiAccount>
    = emptyList()
}