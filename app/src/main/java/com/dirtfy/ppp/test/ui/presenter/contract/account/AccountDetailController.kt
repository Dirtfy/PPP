package com.dirtfy.ppp.test.ui.presenter.contract.account

import com.dirtfy.ppp.test.ui.dto.account.UiAccountDetailState
import com.dirtfy.ppp.test.ui.presenter.contract.Controller
import com.dirtfy.ppp.ui.dto.UiAccount

interface AccountDetailController: Controller<UiAccountDetailState> {

    fun updateUserName(name: String)
    fun updateDifference(difference: String)

    fun addRecord()

}