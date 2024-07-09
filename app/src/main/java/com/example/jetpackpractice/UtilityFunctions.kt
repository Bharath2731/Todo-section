package com.example.jetpackpractice

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatDate(DateTime:LocalDateTime):String{
    // Define the output format
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yy, HH:mm")
    // Format the LocalDateTime to the desired format
    return DateTime.format(outputFormatter)

}

