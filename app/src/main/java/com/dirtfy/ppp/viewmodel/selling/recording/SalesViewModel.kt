package com.dirtfy.ppp.viewmodel.selling.recording

import com.dirtfy.ppp.model.Repository
import com.dirtfy.ppp.model.selling.recording.SalesData
import com.dirtfy.ppp.model.selling.recording.SalesRecordRepository
import com.dirtfy.ppp.viewmodel.ListViewModel

class SalesViewModel: ListViewModel<SalesData>() {

    override val repository: Repository<SalesData>
        get() = SalesRecordRepository


}