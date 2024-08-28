package com.dirtfy.ppp.ui.presenter.viewmodel.account

import com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble.AccountListBubble
import com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble.AccountRecordListBubble
import com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble.ModeBubble
import com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble.NewAccountBubble
import com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble.NewAccountRecordBubble
import com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble.NowAccountBubble
import com.dirtfy.ppp.ui.presenter.viewmodel.account.bubble.SearchClueBubble

class Bubbles {
    val accountList = AccountListBubble()
    val accountRecordList = AccountRecordListBubble()

    val newAccount = NewAccountBubble()
    val searchClue = SearchClueBubble()

    val nowAccount = NowAccountBubble()
    val newAccountRecord = NewAccountRecordBubble()

    val mode = ModeBubble()
}