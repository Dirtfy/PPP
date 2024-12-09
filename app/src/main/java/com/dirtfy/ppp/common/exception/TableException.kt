package com.dirtfy.ppp.common.exception

sealed class TableException(
    massage: String
): CustomException(massage) {

    class TableLockPreempted: TableException("simultaneous group edition is not allowed")
    class IllegalMergeModeSet: TableException("setMode(Merge) is not allowed. use trySetMergeMode()")

    class InvalidTableNumber: TableException("it's invalid table number")
    class InvalidPay: TableException("it's invalid pay")

    class GroupLoss: TableException("group is not found")
    class TableLoss: TableException("table is not found")
    class NameLoss: TableException("name is not found")
    class PriceLoss: TableException("price is not found")
    class CountLoss: TableException("count is not found")
    class MemberLoss: TableException("member is not found")

    class InvalidOrderName: TableException("it's invalid order name")

    class NonUniqueGroup: TableException("group is not unique")
    class NonUniqueOrderName: TableException("order name is not unique")

    class NonEnoughMergingTargets: TableException("number of merging table is not enough")
    class NonEnoughMenuToCancel: TableException("menu count can not be minus")
    class IllegalGroupIdAssignment: RecordException("id is already assigned (illegal)")
    class InValidGroupState: TableException("group table has error!")
}