package com.dirtfy.ppp.contract.viewmodel.accounting.managing

import androidx.compose.runtime.State

object AccountManagingViewModelContract {

    object DTO {

        data class Account(
            val number: String,
            val name: String,
            val phoneNumber: String,
            val registerTimestamp: String,
            val balance: String
        )

        data class Record(
            val timestamp: String,
            val userName: String,
            val amount: String,
            val result: String
        )

    }

    object AccountDetail {
        interface API {
            val accountDetail: State<DTO.Account>

            fun setAccountDetail(account: DTO.Account)
        }
    }

    object RecordList {
        interface API {
            val recordList: State<List<DTO.Record>>

            fun checkAccountRecordList()

            fun addRecord()
        }
    }

    object NewRecord {
        interface API {
            val newRecord: State<DTO.Record>

            fun setNewRecord(now: DTO.Record)

            fun addRecord()
        }
    }

    interface API: AccountDetail.API, RecordList.API, NewRecord.API

}