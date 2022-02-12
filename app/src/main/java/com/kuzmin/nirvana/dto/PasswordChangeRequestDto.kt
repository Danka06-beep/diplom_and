package com.kuzmin.nirvana.dto

data class PasswordChangeRequestDto(
    val old: String,
    val new: String
) {
}