package com.dirtfy.ppp.contract.viewmodel.user.selling

import com.dirtfy.ppp.contract.viewmodel.user.selling.menu.managing.MenuManagingUser
import com.dirtfy.ppp.contract.viewmodel.user.selling.sales.recording.SalesRecordingUser
import com.dirtfy.ppp.contract.viewmodel.user.selling.tabling.TablingUser

interface SellingUser: TablingUser, SalesRecordingUser, MenuManagingUser {
}