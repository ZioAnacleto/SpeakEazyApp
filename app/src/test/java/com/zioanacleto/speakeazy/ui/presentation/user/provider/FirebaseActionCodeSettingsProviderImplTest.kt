package com.zioanacleto.speakeazy.ui.presentation.user.provider

import com.zioanacleto.speakeazy.APP_PACKAGE
import com.zioanacleto.speakeazy.USER_DEEPLINK_URI
import com.zioanacleto.speakeazy.assertAllTrue
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Test

class FirebaseActionCodeSettingsProviderImplTest {

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `provideActionCodeSettings - should return correct data`() {
        // given

        // when
        val sut = createSut()
        val response = sut.provideActionCodeSettings()

        // then
        assertAllTrue(
            response.canHandleCodeInApp(),
            response.url == USER_DEEPLINK_URI,
            response.androidPackageName == APP_PACKAGE
        )
    }

    private fun createSut() = FirebaseActionCodeSettingsProviderImpl()
}