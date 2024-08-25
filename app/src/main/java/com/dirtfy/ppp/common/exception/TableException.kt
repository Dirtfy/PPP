package com.dirtfy.ppp.common.exception

sealed class TableException(
    massage: String
): CustomException(massage) {

    class InvalidTableNumber: TableException("it's invalid table number")

    class GroupLoss: TableException("group is not found")
    class NumberLoss: TableException("number is not found")

    class NonUniqueGroup: TableException("group is not unique")

    class NonEnoughMergingTargets: TableException("number of merging table is not enough")
    class NonEnoughMenuToCancel: TableException("menu count can not be minus")
    class InValidGroupState: TableException("group table has error!")
}