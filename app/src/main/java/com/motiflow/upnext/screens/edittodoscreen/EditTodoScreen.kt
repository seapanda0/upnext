package com.motiflow.upnext.screens.edittodoscreen

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Timestamp
import com.motiflow.upnext.AcceptanceStatus
import com.motiflow.upnext.TodoStatus
import java.util.Date
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoScreen(
    assignedToId : String,
    todoId : String,
    popUpScreen : () -> Unit,
    navigateTo : (String) -> Unit,
    restartApp : (String) -> Unit,
    viewModel: EditTodoViewModel = viewModel()
) {
    val todo by viewModel.todo.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {viewModel.initialize(todoId, assignedToId, restartApp)}

    // Key state to todo.id
    var title by remember(todo.id) { mutableStateOf(todo.title.orEmpty()) }
    var description by remember(todo.id) { mutableStateOf(todo.description.orEmpty()) }

    var acceptance by remember(todo.id) { mutableStateOf(todo.acceptance) }
    var status by remember(todo.id) { mutableStateOf(todo.status) }

    // --- Scheduled ---
    var scheduledDateMillis by remember(todo.id) { mutableStateOf(todo.scheduledAt?.toDate()?.time ?: System.currentTimeMillis()) }
    var scheduledTimeMillis by remember(todo.id) { mutableStateOf(todo.scheduledAt?.toDate()?.time ?: System.currentTimeMillis()) }
    var showScheduledDatePicker by remember { mutableStateOf(false) }

    // --- Deadline ---
    var deadlineDateMillis by remember(todo.id) { mutableStateOf(todo.deadlineAt?.toDate()?.time ?: System.currentTimeMillis()) }
    var deadlineTimeMillis by remember(todo.id) { mutableStateOf(todo.deadlineAt?.toDate()?.time ?: System.currentTimeMillis()) }
    var showDeadlineDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text ="Edit To-do",
                style = MaterialTheme.typography.headlineSmall
            )
            // TITLE FIELD
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // DESCRIPTION FIELD
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Acceptance status
            EnumDropdown(
                label = "Acceptance Status",
                selected = acceptance,
                values = AcceptanceStatus.entries,
                onSelected = { acceptance = it }
            )

            // Todo Status
            EnumDropdown(
                label = "Todo Status",
                selected = status,
                values = TodoStatus.entries,
                onSelected = { status = it }
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // SCHEDULED TIME AND DEADLINE TIME START
            // ---- Scheduled ----
            DateTimeRow(
                label = "Scheduled",
                dateMillis = scheduledDateMillis,
                timeMillis = scheduledTimeMillis,
                onDateClick = { showScheduledDatePicker = true },
                onTimeClick = {
                    showTimePicker(context, scheduledTimeMillis) {
                        scheduledTimeMillis = it
                    }
                }
            )

            if (showScheduledDatePicker) {
                DatePickerDialogComposable(
                    initialMillis = scheduledDateMillis,
                    onDateSelected = { scheduledDateMillis = it },
                    onDismiss = { showScheduledDatePicker = false }
                )
            }

            // ---- Deadline ----
            DateTimeRow(
                label = "Deadline",
                dateMillis = deadlineDateMillis,
                timeMillis = deadlineTimeMillis,
                onDateClick = { showDeadlineDatePicker = true },
                onTimeClick = {
                    showTimePicker(context, deadlineTimeMillis) {
                        deadlineTimeMillis = it
                    }
                }
            )

            if (showDeadlineDatePicker) {
                DatePickerDialogComposable(
                    initialMillis = deadlineDateMillis,
                    onDateSelected = { deadlineDateMillis = it },
                    onDismiss = { showDeadlineDatePicker = false }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // SCHEDULED TIME AND DEADLINE TIME END

            // ---- Read-only fields ----
            Text(
                text = "Created by ${todo.createdByName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Last updated ${todo.updateAt.toDate()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onCancelEdit(popUpScreen) }
                ) {
                    Text("Cancel")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.todo.value = viewModel.todo.value.copy(
                            title = title,
                            description = description,
                            acceptance = acceptance,
                            status = status,
                            scheduledAt = combineDateAndTime(
                                scheduledDateMillis,
                                scheduledTimeMillis
                            ),
                            deadlineAt = combineDateAndTime(
                                deadlineDateMillis,
                                deadlineTimeMillis
                            ),
                            updateAt = Timestamp.now()
                        )
                        viewModel.saveTodo(popUpScreen)
                    }
                ) {
                    Text("Save")
                }
            }
        }

    }
}

// Merge time and date information after spilitting them
fun combineDateAndTime(dateMillis: Long, timeMillis: Long): Timestamp {
    val calendarDate = Calendar.getInstance().apply { timeInMillis = dateMillis }
    val calendarTime = Calendar.getInstance().apply { timeInMillis = timeMillis }

    calendarDate.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY))
    calendarDate.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE))
    calendarDate.set(Calendar.SECOND, 0)
    calendarDate.set(Calendar.MILLISECOND, 0)

    return Timestamp(Date(calendarDate.timeInMillis))
}

fun showTimePicker(context: Context, initialMillis: Long, onTimeSelected: (Long) -> Unit) {
    val calendar = Calendar.getInstance().apply { timeInMillis = initialMillis }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(context, { _, h, m ->
        calendar.set(Calendar.HOUR_OF_DAY, h)
        calendar.set(Calendar.MINUTE, m)
        onTimeSelected(calendar.timeInMillis)
    }, hour, minute, true).show()
}

// Reuseable enum dropdown menu
@Composable
fun <T : Enum<T>> EnumDropdown(
    label: String,
    selected: T,
    values: List<T>,
    onSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            Text(
                text = selected.name,
                modifier = Modifier.padding(16.dp)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                values.forEach { value ->
                    DropdownMenuItem(
                        text = { Text(value.name) },
                        onClick = {
                            onSelected(value)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogComposable(
    initialMillis: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberDatePickerState(initialSelectedDateMillis = initialMillis)
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                state.selectedDateMillis?.let(onDateSelected)
                onDismiss()
            }) { Text("OK") }
        }
    ) {
        DatePicker(state)
    }
}

@Composable
fun DateTimeRow(
    label: String,
    dateMillis: Long,
    timeMillis: Long,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = onDateClick,
                label = {
                    Text(
                        SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).format(Date(dateMillis))
                    )
                }
            )

            AssistChip(
                onClick = onTimeClick,
                label = {
                    Text(
                        SimpleDateFormat(
                            "HH:mm",
                            Locale.getDefault()
                        ).format(Date(timeMillis))
                    )
                }
            )
        }
    }
}
