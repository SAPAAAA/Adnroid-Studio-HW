package com.example.simplealarmclockapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

class AlarmViewModel : ViewModel() {

    private val calendar = Calendar.getInstance()

    private val _selectedHour = MutableStateFlow(calendar[Calendar.HOUR])
    val selectedHour: StateFlow<Int> = _selectedHour.asStateFlow()

    private val _selectedMinute = MutableStateFlow(calendar[Calendar.MINUTE])
    val selectedMinute: StateFlow<Int> = _selectedMinute.asStateFlow()

    private val _selectedPeriod = MutableStateFlow(if (calendar[Calendar.AM_PM] == Calendar.AM) "AM" else "PM")
    val selectedPeriod: StateFlow<String> = _selectedPeriod.asStateFlow()

    private val _alarmMessage = MutableStateFlow("Default Alarm")
    val alarmMessage: StateFlow<String> = _alarmMessage.asStateFlow()

    fun setAlarmDetails(hour: Int, minute: Int, period: String, alarmMessage: String) {
        _selectedHour.value = hour
        _selectedMinute.value = minute
        _selectedPeriod.value = period
        _alarmMessage.value = alarmMessage
    }

    fun reset() {
        calendar.timeInMillis = System.currentTimeMillis()
        _selectedHour.value = calendar[Calendar.HOUR]
        _selectedMinute.value = calendar[Calendar.MINUTE]
        _selectedPeriod.value = if (calendar[Calendar.AM_PM] == Calendar.AM) "AM" else "PM"
        _alarmMessage.value = "Default Alarm"
    }
}
