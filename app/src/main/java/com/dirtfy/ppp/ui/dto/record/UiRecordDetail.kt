package com.dirtfy.ppp.ui.dto.record

import com.dirtfy.ppp.data.dto.feature.record.DataRecordDetail
import com.dirtfy.ppp.ui.presenter.controller.common.Utils

data class UiRecordDetail(
    val name: String,
    val price: String,
    val count: String
) {

    companion object {
        fun DataRecordDetail.convertToUiRecordDetail(): UiRecordDetail {
            return UiRecordDetail(
                name = name,
                price = Utils.formatCurrency(amount),
                count = count.toString()
            )
        }
    }
}
