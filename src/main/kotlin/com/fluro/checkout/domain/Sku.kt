package com.fluro.checkout.domain

@Suppress("MemberVisibilityCanBePrivate")
@JvmInline
value class Sku(val value: String) {
    init {
        require(value.isNotBlank()) { "Sku value cannot be blank" }
    }
}
