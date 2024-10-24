package com.dirtfy.ppp.common.exception

sealed class MenuException(
    massage: String
): CustomException(massage) {
    class NonUniqueName: MenuException("name is not unique")

    class BlankName: MenuException("menu name can not be a blank")
    class BlankPrice: MenuException("menu price can not be a blank")

    class NameLoss: MenuException("name not found")
    class PriceLoss: MenuException("price not found")
}