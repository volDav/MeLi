package com.drac.challenge.presentation.utils

import org.junit.Assert.*
import org.junit.Test

class ToPriceKtTest {

    @Test
    fun `Evaluate price less than 0`() {

        val ss = toPrice(-100)

        assertEquals("$ 0", ss)
    }

    @Test
    fun `Evaluate price equals to 0`() {
        val ss = toPrice(0)

        assertEquals("$ 0", ss)
    }

    @Test
    fun `Evaluate price more than 0`() {
        val ss = toPrice(420)

        assertEquals("$ 420", ss)
    }

    @Test
    fun `Evaluate price more than 1000`() {
        val ss = toPrice(2500)

        assertEquals("$ 2.500", ss)
    }

    @Test
    fun `Evaluate price more than 1000000`() {
        val ss = toPrice(6890500)

        assertEquals("$ 6.890.500", ss)
    }

}