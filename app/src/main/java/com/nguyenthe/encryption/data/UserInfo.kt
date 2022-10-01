package com.nguyenthe.encryption.data

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.Serializable

data class UserInfo(
    val name: String = "VanThe",
    val age : Int = 29,
    val address: String = "Ho Chi Minh",
) : Serializable{
    companion object{
        val UserInfo_NAME = stringPreferencesKey("user_first_name")
        val UserInfo_AGE = intPreferencesKey("UserInfo_AGE")
        val UserInfo_Address = stringPreferencesKey("UserInfo_Address")
    }
}
