package io.github.zeyuyangdev.cardstaircase.service
import io.github.zeyuyangdev.cardstaircase.entity.*

class PlayerActionService(private val rootService: RootService): AbstractRefreshingService() {

    /**
     * Destroys a card from the staircase.
     * @param card The [Card] instance on top of one of the stacks in the staircase.
     */
    fun destroyCard(card: Card) {

        val currentGame = rootService.currentGame
        val currentPlayerIndex = rootService.currentGame.currentPlayer
        val currentPlayer = rootService.currentGame.players[currentPlayerIndex]
        val playerName = currentPlayer.name

        require(currentPlayer.score >= 5) {"The player must have at least 5 points to destroy a card."}

        // Removes the card from the stair and put it to the discard stack.
        for (stack in rootService.currentGame.stairs) {
            if (stack.isNotEmpty() && stack.peek() == card) {
                rootService.currentGame.discardStack.push(stack.pop())
                break
            }
        }

        // Decreases the score of the current player by 5.
        currentPlayer.score -= 5

        // Logs the action of the current player.
        currentGame.gameLog.add("${playerName} destroyed ${card} from stairs.")

        // Marks that the stairs has been modified.
        currentGame.stairsModified = true

        // If the game end condition is fulfilled, call endGame()
        if (currentGame.stairs.all { it.isEmpty() }) {
            rootService.gameService.endGame()
            println("CardStaircase ended in condition 1.")
        }

        onAllRefreshables { refreshAfterDestroyCard() }
    }


    /**
     * Matches a card in hand with a card in the stairs.
     * @param card The card in hand.
     * @param target The card in the staircase.
     */
    fun matchCard(card: Card, target: Card) {

        require(card.value == target.value || card.suit == target.suit) {"The two cards must match."}

        val currentGame = rootService.currentGame
        val currentPlayerIndex = rootService.currentGame.currentPlayer
        val currentPlayer = rootService.currentGame.players[currentPlayerIndex]
        val playerName = currentPlayer.name

        // RemoveS the card from the current player's hand.
        currentPlayer.hand.remove(card)

        // Removes the card from the staircase.
        for (stack in currentGame.stairs) {
            if (stack.isNotEmpty() && stack.peek() == target) {
                stack.pop()
                break
            }
        }

        // Increases the score of the current player.
        currentPlayer.score += card.value.toInt()
        currentPlayer.score += target.value.toInt()

        // Logs the action of the current player.
        currentGame.gameLog.add("${playerName} matched ${card} with ${target}.")

        // Marks that the stairs has been modified.
        rootService.currentGame.stairsModified = true

        // If Condition 1 fulfilled:
        // If the game end condition is fulfilled, call endGame()
        if (rootService.currentGame.stairs.all { it.isEmpty() }) {
            rootService.gameService.endGame()
            println("CardStaircase ended in condition 1.")
        }

        onAllRefreshables { refreshAfterPlayCard() }
    }


    /**
     * Discards a card from hand, is an alternative action of playCard.
     * @param card The [Card] instance in player's hand.
     */
    fun discardCard(card: Card) {

        val currentGame = rootService.currentGame
        val currentPlayerIndex = rootService.currentGame.currentPlayer
        val currentPlayer = rootService.currentGame.players[currentPlayerIndex]
        val playerName = currentPlayer.name

        // Removes the card from current player's hand.
        currentPlayer.hand.remove(card)

        // The card goes to the discard stack.
        currentGame.discardStack.push(card)

        // Logs the action of the current player.
        currentGame.gameLog.add("${playerName} discarded ${card} from hand.")

        onAllRefreshables { refreshAfterDiscardCard() }
    }

    /**
     * Invoked when the START TURN button is pressed.
     */
    fun startTurn() {
        onAllRefreshables { refreshAfterStartTurn() }
    }

    /**
     * Invoked after the flip-close animation.
     */
    fun endTurn() {
        val currentGame = rootService.currentGame
        val currentPlayerIndex = rootService.currentGame.currentPlayer
        val currentPlayer = rootService.currentGame.players[currentPlayerIndex]

        // Game end condition 2: When both draw and discard stacks are empty.
        if (currentGame.drawStack.isEmpty() && currentGame.discardStack.isEmpty()) {
            rootService.gameService.endGame()
            println("CardStaircase ended in condition 2.")
            return
        }

        // The current player draws a card.
        currentPlayer.hand.add(currentGame.drawStack.pop())

        // Switches the current player.
        rootService.currentGame.currentPlayer = (currentPlayerIndex + 1) % 2

        // Game end condition 3:
        // The draw stack is empty. && The staircase is not modified since the last shuffle.
        if (currentGame.drawStack.isEmpty() && currentGame.stairsModified == false) {
            rootService.gameService.endGame()
            println("CardStaircase ended in condition 3.")
        }

        // Shuffles the stack if the draw stack is empty. (The discard stack is not empty.)
        if (currentGame.drawStack.isEmpty() && currentGame.discardStack.isNotEmpty()) {
            rootService.gameService.shuffleStack()
        }

        onAllRefreshables { refreshAfterEndTurn() }
    }
}