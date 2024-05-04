package com.dirtfy.ppp.accounting.accounting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.accounting.accounting.model.AccountData
import com.dirtfy.ppp.accounting.accounting.model.AccountRepository
import com.dirtfy.ppp.common.Repository
import com.dirtfy.ppp.common.viewmodel.ListViewModel
import com.dirtfy.ppp.test.DClass
import com.dirtfy.ppp.test.DRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class AccountListViewModel : ListViewModel<AccountData>() {

    override val repository: Repository<AccountData>
        get() = AccountRepository

}