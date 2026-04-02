package io.github.zeyuyangdev.staircasecardgame.entity

/**
 * Represents one player in the game.
 *
 * @property name        display name of the player
 * @property score       current score (non-negative; starts at 0)
 * @property hand        read-only view of the cards currently in hand
 */
class Player(val name: String) {
    var score: Int = 0
    val hand: MutableList<Card> = mutableListOf()
}
