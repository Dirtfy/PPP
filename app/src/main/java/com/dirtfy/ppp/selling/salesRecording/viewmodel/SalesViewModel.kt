package com.dirtfy.ppp.selling.salesRecording.viewmodel

import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.common.viewmodel.ListViewModel
import com.dirtfy.ppp.selling.salesRecording.model.SalesData
import com.dirtfy.ppp.selling.salesRecording.model.SalesRecordRepository

class SalesViewModel: ListViewModel<SalesData>() {

    override val repository: Repository<SalesData>
        get() = SalesRecordRepository


}