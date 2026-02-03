package com.zioanacleto.speakeazy.core.data.user.repositories

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FirebaseAuthRepositoryImplTest {

    private val firebaseAuth: FirebaseAuth = mockk(relaxed = true)
    private val firebaseApp: FirebaseApp = mockk(relaxed = true)
    private val actionCodeSettings: ActionCodeSettings = mockk()

    @Before
    fun setUp() {
        // Mock all the static firebase calls
        mockkStatic(FirebaseApp::class)
        every { FirebaseApp.getInstance() } returns firebaseApp
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns firebaseAuth
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `currentUserEmail - when user is logged in - returns email`() {
        // given
        val expectedEmail = "test@example.com"
        val firebaseUser: FirebaseUser = mockk()
        every { firebaseUser.email } returns expectedEmail
        every { firebaseAuth.currentUser } returns firebaseUser

        // when
        val sut = createSut()
        val actualEmail = sut.currentUserEmail

        // then
        assertEquals(expectedEmail, actualEmail)
    }

    @Test
    fun `currentUserEmail - when user is not logged in - returns null`() {
        // given
        every { firebaseAuth.currentUser } returns null

        // when
        val sut = createSut()
        val actualEmail = sut.currentUserEmail

        // then
        assertEquals(null, actualEmail)
    }

    @Test
    fun `sendSignInLinkToEmail - when task is successful - calls onEmailSent with true`() {
        // given
        val email = "test@example.com"
        val mockTask: Task<Void> = mockk()
        val listener = slot<OnCompleteListener<Void>>()
        var result: Boolean? = null

        every { firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings) } returns mockTask
        every { mockTask.addOnCompleteListener(capture(listener)) } answers {
            every { mockTask.isSuccessful } returns true
            listener.captured.onComplete(mockTask)
            mockTask
        }

        // when
        val sut = createSut()
        sut.sendSignInLinkToEmail(email) { onEmailSentResult ->
            result = onEmailSentResult
        }

        // then
        assertTrue(result!!)
    }

    @Test
    fun `sendSignInLinkToEmail - when task fails - calls onEmailSent with false`() {
        // given
        val email = "test@example.com"
        val mockTask: Task<Void> = mockk()
        val listener = slot<OnCompleteListener<Void>>()
        var result: Boolean? = null

        every { firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings) } returns mockTask
        every { mockTask.addOnCompleteListener(capture(listener)) } answers {
            every { mockTask.isSuccessful } returns false
            listener.captured.onComplete(mockTask)
            mockTask
        }

        // when
        val sut = createSut()
        sut.sendSignInLinkToEmail(email) { onEmailSentResult ->
            result = onEmailSentResult
        }

        // then
        assertFalse(result!!)
    }

    @Test
    fun `isSignInWithEmailLink - returns value from firebaseAuth`() {
        // given
        val emailLink = "http://some.link"
        every { firebaseAuth.isSignInWithEmailLink(emailLink) } returns true

        // when
        val sut = createSut()
        val result = sut.isSignInWithEmailLink(emailLink)

        // then
        assertTrue(result)
        verify { firebaseAuth.isSignInWithEmailLink(emailLink) }
    }

    @Test
    fun `signInWithEmailLink - when task is successful - calls onSignInDone with true`() {
        // given
        val email = "test@example.com"
        val emailLink = "http://some.link"
        val mockTask: Task<AuthResult> = mockk()
        val listener = slot<OnCompleteListener<AuthResult>>()
        var result: Boolean? = null

        every { firebaseAuth.signInWithEmailLink(email, emailLink) } returns mockTask
        every { mockTask.addOnCompleteListener(capture(listener)) } answers {
            every { mockTask.isSuccessful } returns true
            listener.captured.onComplete(mockTask)
            mockTask
        }

        // when
        val sut = createSut()
        sut.signInWithEmailLink(email, emailLink) { onSignInDoneResult ->
            result = onSignInDoneResult
        }

        // then
        assertTrue(result!!)
    }

    @Test
    fun `signInWithEmailLink - when task fails - calls onSignInDone with false`() {
        // given
        val email = "test@example.com"
        val emailLink = "http://some.link"
        val mockTask: Task<AuthResult> = mockk()
        val listener = slot<OnCompleteListener<AuthResult>>()
        var result: Boolean? = null

        every { firebaseAuth.signInWithEmailLink(email, emailLink) } returns mockTask
        every { mockTask.addOnCompleteListener(capture(listener)) } answers {
            every { mockTask.isSuccessful } returns false
            listener.captured.onComplete(mockTask)
            mockTask
        }

        // when
        val sut = createSut()
        sut.signInWithEmailLink(email, emailLink) { onSignInDoneResult ->
            result = onSignInDoneResult
        }

        // then
        assertFalse(result!!)
    }

    @Test
    fun `signOut - calls firebaseAuth signOut`() {
        // when
        val sut = createSut()
        sut.signOut()

        // then
        verify { firebaseAuth.signOut() }
    }

    private fun createSut() = FirebaseAuthRepositoryImpl(actionCodeSettings)
}
