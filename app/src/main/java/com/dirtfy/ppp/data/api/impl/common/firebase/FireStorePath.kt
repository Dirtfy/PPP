package com.dirtfy.ppp.data.api.impl.common.firebase

object FireStorePath {

//    private const val CONSTANT: String = "constant"
//
//    const val MAX_ACCOUNT_NUMBER: String = "$CONSTANT/MAX_ACCOUNT_NUMBER"
//    const val RECORD_ID_COUNT: String = "$CONSTANT/RECORD_ID_COUNT"
//    const val GROUP_ID_COUNT: String = "$CONSTANT/GROUP_ID_COUNT"
//
//    const val ACCOUNT: String = "account"
//
//    const val MENU: String = "menu"
//
//    const val RECORD: String = "record"
//    const val RECORD_DETAIL: String = "detail"
//
//    const val TABLE: String = "table"
//    const val TABLE_ORDER: String = "order"
//
//    private const val LOCK: String = "lock"
//
//    const val TABLE_GROUP_LOCK = "$LOCK/TABLE_GROUP_LOCK"

    object Service {
        private const val CONSTANT: String = "constant"

        const val MAX_ACCOUNT_NUMBER: String = "$CONSTANT/MAX_ACCOUNT_NUMBER"
        const val RECORD_ID_COUNT: String = "$CONSTANT/RECORD_ID_COUNT"
        const val GROUP_ID_COUNT: String = "$CONSTANT/GROUP_ID_COUNT"

        private const val LOCK: String = "lock"

        const val TABLE_GROUP_LOCK = "$LOCK/TABLE_GROUP_LOCK"

        const val ACCOUNT: String = "account"

        const val MENU: String = "menu"

        const val RECORD: String = "record"
        const val RECORD_DETAIL: String = "detail"

        const val TABLE: String = "table"
        const val TABLE_ORDER: String = "order"
    }

    object Test {
        private const val CONSTANT: String = "constant_exp"

        const val MAX_ACCOUNT_NUMBER: String = "$CONSTANT/MAX_ACCOUNT_NUMBER"
        const val RECORD_ID_COUNT: String = "$CONSTANT/RECORD_ID_COUNT"
        const val GROUP_ID_COUNT: String = "$CONSTANT/GROUP_ID_COUNT"

        private const val LOCK: String = "lock_exp"

        const val TABLE_GROUP_LOCK = "$LOCK/TABLE_GROUP_LOCK"

        const val ACCOUNT: String = "account_exp"

        const val MENU: String = "menu_exp"

        const val RECORD: String = "record_exp"
        const val RECORD_DETAIL: String = "detail"

        const val TABLE: String = "table_exp"
        const val TABLE_ORDER: String = "order"
    }

}