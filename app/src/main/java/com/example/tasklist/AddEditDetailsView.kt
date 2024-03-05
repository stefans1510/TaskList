package com.example.tasklist

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taskmaster.R
import com.example.tasklist.data.Task
import com.example.tasklist.ui.theme.CustomYellow
import com.example.tasklist.ui.theme.Dark
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditDetailsView(
    id: Long,
    viewModel: TaskViewModel,
    navHostController: NavController
) {
    val context = LocalContext.current

    Log.d("AddEditDetailsView", "Received ID: $id")

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    if (id != 0L) {
        val task = viewModel.getTaskById(id).collectAsState(initial = Task(0L, "", "", ""))
        viewModel.taskTitleState = task.value.title
        viewModel.taskDescriptionState = task.value.description
        viewModel.taskStatusState = task.value.status
    } else {
        viewModel.taskTitleState = ""
        viewModel.taskDescriptionState = ""
        viewModel.taskStatusState = "Ongoing"
    }

    Scaffold(
        topBar = {
            TopBar(
                title = if (id != 0L) stringResource(id = R.string.update_task) else stringResource(
                    id = R.string.add_task
                )
            ) { navHostController.navigateUp() }
        },
        scaffoldState = scaffoldState
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Dark)) {
            CreateForm {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    TaskTextField(
                        label = "Title",
                        value = viewModel.taskTitleState,
                        onValueChanged = {
                            viewModel.onTaskTitleChanged(it)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    TaskTextField(
                        label = "Description",
                        value = viewModel.taskDescriptionState,
                        onValueChanged = {
                            viewModel.onTaskDescriptionChanged(it)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (id != 0L) {
                        StatusDropdown(
                            selectedStatus = viewModel.taskStatusState,
                            onStatusSelected = {
                                viewModel.onTaskStatusChanged(it)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (viewModel.taskTitleState.isNotEmpty()
                                && viewModel.taskDescriptionState.isNotEmpty()) {
                                if (id != 0L) {
                                    viewModel.updateTask(
                                        Task(
                                            id = id,
                                            title = viewModel.taskTitleState.trim(),
                                            description = viewModel.taskDescriptionState.trim(),
                                            status = viewModel.taskStatusState.trim()
                                        )
                                    )
                                } else {
                                    viewModel.createTask(
                                        Task(
                                            title = viewModel.taskTitleState.trim(),
                                            description = viewModel.taskDescriptionState.trim(),
                                            status = viewModel.taskStatusState.trim()
                                        )
                                    )
                                    Toast.makeText(
                                        context, "Task created successfully!", Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            scope.launch {
                                navHostController.navigateUp()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(
                            text = if (id != 0L) stringResource(id = R.string.update_task)
                            else stringResource(id = R.string.add_task),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CustomYellow
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreateForm(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(10.dp)
                .background(CustomYellow, shape = RoundedCornerShape(10))
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTextField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = {
            Text(
                text = label,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = colorResource(id = R.color.white_smoke),
            textColor = Color.Black,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black
        )
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StatusDropdown(
    selectedStatus: String,
    onStatusSelected: (String) -> Unit
) {
    val options = listOf("Ongoing", "Completed", "Cancelled")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(selectedStatus) }
    // We want to react on tap/press on TextField to show menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text("Status", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                containerColor = colorResource(id = R.color.white_smoke),
                textColor = Color.Black,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption, fontSize = 20.sp) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        onStatusSelected(selectionOption)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Preview
@Composable
fun TaskTextFieldPreview() {
    TaskTextField(label = "Label", value = "Some text", onValueChanged = {})
}