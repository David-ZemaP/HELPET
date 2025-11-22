package com.ucb.helpet.features.login.domain.model

@JvmInline
value class Location private constructor(val value: String) {
    init {
        require(value.isNotEmpty()) { "La ubicación no puede estar vacía" }
    }

    override fun toString(): String = value

    companion object {
        fun create(value: String): Location {
            return Location(value)
        }
    }
}