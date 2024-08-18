package com.dirtfy.ppp.viewmodel.use.accounting

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirtfy.tagger.Tagger
import com.dirtfy.ppp.contract.model.accounting.AccountModelContract
import com.dirtfy.ppp.contract.viewmodel.accounting.AccountingViewModelContract
import com.dirtfy.ppp.contract.viewmodel.accounting.AccountingViewModelContract.DTO.Account
import com.dirtfy.ppp.model.accounting.accounting.AccountRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class AccountViewModel: ViewModel(), AccountingViewModelContract.API, Tagger {

    private val accountModel: AccountModelContract.API = AccountRepository

    private val _searchClue: MutableState<String>
    = mutableStateOf("")
    override val searchClue: State<String>
        get() = _searchClue

    override fun clueChanged(now: String) {
        _searchClue.value = now
    }

    override fun searchByClue() {
        val clue = _searchClue.value

        _accountList.value = listOf()
        val newList = mutableListOf<Account>()
        for (rawAccount in _rawAccountList) {
            if(rawAccount.accountNumber.contains(clue))
                newList.add(rawAccount.convertToViewModelAccount())
        }
        _accountList.value = newList
    }

    private val _rawAccountList: MutableList<AccountModelContract.DTO.Account>
    = mutableListOf()
    private val _accountList: MutableState<List<Account>>
    = mutableStateOf(listOf())
    override val accountList: State<List<Account>>
        get() = _accountList

    private fun timestampFormatting(timestamp: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        return "$year/$month/$day"
    }
    private fun AccountModelContract.DTO.Account.convertToViewModelAccount(): Account {
        return Account(
            number = this.accountNumber,
            name = this.accountName,
            registerDate = timestampFormatting(this.registerTimestamp)
        )
    }

    override fun checkAccountList() {
        viewModelScope.launch {
            _rawAccountList.clear()

            _rawAccountList.addAll(accountModel.read { true })

            _accountList.value = _rawAccountList.map { it.convertToViewModelAccount() }
        }
    }

    override fun buildAccountArgumentString(data: Account): String {
        val target = _rawAccountList.find {
            it.accountNumber == data.number
        }

        if (target == null) {
            Log.e(TAG, "fail to find target")
            return ""
        }

        return "account_number=${target.accountNumber}&" +
                "account_name=${target.accountName}&" +
                "phone_number=${target.phoneNumber}&" +
                "register_timestamp=${target.registerTimestamp}&" +
                "balance=${target.balance}"
    }
}