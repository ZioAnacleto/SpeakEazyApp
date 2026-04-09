package com.zioanacleto.speakeazy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zioanacleto.speakeazy.core.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM userentity WHERE email LIKE :email LIMIT 1")
    fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM userentity LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    @Query("DELETE FROM userentity WHERE email LIKE :email")
    fun deleteUser(email: String)

    @Update
    fun updateUser(user: UserEntity)
}