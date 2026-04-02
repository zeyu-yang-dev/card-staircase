package io.github.zeyuyangdev.staircasecardgame.service

import io.github.zeyuyangdev.staircasecardgame.entity.*
import java.util.Stack

class GameService(private val rootService: RootService): AbstractRefreshingService() {

    fun startNewGame(player1: String, player2: String) {

        require (player1 != "" && player2 != "") {"Player name can't be null."}
        require (player1 != player2) {"Players can't have identical names."}

        // 取消以随机顺序开始, 改为交替先手，实现在KartentreppeApplication中
        val shuffled = listOf(player1, player2) // .shuffled()
        val player_01 = Player(shuffled[0])
        val player_02 = Player(shuffled[1])

        // Create a new instance of Game class
        rootService.currentGame = Game(players = listOf(player_01, player_02))
        val currentGame = rootService.currentGame

        // Create a new draw Stack
        val drawStack = createDrawStack()

        // Fill in the stairs
        val stairs = currentGame.stairs
        var heightOfStair = 5
        for (i in stairs.indices) {
            repeat(heightOfStair) {
                currentGame.stairs[i].push(drawStack.pop())
            }
            heightOfStair -= 1
        }

        // Give each player 5 hand cards
        for (player in currentGame.players) {
            repeat(5) {
                player.hand.add(drawStack.pop())
            }
        }

        // The rest cards go to the draw stack
        currentGame.drawStack = drawStack


        onAllRefreshables { refreshAfterStartNewGame() }
    }

    // Called in endTurn()
    fun shuffleStack() {
        val currentGame = rootService.currentGame
        require (currentGame.drawStack.isEmpty() && currentGame.discardStack.isNotEmpty()) {"Can't shuffle now."}

        currentGame.discardStack.shuffle()
        currentGame.drawStack = currentGame.discardStack
        currentGame.discardStack = Stack()

        currentGame.stairsModified = false

        onAllRefreshables { refreshAfterShuffleStack() }
    }

    // Called in endTurn() AND in destroyCard() AND playCard()
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

        // // 为了测试洗切和相关的结束条件
        // // 这样就只有两张牌了
        // repeat(25) {
        //     drawStack.pop()
        // }

        return drawStack
    }

    //------------------------------------------------------------------------------------------------------------------

    private fun findCardInStack(valueNeeded: CardValue): Int {
        val currentGame = rootService.currentGame
        return currentGame.drawStack.indexOfFirst { it.value ==  valueNeeded }
    }

    private fun swapToTop(indexOfCard: Int) {
        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}
        val drawStack = currentGame.drawStack

        val cardNeeded = drawStack[indexOfCard]
        val cardOnTop = drawStack.peek()

        drawStack[indexOfCard] = cardOnTop
        drawStack[drawStack.size - 1] = cardNeeded
    }

    fun shuffleDrawStack(option: Boolean) {

        val candidates = if (option) {
            listOf(CardValue.KING, CardValue.QUEEN, CardValue.TEN).shuffled()
        } else {
            listOf(CardValue.ACE, CardValue.TWO, CardValue.THREE, CardValue.FOUR).shuffled()
        }

        for (value in candidates) {
            val index = findCardInStack(value)
            if (index != -1) {
                swapToTop(index)
                return
            }
        }
    }






}