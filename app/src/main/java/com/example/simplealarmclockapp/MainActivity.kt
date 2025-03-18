package com.example.simplealarmclockapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplealarmclockapp.ui.theme.SimpleAlarmClockAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleAlarmClockAppTheme {
                AlarmClockScreen()
            }
        }
    }
}

@Preview
@Composable
fun AlarmClockScreen(
    alarmViewModel: AlarmViewModel = viewModel()
) {
    val context = LocalContext.current
    val selectedHour by alarmViewModel.selectedHour.collectAsState()
    val selectedMinute by alarmViewModel.selectedMinute.collectAsState()
    val selectedPeriod by alarmViewModel.selectedPeriod.collectAsState()
    val alarmMessage by alarmViewModel.alarmMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8ECF4))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.set_name), fontSize = 24.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = alarmMessage,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Normal),
            onValueChange = { alarmViewModel.setAlarmDetails(selectedHour, selectedMinute, selectedPeriod, it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            isError = alarmMessage.isBlank(), // Show error when empty
            label = { Text(text = stringResource(R.string.alarm_message_label)) },
            supportingText = {
                if (alarmMessage.isBlank()) {
                    Text(text = stringResource(R.string.alarm_message_error), color = Color.Red)
                }
            }
        )

        TimePickerSpinner(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            initialPeriod = selectedPeriod,
        ) { hour, minute, period ->
            alarmViewModel.setAlarmDetails(hour, minute, period, alarmMessage)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            TextButton(
                onClick = {
                    (context as? ComponentActivity)?.finishAffinity()
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(text = stringResource(R.string.cancel), fontSize = 16.sp, color = Color.Gray)
            }
            TextButton(
                onClick = {
                    setAlarmIntent(
                        context = context,
                        hour = selectedHour,
                        minute = selectedMinute,
                        period = selectedPeriod,
                        message = alarmMessage
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                enabled = alarmMessage.isNotBlank() // Disable if empty
            ) {
                Text(
                    text = stringResource(R.string.save),
                    fontSize = 16.sp,
                    color = if (alarmMessage.isNotBlank()) Color(0xFF6200EE) else Color.Gray // Change color when disabled
                )
            }
        }
    }
}

@Composable
fun TimePickerSpinner(
    initialHour: Int,
    initialMinute: Int,
    initialPeriod: String,
    onTimeSelected: (Int, Int, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = MaterialTheme.shapes.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        AndroidView(
            factory = { context ->
                val parent = FrameLayout(context)
                LayoutInflater.from(context).inflate(R.layout.time_picker_layout, parent, true).apply {
                    val timePicker = findViewById<TimePicker>(R.id.timePicker)
                    timePicker.hour = if (initialPeriod == "PM" && initialHour != 12) initialHour + 12 else initialHour
                    timePicker.minute = initialMinute

                    timePicker.setOnTimeChangedListener { _, newHour, newMinute ->
                        val adjustedHour = if (newHour > 12) newHour - 12 else newHour
                        val period = if (newHour >= 12) "PM" else "AM"
                        onTimeSelected(adjustedHour, newMinute, period)
                    }
                }
            },
            modifier = Modifier
                .wrapContentSize(),
        )

        Text(
            text = stringResource(R.string.alarm_set_for, initialHour, initialMinute, initialPeriod),
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )
    }
}

fun setAlarmIntent(context: Context, hour: Int, minute: Int, period: String, message: String) {

    val hour24Format = if (period == "PM") {
        if (hour != 12) hour + 12 else hour
    } else {
        if (hour == 12) 0 else hour
    }

    Log.d("AlarmDebug", "Converted - Hour24: $hour24Format, Minute: $minute")

    val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
        putExtra(AlarmClock.EXTRA_HOUR, hour24Format)
        putExtra(AlarmClock.EXTRA_MINUTES, minute)
        putExtra(AlarmClock.EXTRA_MESSAGE, message)
    }

    try {
        context.startActivity(intent)
        Toast.makeText(
            context,
            context.getString(R.string.alarm_set_for, hour, minute, message),
            Toast.LENGTH_SHORT
        ).show()
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            context.getString(R.string.no_alarm_app_available),
            Toast.LENGTH_SHORT
        ).show()
    }
}
