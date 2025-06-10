package com.zioanacleto.speakeazy.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.zioanacleto.speakeazy.database.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM userentity WHERE email LIKE :email LIMIT 1")
    fun getUserByEmail(email: String): UserEntity

    // Although this could lead to problems, we assume that we'll save just one user
    @Query("SELECT * FROM userentity LIMIT 1")
    fun getUser(): UserEntity

    @Insert
    fun insertUser(user: UserEntity)

    @Query("DELETE FROM userentity WHERE email LIKE :email")
    fun deleteUser(email: String)

    @Update
    fun updateUser(user: UserEntity)
}