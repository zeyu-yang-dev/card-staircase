package io.github.zeyuyangdev.cardstaircase.view.panes

import io.github.zeyuyangdev.cardstaircase.entity.*
import io.github.zeyuyangdev.cardstaircase.service.Refreshable
import io.github.zeyuyangdev.cardstaircase.service.RootService
import io.github.zeyuyangdev.cardstaircase.view.*
import io.github.zeyuyangdev.cardstaircase.view.GameScene.UIState

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.gamecomponentviews.CardView



class PaneStairs(
    private val rootService: RootService,
    private val gameScene: GameScene
) : Pane<ComponentView>(
    STAIRS_POS_X,
    STAIRS_POS_Y,
    STAIRS_WIDTH,
    STAIRS_HEIGHT,
    visual = PANE_BG_VISUAL
), Refreshable {

    private val cardImageLoader = CardImageLoader()

    private val playerActionService = rootService.playerActionService

    // A mask that marks the clickable CardViews in the stairs
    private val stairsMask: List<MutableList<Boolean>> = listOf(5, 4, 3, 2, 1).map { MutableList(it) { false } }

    // Each column of cardViews is stored in a list.
    val cardViewsCol_1: MutableList<CardView> = mutableListOf()
    val cardViewsCol_2: MutableList<CardView> = mutableListOf()
    val cardViewsCol_3: MutableList<CardView> = mutableListOf()
    val cardViewsCol_4: MutableList<CardView> = mutableListOf()
    val cardViewsCol_5: MutableList<CardView> = mutableListOf()

    val stairCardViews = listOf(cardViewsCol_1, cardViewsCol_2, cardViewsCol_3, cardViewsCol_4, cardViewsCol_5)

    init {

        // Creates CardViews for a full staircase:
        var numOfCardsInCol = 5
        for (col in stairCardViews.indices) {
            // row from bottom to top
            for (row in 0 until numOfCardsInCol) {
                stairCardViews[col].add(createCardView(col, row))
            }
            numOfCardsInCol -= 1
        }

        // Defines the behavior of each cardView upon click:
        numOfCardsInCol = 5
        for (col in stairCardViews.indices) {
            for (row in 0 until numOfCardsInCol) {
                stairCardViews[col][row].apply {
                    onMouseClicked = {
                        // If this cardView is clickable:
                        if (stairsMask[col][row]) {
                            val currentPlayerIndex = rootService.currentGame.currentPlayer
                            val currentPlayer = rootService.currentGame.players[currentPlayerIndex]

                            // 1. When a turn is just started: Destroy action
                            if (gameScene.state == UIState.TURN_STARTED && currentPlayer.score >= 5) {
                                val stairs = rootService.currentGame.stairs
                                // Changes UI state before the refresh.
                                gameScene.state = UIState.HAS_DESTROYED
                                playerActionService.destroyCard(stairs[col][row])
                            }

                            // 2. When a card is selected from hand: Match action
                            if (gameScene.state == UIState.HAS_SELECTED) {
                                // Checks whether the two cards match.
                                val stairs = rootService.currentGame.stairs
                                if (gameScene.cardSelected!!.value == stairs[col][row].value ||
                                    gameScene.cardSelected!!.suit == stairs[col][row].suit) {
                                    // Changes UI state before the refresh.
                                    gameScene.state = UIState.HAS_PLAYED
                                    playerActionService.matchCard(gameScene.cardSelected!!, stairs[col][row])
                                    // Resets the selected hand card.
                                    gameScene.cardSelected = null
                                }
                            }
                        }
                    }
                }
            }
            numOfCardsInCol -= 1
        }

        // Adds all cardViews to this pane.
        for (i in stairCardViews.indices) {
            addAll(stairCardViews[i])
        }
    }

    /**
     * Creates a Cardview for this pane.
     * @param column The index of the 5 columns in stairs, from left to right.
     * @param row The index of the card in one column, from bottom to top.
     */
    private fun createCardView(column: Int, row: Int): CardView {
        val cardView = CardView(
            posX = BOTT_LEFT_OFFSET_X + HORIZ_DIS * column,
            posY = BOTT_LEFT_OFFSET_Y - VERTIC_DIS * row,
            width = CARD_WIDTH,
            height = CARD_HEIGHT,
            front = cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE),
            back = cardImageLoader.backImage,
        ).apply {
            this.showFront()
        }
        return cardView
    }

    /**
     * Refreshes the visual and visibility for each [CardView].
     */
    private fun refreshCardContent() {
        val stairs = rootService.currentGame.stairs

        // Sets visual and visibility for each cardView.
        for (col_index in stairCardViews.indices) {
            // If this stack in stairs is not empty:
            if (stairs[col_index].isNotEmpty()) {
                for (row_index in stairCardViews[col_index].indices) {
                    if (row_index in 0 until stairs[col_index].size) {
                        stairCardViews[col_index][row_index].apply {
                            frontVisual = cardImageLoader.frontImageFor(stairs[col_index][row_index].suit, stairs[col_index][row_index].value)
                            this.isVisible = true
                        }
                    }
                    else {
                        stairCardViews[col_index][row_index].isVisible = false
                    }
                }
            }
            // If this stack in stairs is empty:
            else {
                for (cardView in stairCardViews[col_index]) {
                    cardView.isVisible = false
                }
            }
        }
    }

    /**
     * Refreshes for each cardView:
     * if this cardView show a card front and
     * if this cardView is clickable.
     */
    private fun refreshCardSide() {
        val stairs = rootService.currentGame.stairs

        // Sets all cardViews to show card back.
        for (col_index in stairCardViews.indices) {
            for (row_index in stairCardViews[col_index].indices) {
                stairCardViews[col_index][row_index].apply {
                    this.showBack()
                }
            }
        }

        // Sets all cardView to be unclickable.
        for (column in stairsMask) {
            for (row_index in column.indices) {
                column[row_index] = false
            }
        }

        /**
         * Sets the cardView, which represents a [Card] on top of a stack in [CardStaircase.stairs]
         * to show the front visual corresponding to this [Card] and
         * to be clickable.
         */
        for (col_index in stairs.indices) {
            if (stairs[col_index].isNotEmpty()) {
                stairCardViews[col_index][stairs[col_index].size - 1].showFront()
                stairsMask[col_index][stairs[col_index].size - 1] = true
            }
        }
    }

    private fun refreshThisPane() {
        refreshCardContent()
        refreshCardSide()
    }

    override fun refreshAfterStartNewGame() = refreshThisPane()
    override fun refreshAfterStartTurn() = refreshThisPane()
    override fun refreshAfterDestroyCard() = refreshThisPane()
    override fun refreshAfterPlayCard() = refreshThisPane()
    override fun refreshAfterDiscardCard() = refreshThisPane()
    override fun refreshAfterEndTurn() = refreshThisPane()
    override fun refreshAfterEndGame() = refreshThisPane()
}