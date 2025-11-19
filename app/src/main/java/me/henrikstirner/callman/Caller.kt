package me.henrikstirner.callman

import java.util.UUID

data class Caller(
    val id: String = UUID.randomUUID().toString(),
    val name: String?,
    val number: String
)
