package io.github.zeyuyangdev.cardstaircase.service

import io.github.zeyuyangdev.cardstaircase.entity.*
import java.util.Stack

class GameService(private val rootService: RootService): AbstractRefreshingService() {

    fun startNewGame(player1: String, player2: String) {

        require (player1 != "" && player2 != "") {"Player names can't be empty."}
        require (player1 != player2) {"Players can't have identical names."}

        // (uncomment below to activate random player order)
        val shuffled = listOf(player1, player2) // .shuffled()
        val player_01 = Player(shuffled[0])
        val player_02 = Player(shuffled[1])

        // Creates an instance of [CardStaircase] representing the current round of game.
        rootService.currentGame = CardStaircase(players = listOf(player_01, player_02))
        val currentGame = rootService.currentGame

        // Creates a new draw Stack of 52 cards.
        val drawStack = createDrawStack()

        // Fills in the stairs.
        val stairs = currentGame.stairs
        var heightOfStair = 5
        for (i in stairs.indices) {
            repeat(heightOfStair) {
                currentGame.stairs[i].push(drawStack.pop())
            }
            heightOfStair -= 1
        }

        // Gives each player 5 hand cards.
        for (player in currentGame.players) {
            repeat(5) {
                player.hand.add(drawStack.pop())
            }
        }

        // The rest cards go to the draw stack.
        currentGame.drawStack = drawStack

        // Logs the first turn.
        currentGame.gameLog.add("↑ ──────── Turn ${currentGame.turnCount} ────────")

        onAllRefreshables { refreshAfterStartNewGame() }
    }

    /**
     * Shuffles the discard stack and move the cards to draw stack.
     * Called in endTurn()
     */
    fun shuffleStack() {
        val currentGame = rootService.currentGame
        require (currentGame.drawStack.isEmpty() && currentGame.discardStack.isNotEmpty()) {"Can't shuffle now."}

        currentGame.discardStack.shuffle()
        currentGame.drawStack = currentGame.discardStack
        currentGame.discardStack = Stack()

        currentGame.stairsModified = false

        onAllRefreshables { refreshAfterShuffleStack() }
    }

    // Called in endTurn() AND in destroyCard() AND in playCard()
    fun endGame() {
        onAllRefreshables { refreshAfterEndGame() }
    }

    /**
     * Creates a full stack of 52 cards.
     * @return A full stack of 52 cards.
     */
    private fun createDrawStack() : Stack<Card> {

        val drawStack = Stack<Card>()

        for(suit in CardSuit.entries) {
            for(value in CardValue.entries) {
                drawStack.push(Card(suit, value))
            }
        }

        drawStack.shuffle()
        return drawStack
    }
}