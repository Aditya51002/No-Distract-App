package com.example.focusapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedAppDao {
    @Query("SELECT * FROM blocked_apps ORDER BY appName ASC")
    fun observeAll(): Flow<List<BlockedAppEntity>>

    @Query("SELECT * FROM blocked_apps")
    suspend fun getAll(): List<BlockedAppEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<BlockedAppEntity>)

    @Query("UPDATE blocked_apps SET isSelected = :isSelected WHERE packageName = :packageName")
    suspend fun setSelected(packageName: String, isSelected: Boolean)

    @Query("SELECT EXISTS(SELECT 1 FROM blocked_apps WHERE packageName = :packageName AND isSelected = 1)")
    suspend fun isSelected(packageName: String): Boolean

    @Query("SELECT packageName FROM blocked_apps WHERE isSelected = 1")
    suspend fun getSelectedPackages(): List<String>

    @Query("UPDATE blocked_apps SET remoteId = :remoteId WHERE packageName = :packageName")
    suspend fun updateRemoteId(packageName: String, remoteId: String?)

    @Query("DELETE FROM blocked_apps")
    suspend fun clear()
}

