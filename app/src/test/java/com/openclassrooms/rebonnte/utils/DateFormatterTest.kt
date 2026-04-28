package com.openclassrooms.rebonnte.utils

import org.junit.Test
import org.junit.Assert.assertTrue
import java.util.regex.Pattern

class DateFormatterTest {
    @Test
    fun testDatePattern() {
        val date = DateFormatter.getCurrentFormattedDate()
        val regex = "^[A-Z][a-z]{2}\\s[0-9]{1,2}\\s[A-Z][a-z]{2}\\s[0-9]{4}\\s-\\s[0-9]{2}h[0-9]{2}$"
        assertTrue(Pattern.compile(regex).matcher(date).matches())
    }
}
