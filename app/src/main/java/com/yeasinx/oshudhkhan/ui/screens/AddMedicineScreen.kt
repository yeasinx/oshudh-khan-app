package com.yeasinx.oshudhkhan.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.yeasinx.oshudhkhan.data.FrequencyType
import com.yeasinx.oshudhkhan.data.Medicine
import com.yeasinx.oshudhkhan.ui.medicine.MedicineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineScreen(
    viewModel: MedicineViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var frequencyType by remember { mutableStateOf(FrequencyType.DAILY) }
    var timesPerDay by remember { mutableIntStateOf(1) }
    var intervalDays by remember { mutableIntStateOf(2) }
    val reminderTimes = remember { mutableStateListOf("08:00") }
    var showTimePickerForIndex by remember { mutableStateOf<Int?>(null) }

    val isFormValid = name.isNotBlank() && dosage.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Medicine", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── Medicine Info ─────────────────────────────────────────────────
            SectionCard(title = "Medicine Info") {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Medicine Name") },
                    placeholder = { Text("e.g. Amoxicillin") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Dosage") },
                    placeholder = { Text("e.g. 1 tablet, 5ml") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ── Frequency ─────────────────────────────────────────────────────
            SectionCard(title = "Frequency") {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = frequencyType == FrequencyType.DAILY,
                        onClick = {
                            frequencyType = FrequencyType.DAILY
                            while (reminderTimes.size > timesPerDay) reminderTimes.removeAt(reminderTimes.lastIndex)
                            while (reminderTimes.size < timesPerDay) reminderTimes.add("08:00")
                        },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        label = { Text("Daily") }
                    )
                    SegmentedButton(
                        selected = frequencyType == FrequencyType.EVERY_N_DAYS,
                        onClick = {
                            frequencyType = FrequencyType.EVERY_N_DAYS
                            while (reminderTimes.size > 1) reminderTimes.removeAt(reminderTimes.lastIndex)
                            if (reminderTimes.isEmpty()) reminderTimes.add("08:00")
                        },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        label = { Text("Every N Days") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (frequencyType == FrequencyType.DAILY) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Times per day", style = MaterialTheme.typography.bodyMedium)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    if (timesPerDay > 1) {
                                        timesPerDay--
                                        if (reminderTimes.size > timesPerDay)
                                            reminderTimes.removeAt(reminderTimes.lastIndex)
                                    }
                                },
                                enabled = timesPerDay > 1
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text(
                                text = "$timesPerDay",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.widthIn(min = 32.dp)
                            )
                            IconButton(
                                onClick = {
                                    if (timesPerDay < 4) {
                                        timesPerDay++
                                        reminderTimes.add("12:00")
                                    }
                                },
                                enabled = timesPerDay < 4
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Every how many days?", style = MaterialTheme.typography.bodyMedium)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (intervalDays > 2) intervalDays-- },
                                enabled = intervalDays > 2
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text(
                                text = "$intervalDays",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.widthIn(min = 32.dp)
                            )
                            IconButton(
                                onClick = { if (intervalDays < 14) intervalDays++ }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }
                    }
                }
            }

            // ── Reminder Times ────────────────────────────────────────────────
            SectionCard(title = "Reminder Times") {
                reminderTimes.forEachIndexed { index, time ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Reminder ${index + 1}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedButton(onClick = { showTimePickerForIndex = index }) {
                            Text(formatTimeTo12h(time))
                        }
                    }
                    if (index < reminderTimes.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // ── Save Button ───────────────────────────────────────────────────
            Button(
                onClick = {
                    if (isFormValid) {
                        val medicine = Medicine(
                            name = name.trim(),
                            dosage = dosage.trim(),
                            frequencyType = frequencyType,
                            timesPerDay = if (frequencyType == FrequencyType.DAILY) timesPerDay else 1,
                            intervalDays = if (frequencyType == FrequencyType.EVERY_N_DAYS) intervalDays else 1,
                            reminderTimes = reminderTimes.joinToString(",")
                        )
                        viewModel.addMedicine(medicine)
                        onNavigateBack()
                    }
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Save Medicine", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Time Picker Dialog
    showTimePickerForIndex?.let { index ->
        val currentTime = reminderTimes.getOrElse(index) { "08:00" }
        val parts = currentTime.split(":")
        val initialHour = parts.getOrNull(0)?.toIntOrNull() ?: 8
        val initialMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0

        val timePickerState = rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = false
        )

        AlertDialog(
            onDismissRequest = { showTimePickerForIndex = null },
            title = { Text("Select Time") },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TimePicker(state = timePickerState)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val h = "%02d".format(timePickerState.hour)
                    val m = "%02d".format(timePickerState.minute)
                    reminderTimes[index] = "$h:$m"
                    showTimePickerForIndex = null
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePickerForIndex = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}



@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}

private fun formatTimeTo12h(time: String): String {
    return try {
        val parts = time.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()
        val amPm = if (hour < 12) "AM" else "PM"
        val h = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        "%d:%02d %s".format(h, minute, amPm)
    } catch (e: Exception) {
        time
    }
}