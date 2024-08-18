package com.dirtfy.ppp.viewmodel.selling.recording

import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.contract.model.selling.SalesRecordModelContract.DTO.Sales
import com.dirtfy.ppp.model.selling.recording.SalesRecordRepository
import com.dirtfy.ppp.viewmodel.ListViewModel

class SalesViewModel: ListViewModel<Sales>() {

    override val repository: Repository<Sales>
        get() = SalesRecordRepository


}