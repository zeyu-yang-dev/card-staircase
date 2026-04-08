package io.github.zeyuyangdev.cardstaircase.service
import io.github.zeyuyangdev.cardstaircase.entity.*

class PlayerActionService(private val rootService: RootService): AbstractRefreshingService() {


    // 一回合中最开始可以进行的动作
    fun destroyCard(card: Card) {

        val currentGame = rootService.currentGame
        val currentPlayerIndex = rootService.currentGame.currentPlayer
        val currentPlayer = rootService.currentGame.players[currentPlayerIndex]
        val playerName = currentPlayer.name

        require(currentPlayer.score >= 5) {"The player must have at least 5 points to destroy a card."}

        // remove the card from the stair and put it to the discard stack
        for (stack in rootService.currentGame.stairs) {
            if (stack.isNotEmpty() && stack.peek() == card) {
                rootService.currentGame.discardStack.push(stack.pop())
                break
            }
        }

        // Decrease the score of the current player by 5
        currentPlayer.score -= 5

        // Log the action of the current player
        currentGame.gameLog.add("${playerName} destroyed ${card} from stairs.")

        // Mark that the stairs has been modified
        currentGame.stairsModified = true

        // If the game end condition is fulfilled, call endGame()
        if (currentGame.stairs.all { it.isEmpty() }) {
            rootService.gameService.endGame()
            println("CardStaircase ended in condition 1.")
        }

        onAllRefreshables { refreshAfterDestroyCard() }
    }


    /**
     * Combine a card in hand with a card in the stairs.
     * @param card The card in hand.
     * @param target The card in the stairs.
     */
    fun playCard(card: Card, target: Card) {

        require(card.value == target.value || card.suit == target.suit) {"The two cards must match."}

        val currentGame = rootService.currentGame
        val currentPlayerIndex = rootService.currentGame.currentPlayer
        val currentPlayer = rootService.currentGame.players[currentPlayerIndex]
        val playerName = currentPlayer.name

        // remove the card from the players hand
        currentPlayer.hand.remove(card)

        // remove the card from the stair
        for (stack in currentGame.stairs) {
            if (stack.isNotEmpty() && stack.peek() == target) {
                stack.pop()
                break
            }
        }

        // Increase the score of the current player
        currentPlayer.score += card.value.toInt()
        currentPlayer.score += target.value.toInt()

        // Log the action of the current player
        currentGame.gameLog.add("${playerName} matched ${card} with ${target}.")

        // Mark that the stairs has been modified
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
     * @param card
     */
    fun discardCard(card: Card) {

        val currentGame = rootService.currentGame
        val currentPlayerIndex = rootService.currentGame.currentPlayer
        val currentPlayer = rootService.currentGame.players[currentPlayerIndex]
        val playerName = currentPlayer.name

        // Remove the card from current player's hand
        currentPlayer.hand.remove(card)

        // The card goes to the discard stack
        currentGame.discardStack.push(card)

        // Log the action of the current player
        currentGame.gameLog.add("${playerName} discarded ${card} from hand.")

        onAllRefreshables { refreshAfterDiscardCard() }
    }

    // 对应UI上的start按键
    fun startTurn() {
        onAllRefreshables { refreshAfterStartTurn() }
    }

    fun endTurn() {
        val currentGame = rootService.currentGame
        val currentPlayerIndex = rootService.currentGame.currentPlayer
        val currentPlayer = rootService.currentGame.players[currentPlayerIndex]


        // TODO: 检查判断条件
        // 为了防止drawStack和discardStack同时为空，需要直接强制结束游戏
        if (currentGame.drawStack.isEmpty() && currentGame.discardStack.isEmpty()) {
            rootService.gameService.endGame()
            println("CardStaircase ended in condition 2.")
            return
        }

        // The current player draws a card
        // 注意drawStack和discardStack有可能同时空
        currentPlayer.hand.add(currentGame.drawStack.pop())

        // switch the current player
        rootService.currentGame.currentPlayer = (currentPlayerIndex + 1) % 2


        // If the game end condition is fulfilled, call endGame()
        if (currentGame.drawStack.isEmpty() && currentGame.stairsModified == false) {
            rootService.gameService.endGame()
            println("CardStaircase ended in condition 3.")
        }

        // shuffle the stack if the draw stack is empty and discard stack is not empty
        if (currentGame.drawStack.isEmpty() && currentGame.discardStack.isNotEmpty()) {
            rootService.gameService.shuffleStack()
        }

        onAllRefreshables { refreshAfterEndTurn() }

    }




}