package com.example.mealmemoapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.mealmemoapp.utilities.Result
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DataSource @Inject constructor() {

    fun <RemoteType> fetchRemoteData(
        fetchRemoteData: suspend () -> Result<RemoteType>,
    ): LiveData<Result<RemoteType>> = liveData(Dispatchers.IO) {

        when (val remoteResponse = fetchRemoteData()) {
            is Result.Success -> {
                emit(Result.Success(remoteResponse.data))
            }
            is Result.Failure -> {
                emit(Result.Failure("Network ERROR, Check Connection to the Internet..."))
            }
        }
    }
}