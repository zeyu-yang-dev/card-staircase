package io.github.zeyuyangdev.cardstaircase.entity

/**
 * Represents one player in the game.
 *
 * @property name        name of the player
 * @property score       current score
 * @property hand        the cards currently in hand
 */
class Player(val name: String) {
    var score: Int = 0
    val hand: MutableList<Card> = mutableListOf()
}
