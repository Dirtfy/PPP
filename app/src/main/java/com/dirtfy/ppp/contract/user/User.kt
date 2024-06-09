package com.dirtfy.ppp.contract.user

import com.dirtfy.ppp.contract.user.accounting.AccountingUser
import com.dirtfy.ppp.contract.user.selling.SellingUser

interface User: AccountingUser, SellingUser {


}