package com.dirtfy.ppp.common.exception

sealed class RecordException(
    massage: String
): CustomException(massage) {

    class IdLoss: RecordException("id is not found")
    class IllegalIdAssignment: RecordException("id is already assigned (illegal)")
    class IssuedNameLoss: RecordException("issued name is not found")
    class IncomeLoss: RecordException("income is not found")
    class TypeLoss: RecordException("type is not found")
    class DifferenceLoss: RecordException("difference is not found")
    class TimestampLoss: RecordException("time stamp is not found")

    class DetailNameLoss: RecordException("detail name is not found")
    class DetailAmountLoss: RecordException("detail price is not found")
    class DetailCountLoss: RecordException("detail count is not found")

    class NonUniqueQuery: RecordException("query result is not unique")
    class NonExistQuery: RecordException("query result is not exist")
}