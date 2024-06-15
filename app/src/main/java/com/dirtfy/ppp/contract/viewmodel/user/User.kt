package com.dirtfy.ppp.contract.viewmodel.user

import com.dirtfy.ppp.contract.viewmodel.user.accounting.AccountingUser
import com.dirtfy.ppp.contract.viewmodel.user.selling.SellingUser

interface User: AccountingUser, SellingUser {


}