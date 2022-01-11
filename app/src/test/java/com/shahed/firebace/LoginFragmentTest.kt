package com.shahed.firebace

import androidx.core.util.PatternsCompat
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginFragmentTest() {

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            return false
        }

        if (PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        }

        return false
    }

    @Test
    fun loginWithCorrectEmailAndPassword() {
        val email = "jaad@yaoo.com"
        val password = "123456"

        assertTrue(validateInput(email, password))
    }

    @Test
    fun loginWithEmptyFields() {
        val email = " "
        val password = " "

        assertFalse(validateInput(email, password))
    }

    @Test
    fun loginWithEmptyPassword() {
        val email = "abc@yahoo.com"
        val password = " "

        assertFalse(validateInput(email, password))
    }


    @Test
    fun loginWithWrongEmail() {
        val email = "builrigo;xf"
        val password = "123456"

        assertFalse(validateInput(email, password))
    }

}