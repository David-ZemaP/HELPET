package com.ucb.helpet.features.login.domain.model

@JvmInline
value class Phone private constructor(val value: String) {
    init {
        require(value.isNotEmpty()) { "El teléfono no puede estar vacío" }
    }

    override fun toString(): String = value

    companion object {
        fun create(value: String): Phone {
            // Add validation logic if needed (e.g., regex for numbers)
            return Phone(value)
        }
    }
}