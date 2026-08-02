package com.fluro.checkout.domain

data class Money(val amountInPence: Int) {
    init {
        require(amountInPence >= 0) { "Money amount must be non-negative" }
    }

    operator fun plus(other: Money): Money {
        return Money(this.amountInPence + other.amountInPence)
    }

    operator fun minus(other: Money): Money {
        return Money(maxOf(0, this.amountInPence - other.amountInPence))
    }

    operator fun times(multiplier: Int): Money {
        return Money(this.amountInPence * multiplier)
    }

    companion object {
        val ZERO = Money(0)
    }
}
