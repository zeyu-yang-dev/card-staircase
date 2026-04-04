package io.github.zeyuyangdev.cardstaircase.entity

/**
 * Represents a single playing card.
 * It is characterized by a [CardSuit] and a [CardValue].
 *
 * @property suit  the suit of the card (♣/♠/♥/♦)
 * @property value the value of the card (2..10/J/Q/K/A)
 */
data class Card(
    val suit: CardSuit,
    val value: CardValue
) {
    /** Compact label like "♥A". */
    override fun toString(): String = "$suit$value"

}
