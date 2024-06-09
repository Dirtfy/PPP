package com.dirtfy.ppp.contract.user.selling

import com.dirtfy.ppp.contract.user.selling.menu.managing.MenuManagingUser
import com.dirtfy.ppp.contract.user.selling.sales.recording.SalesRecordingUser
import com.dirtfy.ppp.contract.user.selling.tabling.TablingUser

interface SellingUser: TablingUser, SalesRecordingUser, MenuManagingUser {
}