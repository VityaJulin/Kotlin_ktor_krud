package com.example.dto

data class RequestDto (
        val id: Long,
        val author: String,
        val content: String? = null
)