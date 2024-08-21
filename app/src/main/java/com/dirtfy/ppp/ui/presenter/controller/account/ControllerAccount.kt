package com.dirtfy.ppp.ui.presenter.controller.account

import com.dirtfy.ppp.data.logic.account.ServiceAccount
import com.dirtfy.ppp.ui.presenter.controller.Utils
import com.dirtfy.ppp.ui.presenter.controller.account.ControllerAccountRecord.Companion.convertToControllerAccountRecord

data class ControllerAccount(
    val number: String,
    val name: String,
    val phoneNumber: String,
    val balance: String,
    val registerTimestamp: String,
    val recordList: List<ControllerAccountRecord>
) {

    companion object {
        fun ServiceAccount.convertToControllerAccount(): ControllerAccount {
            return ControllerAccount(
                number = number.toString(),
                name = name,
                phoneNumber = phoneNumber,
                balance = Utils.currencyFormatting(balance),
                registerTimestamp = Utils.timestampFormatting_YMD(registerTimestamp),
                recordList = recordList.map { it.convertToControllerAccountRecord() }
            )
        }
    }

    fun convertToServiceAccount(): ServiceAccount {
        return ServiceAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber,
            balance = Utils.currencyReformatting(balance),
            registerTimestamp = Utils.timestampReformatting_YMD(registerTimestamp),
            recordList = recordList.map { it.convertToServiceAccountRecord() }
        )
    }
}
