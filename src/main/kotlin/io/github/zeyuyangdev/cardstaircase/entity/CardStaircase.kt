package io.github.zeyuyangdev.cardstaircase.entity

import java.util.Stack

/**
 * Root entity that holds the whole game state.
 *
 * @property players        the two players in turn order (size = 2)
 * @property stairs         a list of 5 [Stack]s representing the staircase, each [Stack] represents a column.
 * @property currentPlayer  index of the player who's in turn (0 or 1)
 * @property gameLog        list of log messages
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







