package com.motiflow.upnext.screens.edittodoscreen

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoScreen(
    todoId : String,
    popUpScreen : () -> Unit,
    navigateTo : (String) -> Unit,
    restartApp : (String) -> Unit,
    viewModel: EditTodoViewModel = viewModel()
) {
    val todo by viewModel.todo.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {viewModel.initialize(todoId, restartApp)}

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

    Column {
        Text("Edit Todo")

        Spacer(Modifier.height(8.dp))

        Text("Title")
        TextField(
            value = title,
            onValueChange = { title = it }
        )

        Spacer(Modifier.height(8.dp))

        Text("Description")
        TextField(
            value = description,
            onValueChange = { description = it }
        )

        Spacer(Modifier.height(8.dp))

        EnumDropdown(
            label = "Acceptance Status",
            selected = acceptance,
            values = AcceptanceStatus.entries,
            onSelected = { acceptance = it }
        )

        Spacer(Modifier.height(8.dp))

        EnumDropdown(
            label = "Todo Status",
            selected = status,
            values = TodoStatus.entries,
            onSelected = { status = it }
        )

        Spacer(Modifier.height(16.dp))

        // --- Scheduled Date & Time ---
        Text(text = "Scheduled:")
        Row {
            Text(
                text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(scheduledDateMillis)),
                modifier = Modifier
                    .clickable { showScheduledDatePicker = true }
                    .padding(8.dp)
            )
            Text(
                text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(scheduledTimeMillis)),
                modifier = Modifier
                    .clickable { showTimePicker(context, scheduledTimeMillis) { scheduledTimeMillis = it } }
                    .padding(8.dp)
            )
        }

        if (showScheduledDatePicker) {
            DatePickerDialogComposable(
                initialMillis = scheduledDateMillis,
                onDateSelected = { scheduledDateMillis = it },
                onDismiss = { showScheduledDatePicker = false }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Deadline Date & Time ---
        Text(text = "Deadline:")
        Row {
            Text(
                text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(deadlineDateMillis)),
                modifier = Modifier
                    .clickable { showDeadlineDatePicker = true }
                    .padding(8.dp)
            )
            Text(
                text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(deadlineTimeMillis)),
                modifier = Modifier
                    .clickable { showTimePicker(context, deadlineTimeMillis) { deadlineTimeMillis = it } }
                    .padding(8.dp)
            )
        }

        if (showDeadlineDatePicker) {
            DatePickerDialogComposable(
                initialMillis = deadlineDateMillis,
                onDateSelected = { deadlineDateMillis = it },
                onDismiss = { showDeadlineDatePicker = false }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Created By: ${todo.createdByName}")
        Text("Last Updated: ${todo.updateAt.toDate()}")


        Spacer(Modifier.height(16.dp))

        // ---- Read-only fields ----
        Text("Created By")
        Text(todo.createdByName)

        Spacer(Modifier.height(8.dp))

        Text("Last Updated At")
        Text(todo.updateAt.toDate().toString())

        Spacer(Modifier.height(16.dp))

        Row {
            Button(onClick = {
                // Update the state first
                viewModel.todo.value = viewModel.todo.value.copy(
                    title = title,
                    description = description,
                    acceptance = acceptance,
                    status = status,
                    scheduledAt = combineDateAndTime(scheduledDateMillis, scheduledTimeMillis),
                    deadlineAt = combineDateAndTime(deadlineDateMillis, deadlineTimeMillis),
                    updateAt = Timestamp.now()
                )
                viewModel.saveTodo(popUpScreen)
            }){
                Text("Save")
            }

            Spacer(Modifier.width(8.dp))

            Button(onClick = {viewModel.onCancelEdit(popUpScreen)}) {
                Text("Cancel")
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
        Text(label)

        Box {
            Text(
                text = selected.name,
                modifier = Modifier
                    .clickable { expanded = true }
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
