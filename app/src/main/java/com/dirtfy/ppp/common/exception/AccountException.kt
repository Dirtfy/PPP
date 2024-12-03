package com.dirtfy.ppp.common.exception

sealed class AccountException(
    massage: String
): CustomException(massage) {
    class NonUniqueNumber: AccountException("number is not unique")
    class InvalidNumber: AccountException("it's not a valid number")
    class InvalidBalance: AccountException("it's invalid balance")
    class InvalidPhoneNumber: AccountException("it's invalid phone number")

    class RecordIssuedNameLoss: AccountException("record issued name is not found")
    class RecordAmountLoss: AccountException("record amount is not found")
    class RecordResultLoss: AccountException("record result is not found")
    class RecordTimestampLoss: AccountException("record timestamp is not found")

    class NumberLoss: AccountException("number is not found")
    class NameLoss: AccountException("name is not found")
    class PhoneNumberLoss: AccountException("phone number is not found")
    class BalanceLoss: AccountException("balance is not found")
    class RegisterTimestampLoss: AccountException("register timestamp is not found")

    class BlankNumber: MenuException("account number can not be a blank")
    class BlankName: MenuException("account name can not be a blank")
    class BlankPhoneNumber: MenuException("account phone number can not be a blank")
}