package com.dirtfy.ppp.ui.controller.common

import kotlinx.coroutines.flow.StateFlow

interface Controller<ScreenData, Scope: Controller<ScreenData, Scope>> {

    val screenData: StateFlow<ScreenData>

    fun request(job: suspend Scope.() -> Unit)

}