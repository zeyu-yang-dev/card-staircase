package io.github.zeyuyangdev.cardstaircase.entity

import java.util.Stack

/**
 * Root entity that holds the whole game state.
 *
 * @property players        the two players in turn order (size = 2)
 * @property stairs         the 5 stair stacks; each row is a [Stack] of [Card]
 * @property currentPlayer  index of the player whose turn it is (0 or 1)
 * @property gameLog        list of public log messages (anonymized)
 * @property stairsModified flag set to true once a stair was changed during the current turn
 * @property drawStack      the stack representing the draw stack
 * @property discardStack   the stack representing the discard stack
 */
data class CardStaircase(val players: List<Player>) {
    var currentPlayer: Int = 0

    var stairs: List<Stack<Card>> = listOf(Stack(), Stack(), Stack(), Stack(), Stack())
    var stairsModified: Boolean = false

    var gameLog: MutableList<String> = mutableListOf()

    var drawStack: Stack<Card> = Stack()
    var discardStack: Stack<Card> = Stack()
}







