package com.nguyenthe.encryption

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.nguyenthe.encryption.data.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreUtils(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )
    companion object {
        private const val USER_PREFERENCES_NAME = "user_preferences"
    }

    suspend fun saveUserInfo(userInfo: UserInfo){
        context.dataStore.edit { preference ->
            preference[UserInfo.UserInfo_NAME] = userInfo.name
            preference[UserInfo.UserInfo_AGE] = userInfo.age
            preference[UserInfo.UserInfo_Address] = userInfo.address
        }
    }

    fun getUserInfo() : Flow<UserInfo> {
        return context.dataStore.data.map { preference ->
            UserInfo(
                name = preference[UserInfo.UserInfo_NAME].orEmpty(),
                age = preference[UserInfo.UserInfo_AGE]?:0,
                address = preference[UserInfo.UserInfo_Address].orEmpty()
            )
        }
    }

}