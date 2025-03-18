package com.example.simplealarmclockapp.data

object TimeData {
    val hours = (1..12).toList()  // 1 to 12 hours (AM/PM)
    val minutes = (0..59).toList() // 0 to 59 minutes
    val periods = listOf("AM", "PM") // AM / PM selection
}
