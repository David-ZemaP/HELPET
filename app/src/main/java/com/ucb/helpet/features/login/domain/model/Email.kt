package com.ucb.helpet.features.login.domain.model


@JvmInline
value class Email private constructor(val value: String) {
    init {
        require(value.contains("@")) { "El email no es válido" }
    }

    override fun toString(): String = value

    companion object {
        fun create(value: String): Email {
            return Email(value) // You can add stricter regex validation here
        }
    }
}