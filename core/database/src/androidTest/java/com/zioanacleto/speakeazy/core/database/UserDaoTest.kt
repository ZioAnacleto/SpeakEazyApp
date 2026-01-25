package com.zioanacleto.speakeazy.core.database

import com.zioanacleto.speakeazy.core.database.entities.UserEntity
import org.junit.Test

class UserDaoTest : AbstractDatabaseTest() {

    @Test
    fun test_getUserByEmail_returnsUserWhenItExists() {
        // given
        insertUsers()
        val email = "test@test.com"

        // when
        val user = userDao.getUserByEmail(email)

        // then
        assert(user?.email == email)
    }

    @Test
    fun test_getUserByEmail_returnsNullWhenUserDoesNotExist() {
        // given
        insertUsers()
        val email = "notexisting@test.com"

        // when
        val user = userDao.getUserByEmail(email)

        // then
        assert(user == null)
    }

    private fun insertUsers() {
        userDao.insertUser(
            UserEntity(
                name = "John Doe",
                email = "test@test.com"
            )
        )
    }
}