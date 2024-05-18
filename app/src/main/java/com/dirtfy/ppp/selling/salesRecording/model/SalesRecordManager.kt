package com.dirtfy.ppp.selling.salesRecording.model

import com.dirtfy.ppp.common.RepositoryPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object SalesRecordManager {

    private const val TAG = "SalesRecordManager"

    private val repositoryRef =
        Firebase.firestore.collection(RepositoryPath.SALES_RECORD)


}