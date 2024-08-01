package com.example.jetpackpractice

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.example.jetpackpractice.ui.theme.CustomYellow
import kotlinx.coroutines.flow.forEach
import java.time.LocalDateTime
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddToDoCard(modifier: Modifier = Modifier,onClickDone: () -> Unit) {
    val viewModel: ToDoViewModel = viewModel()
    val completedViewModel:CompletedTodoViewModel =viewModel()
    var task by rememberSaveable {
        mutableStateOf("")
    }
    var status by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedDateTime:(java.time.LocalDateTime)? by rememberSaveable {
        mutableStateOf(null)
    }
    var showDateTimeDialog by remember {
        mutableStateOf(false)
    }
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = modifier
                .background(color = Color.White)
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .background(color = Color.White)
                    .padding(bottom = 10.dp)
            ) {
                Checkbox(
                    checked = status,
                    onCheckedChange = {
                        status = !status
                        Log.d("called","$status")
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = CustomYellow,
                    )
                )
                TextField(
                    value = task,
                    onValueChange = { task = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = CustomYellow,
                        focusedPlaceholderColor = Color.LightGray,
                        unfocusedPlaceholderColor = Color.LightGray,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    placeholder = {
                        Text(
                            text = "Add your task",
                        )
                    },
                    modifier = modifier.fillMaxWidth()
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth()

            ) {
                Button(
                    onClick = {
                        showDateTimeDialog=true
                              },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomYellow
                    ),
                    contentPadding = ButtonDefaults.ContentPadding
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if (selectedDateTime!=null) "$selectedDateTime"
                                    else "Set Reminder"
                        )
                        Icon(
                            imageVector = Icons.Rounded.Notifications,
                            contentDescription = "",
                            modifier = modifier
                                .padding(start = 1.dp)
                                .scale(.6f)

                        )
                    }
                }

                Text(text = "Done",
                    color = CustomYellow,
                    fontWeight = FontWeight.Bold,
                    modifier=modifier.clickable{
//                        val todoItem= ToDo(status,task,selectedDateTime)
//                        val copyTodos = mutableListOf<ToDo>()
//                        val copyCompletedTodos = mutableListOf<ToDo>()
//                        if (MyToDo.todos.value!= null) {
//                            MyToDo.todos.value!!.forEach{
//                                copyTodos.add(it)
//                            }
//                        }
//                        if (MyToDo.completedTodos.value!= null) {
//                            MyToDo.completedTodos.value!!.forEach{
//                                copyCompletedTodos.add(it)
//                            }
//                        }
//                        copyTodos.add(todoItem)
//                        MyToDo.todos.value = copyTodos
//                        if(todoItem.isChecked){
//                            copyCompletedTodos.add(todoItem)
//                            MyToDo.completedTodos.value = copyCompletedTodos
//                        }

                        if(!status){
                            var todo = ToDo().apply {
                                isChecked=status
                                this.task=task
                                dueDate=selectedDateTime
                            }
                            viewModel.addTodo(todo)
                        }
                        else{
                            var completedTodo = CompletedTodo().apply {
                                isChecked=status
                                this.task=task
                                dueDate=selectedDateTime
                            }
                            completedViewModel.addCompletedTodo(completedTodo)
                        }

                        onClickDone()

                    }
                )

            }
        }
    }
    if (showDateTimeDialog){
        Dialog(onDismissRequest = { showDateTimeDialog=false }) {
            DatePickerPopUp(modifier=Modifier, onClickCancel = { showDateTimeDialog=false },
                onClickDone={selectedDate ->
                    selectedDateTime=selectedDate
                    showDateTimeDialog=false
            })
        }
    }
}
@Composable
fun CompletedTodoCard(modifier: Modifier = Modifier,completedTodo: CompletedTodo){
    val completedTodoViewModel = viewModel<CompletedTodoViewModel>()
        Surface(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Checkbox(
                    checked = completedTodo.isChecked, onCheckedChange = {},
                    modifier = modifier.scale(.8f),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.LightGray
                    )
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                    ) {
                    completedTodo.task?.let {
                        Text(
                            text = it,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(textDecoration = TextDecoration.LineThrough)
                        )
                    }
                    if(completedTodo.dueDate!=null){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "${completedTodo.dueDate}", color = Color.LightGray)
                            Icon(
                                painter = painterResource(id = R.drawable.reminder),
                                contentDescription = "",
                                tint = Color.LightGray,
                                modifier = modifier.scale(.6f),
                            )
                        }
                    }
                }
                Icon(imageVector = Icons.Rounded.Delete,
                    contentDescription ="",
                    tint = Color.Gray,
                    modifier = modifier.clickable {
                        completedTodoViewModel.removeCompletedTodo(completedTodo)
                        }
                    )
            }
        }

}

@Composable
fun ToDoCard(modifier: Modifier = Modifier, todo: ToDo) {
    val viewModel: ToDoViewModel = viewModel()
    if(todo.isRemainderShown){
        Dialog(onDismissRequest = { /*TODO*/ }) {
            ReminderPopup(modifier = modifier, todo = todo)
        }
    }
    if(!todo.isChecked){
        Surface(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = todo.isChecked, onCheckedChange = {
                        viewModel.updateTodo(todo)
                    },
                    modifier = modifier.scale(.8f),
                    colors = CheckboxDefaults.colors(
                        checkedColor = CustomYellow
                    )
                )
                Column(horizontalAlignment = Alignment.Start) {
                    todo.task?.let {
                        Text(
                            text = it,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    if(todo.dueDate!=null){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "${formatDate(todo.dueDate!!)}", color = Color.Gray)
                            Icon(
                                painter = painterResource(id = R.drawable.reminder),
                                contentDescription = "",
                                tint = CustomYellow,
                                modifier = modifier.scale(.6f),
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AddButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(modifier = modifier) {
        Icon(
            imageVector = Icons.Rounded.AddCircle,
            contentDescription = "addIcon",
            tint = CustomYellow,
            modifier = modifier
                .size(60.dp)
                .clickable { onClick() },
        )
    }
}


@Composable
fun DatePicker(modifier: Modifier =Modifier.background(Color.Red)) {
    WheelDateTimePicker(
//        startDateTime = LocalDateTime.of(
//            2025, 10, 20, 5, 30
//        ),
        startDateTime = LocalDateTime.now(),
        minDateTime = LocalDateTime.now(),
        maxDateTime = LocalDateTime.of(
            2025, 10, 20, 5, 30
        ),
        timeFormat = TimeFormat.AM_PM,
        size = DpSize(200.dp, 100.dp),
        rowCount = 5,
        textStyle = MaterialTheme.typography.titleSmall,
        textColor = Color(0xFFffc300),
        selectorProperties = WheelPickerDefaults.selectorProperties(
            enabled = true,
            shape = RoundedCornerShape(0.dp),
            color = Color(0xFFf1faee).copy(alpha = 0.2f),
            border = BorderStroke(2.dp, Color(0xFFf1faee))
        ),
        modifier = modifier.background(Color.White)
    ){ snappedDateTime -> }
}
@Composable
fun DatePickerPopUp(modifier: Modifier=Modifier.background(Color.White),onClickCancel:()->Unit,onClickDone: (selectedDate:LocalDateTime?) -> Unit){
    var selectedDate:(java.time.LocalDateTime?) by remember {
        mutableStateOf(null)
    }
    Surface(shape=MaterialTheme.shapes.medium) {
        Column(modifier= modifier
            .background(Color.White)
            .padding(8.dp)) {
            WheelDateTimePicker(
//                startDateTime = LocalDateTime.of(
//                    2025, 10, 20, 5, 30
//                ),
                startDateTime = LocalDateTime.now(),
                minDateTime = LocalDateTime.now(),
                maxDateTime = LocalDateTime.of(
                    2030, 10, 20, 5, 30
                ),
                timeFormat = TimeFormat.AM_PM,
                size = DpSize(200.dp, 100.dp),
                rowCount = 5,
                textStyle = MaterialTheme.typography.titleSmall,
                textColor = Color(0xFFffc300),
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    enabled = true,
                    shape = RoundedCornerShape(0.dp),
                    color = Color(0xFFf1faee).copy(alpha = 0.2f),
                    border = BorderStroke(2.dp, Color(0xFFf1faee))
                ),
                modifier = modifier
                    .background(Color.White)
                    .fillMaxWidth()
            ){ snappedDateTime -> selectedDate=snappedDateTime }
            Text(text = if(selectedDate!=null)"Selected Time : ${formatDate(selectedDate!!)}"
                        else "",
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
            Row (horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .padding(15.dp)
                    .background(Color.White)
                    .fillMaxWidth()){
                Text(text = "Cancel",modifier = modifier
                    .clickable { onClickCancel() }
                    .fillMaxWidth(.3f)
                    .background(Color.LightGray, shape = RoundedCornerShape(3.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                    textAlign = TextAlign.Center
                )
                Text(text = "Done",modifier = modifier
                    .clickable { onClickDone(selectedDate) }
                    .fillMaxWidth(.4f)
                    .background(Color.LightGray, shape = RoundedCornerShape(3.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                    textAlign = TextAlign.Center

                )
            }

        }
    }
}

@Composable
fun ReminderPopup(modifier: Modifier = Modifier,todo: ToDo) {
    val viewModel: ToDoViewModel = viewModel()
    Surface(modifier=modifier.fillMaxWidth(.9f),
            shape = MaterialTheme.shapes.small
        ) {
        Column (modifier= modifier
            .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Text(
                text = "You got ${if (todo.task!=null) "\"${todo.task}\"" else ""} to complete",
                modifier = modifier
                    .padding(4.dp)
                    .padding(top = 12.dp)
            )
            Button(onClick = {
                viewModel.updateTodo(todo)
            },
                modifier=modifier.padding(top = 5.dp, bottom = 5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CustomYellow)
            ) {
                Text(text = "Done")
            }
        }

    }

}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        DatePicker(modifier = Modifier)
    }
}
@Preview(showBackground = true)
@Composable
fun DatePickerPopUpPreview() {
    DatePickerPopUp(modifier = Modifier, onClickCancel = { }, onClickDone = { })
}


@Preview(showBackground = true)
@Composable
fun ToDoCardPreview() {
    ToDoCard(modifier = Modifier, todo = ToDo())
}
@Preview(showBackground = true)
@Composable
fun CompletedToDoCardPreview() {
    CompletedTodoCard(modifier = Modifier, completedTodo = CompletedTodo())
}

@Preview(showBackground = true)
@Composable
fun AddButtonPreview() {
    AddButton(modifier = Modifier) {}
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AddToDoCardPreview() {
    AddToDoCard(modifier = Modifier, onClickDone = {})
}
@Preview(showBackground = true)
@Composable
fun ReminderPopupPreview() {
    var todo= ToDo()
    todo.task="task"
    todo.dueDate = LocalDateTime.now()
    ReminderPopup(modifier = Modifier,todo )
}