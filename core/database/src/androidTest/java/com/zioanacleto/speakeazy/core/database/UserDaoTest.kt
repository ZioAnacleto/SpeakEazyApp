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

    @Test
    fun test_getUser_returnsUserWhenItExists() {
        // given
        insertUsers()

        // when
        val user = userDao.getUser()

        // then
        assert(user.name == "John Doe")
    }

    @Test
    fun test_deleteUser_userIsDeleted() {
        // given
        insertUsers()
        val email = "test@test.com"

        // when
        userDao.deleteUser(email)
        val user = userDao.getUserByEmail(email)

        // then
        assert(user == null)
    }

    @Test
    fun test_updateUser_userIsUpdated() {
        // given
        insertUsers()
        val email = "test@test.com"
        val newName = "Jane Doe"

        // when
        var user = userDao.getUserByEmail(email)
        user = user?.copy(name = newName)

        userDao.updateUser(user!!)
        val newUser = userDao.getUserByEmail(email)

        // then
        assert(user?.name == newUser?.name)
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