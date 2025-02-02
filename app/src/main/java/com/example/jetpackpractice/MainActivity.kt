package com.example.jetpackpractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                ToDoHome()
        }
    }
}

@Composable
fun ToDoHome() {
    val viewModel: ToDoViewModel = viewModel()
    val completedTodoViewModel: CompletedTodoViewModel = viewModel()
    val todoList  = viewModel.todoList.collectAsState().value
    val completedToDoList = completedTodoViewModel.completedTodoList.collectAsState().value.reversed()
    var showAddToDoCard by remember { mutableStateOf(false) }
    var showCompletedList by remember { mutableStateOf(true) }
    val angle by animateFloatAsState(
        targetValue =if (showCompletedList) 180f else 0f
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
//            itemsIndexed(todoList, key = {index,_->index}){
//                index, item ->
//                if (item != null) {
//                    println("myList $todoList")
//                    ToDoCard(todo=item)
//                }
//            }
            if(todoList!=null) {
                items(items = todoList, itemContent = { item ->
                    if (!item.isChecked)
                        ToDoCard(todo = item)
                })
            }
            if(completedToDoList.isNotEmpty()) {
                item {
                    Column {
                    Spacer(modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth())
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            showCompletedList = !showCompletedList
                        }
                        ) {
                        Text(text = "Completed",
                            Modifier.padding(start = 16.dp),
                            color = Color.Gray,
                        )
                        Icon(imageVector = Icons.Rounded.KeyboardArrowDown ,
                            contentDescription = "",
                            tint = Color.LightGray,
                            modifier = Modifier.rotate(angle)
                            )
                    }
                }
                }
                if(showCompletedList){
                    items(items = completedToDoList, itemContent = { item ->
                        CompletedTodoCard(completedTodo = item)
                    })
                }

            }
        }

        if (showAddToDoCard) {
            Dialog(onDismissRequest = { showAddToDoCard = false }) {
                AddToDoCard(modifier = Modifier.padding(5.dp),
                    onClickDone = { showAddToDoCard = false })
            }
        }

        AddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { showAddToDoCard = true }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ToDoHomePreview() {
    ToDoHome()
}