package com.dirtfy.ppp.ui.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.ppp.common.FlowState
import com.dirtfy.ppp.data.logic.account.AccountService
import com.dirtfy.ppp.data.source.firestore.AccountFireStore
import com.dirtfy.ppp.data.source.firestore.AccountRecordFireStore
import com.dirtfy.ppp.ui.presenter.controller.account.AccountController
import com.dirtfy.ppp.ui.presenter.controller.account.ControllerAccount
import com.dirtfy.ppp.ui.presenter.controller.account.ControllerAccount.Companion.convertToControllerAccount
import com.dirtfy.ppp.ui.presenter.controller.account.ControllerAccountRecord.Companion.convertToControllerAccountRecord
import com.dirtfy.ppp.ui.presenter.controller.account.ControllerNewAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

class AccountViewModel: ViewModel(), AccountController {

    private val accountService = AccountService(AccountFireStore(), AccountRecordFireStore())

    private val _accountList: MutableStateFlow<FlowState<List<ControllerAccount>>>
    = MutableStateFlow(FlowState.loading())
    private var _accountListLastValue: List<ControllerAccount>
    = emptyList()
    override val accountList: StateFlow<FlowState<List<ControllerAccount>>>
        get() = _accountList

    private val _newAccount: MutableStateFlow<ControllerNewAccount>
    = MutableStateFlow(
        ControllerNewAccount()
    )
    override val newAccount: StateFlow<ControllerNewAccount>
        get() = _newAccount

    private val _isAccountCreateMode: MutableStateFlow<Boolean>
    = MutableStateFlow(false)
    override val isAccountCreateMode: StateFlow<Boolean>
        get() = _isAccountCreateMode

    private val _searchClue: MutableStateFlow<String>
    = MutableStateFlow("")
    override val searchClue: StateFlow<String>
        get() = _searchClue

    override suspend fun updateAccountList() {
        accountService.readAllAccounts().conflate().collect {
            _accountList.value = it.passMap { data ->
                val newValue = data.map { account -> account.convertToControllerAccount() }

                _accountListLastValue = newValue
                newValue
            }
        }
    }

    override suspend fun updateNewAccount(newAccountData: ControllerNewAccount) {
        _newAccount.value = newAccountData
    }

    override suspend fun updateSearchClue(clue: String) {
        _searchClue.value = clue
        accountService.readAllAccounts()
            .conflate().collect {
                _accountList.value = it.passMap { data ->
                    val filter = data.map { account -> account.number.toString() }
                        .filter { number -> number.contains(clue) }

                    data.map { account -> account.convertToControllerAccount() }
                        .filter { account -> filter.contains(account.number) }
                }
            }
    }

    override suspend fun addAccount(newAccountData: ControllerNewAccount) {
        val (number, name, phoneNumber) = newAccountData

        accountService.createAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).conflate().collect {
            _accountList.value = it.passMap { data ->
                val newList = _accountListLastValue.toMutableList()

                newList.add(data.convertToControllerAccount())
                _accountListLastValue = newList

                newList
            }
        }
    }

    override suspend fun setRandomValidAccountNumberToNewAccount() {
        accountService.createAccountNumber()
            .conflate().collect {
                _newAccount.value = it.ignoreMap {
                    _newAccount.value.copy(number = it.toString())
                }?: ControllerNewAccount()
            }
    }

    override suspend fun setAccountCreateMode(mode: Boolean) {
        _isAccountCreateMode.value = mode
    }

    override suspend fun updateAccount(newAccountData: ControllerNewAccount) {
        val (number, name, phoneNumber) = newAccountData

        accountService.updateAccount(
            number = number.toInt(),
            name = name,
            phoneNumber = phoneNumber
        ).conflate().collect {
            _accountList.value = it.passMap { data ->
                val newList = _accountListLastValue.toMutableList()

                newList.replaceAll { account ->
                    if (account.number.toInt() == data.number)
                        account.copy(
                            number = number,
                            name = name,
                            phoneNumber = phoneNumber
                        )
                    else
                        account
                }
                _accountListLastValue = newList

                newList
            }
        }
    }

    override suspend fun addRecord(
        accountNumber: Int,
        issuedName: String,
        difference: Int
    ) {
        accountService.addAccountRecord(
            accountNumber = accountNumber,
            issuedName = issuedName,
            difference = difference
        ).conflate().collect {
            _accountList.value = it.passMap { data ->
                val newList = _accountListLastValue.toMutableList()

                newList.replaceAll { account ->
                    if (account.number.toInt() == data.number)
                        account.copy(
                            recordList = data.recordList.map { record ->
                                record.convertToControllerAccountRecord()
                            }
                        )
                    else
                        account
                }
                _accountListLastValue = newList

                newList
            }
        }
    }

    override fun request(job: suspend AccountController.() -> Unit) {
        viewModelScope.launch {
            job()
        }
    }
}