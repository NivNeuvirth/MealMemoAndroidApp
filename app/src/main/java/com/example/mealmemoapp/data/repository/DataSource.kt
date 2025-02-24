package com.example.mealmemoapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.mealmemoapp.utilities.Result
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DataSource @Inject constructor() {

    fun <LocalType, RemoteType> retrieveAndStoreData(
        getLocalData: () -> LiveData<LocalType>,
        fetchRemoteData: suspend () -> Result<RemoteType>,
        saveToLocalDb: suspend (RemoteType) -> Unit,
        shouldFetchFromRemote: () -> Boolean
    ): LiveData<Result<LocalType>> = liveData(Dispatchers.IO) {

        val localDataStream: LiveData<Result<LocalType>> = getLocalData().map { localData ->
            if (localData != null) {
                Result.Success(localData)
            } else {
                Result.Failure("No local data available")
            }
        }

        emitSource(localDataStream)

        if (!shouldFetchFromRemote()) return@liveData

        when (val remoteResponse = fetchRemoteData()) {
            is Result.Success -> {
                saveToLocalDb(remoteResponse.data)
            }
            is Result.Failure -> {
                emit(Result.Failure(remoteResponse.message))
                emitSource(localDataStream)
            }
        }
    }
}