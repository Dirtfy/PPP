package com.dirtfy.ppp.data.source.firestore.table

import com.google.firebase.firestore.PropertyName

data class FireStoreGroupTable(
    @PropertyName("1")
    val group1: ArrayList<Int>?,
    @PropertyName("2")
    val group2: ArrayList<Int>?,
    @PropertyName("3")
    val group3: ArrayList<Int>?,
    @PropertyName("4")
    val group4: ArrayList<Int>?,
    @PropertyName("5")
    val group5: ArrayList<Int>?,
    @PropertyName("6")
    val group6: ArrayList<Int>?,
    @PropertyName("7")
    val group7: ArrayList<Int>?,
    @PropertyName("8")
    val group8: ArrayList<Int>?,
    @PropertyName("9")
    val group9: ArrayList<Int>?,
    @PropertyName("10")
    val group10: ArrayList<Int>?,
    @PropertyName("11")
    val group11: ArrayList<Int>?,
) {
    constructor(): this(
        null,null,null,null,null,
        null,null,null,null,null,null
    )
}
