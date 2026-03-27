package io.github.zeyu.staircasecardgame.view.panes

import io.github.zeyu.staircasecardgame.entity.*
import io.github.zeyu.staircasecardgame.view.CardImageLoader
import io.github.zeyu.staircasecardgame.view.GameScene

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import io.github.zeyu.staircasecardgame.service.Refreshable
import io.github.zeyu.staircasecardgame.service.RootService
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual


// Element in this pane



// Pane properties
const val STACKS_POS_X = (1920 - CARD_WIDTH * 2 - DIS_BET_CARDS) / 2
const val STACKS_POS_Y = 30

const val STACKS_WIDTH = CARD_WIDTH * 2 + DIS_BET_CARDS
const val STACKS_HEIGHT = CARD_HEIGHT

private const val GAME_LOG_POS_Y = (CARD_HEIGHT - BUTTON_HEIGHT) / 2 - STACKS_POS_Y



class PaneStacks(private val rootService: RootService, private val gameScene: GameScene) : Pane<ComponentView>(
    STACKS_POS_X, STACKS_POS_Y, STACKS_WIDTH, STACKS_HEIGHT, visual = ColorVisual.TRANSPARENT), Refreshable {

    private val cardImageLoader = CardImageLoader()

    val drawStackView = CardView(
        posX = 0,
        posY = 0,
        width = CARD_WIDTH,
        height = CARD_HEIGHT,
        front = cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE),
        back = cardImageLoader.backImage,
    ).apply {
        this.showBack()
    }

    val discardStackView = CardView(
        posX = CARD_WIDTH + DIS_BET_CARDS,
        posY = 0,
        width = CARD_WIDTH,
        height = CARD_HEIGHT,
        front = cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE),
        back = cardImageLoader.backImage,
    ).apply {
        this.showFront()
    }

    val stackCardViews = listOf(drawStackView, discardStackView)

    val gameLogLabel: Label = Label(
        posX = BUTTON_POS_X + (POS_X_2 - STACKS_POS_X),
        posY = GAME_LOG_POS_Y,
        width = BUTTON_WIDTH,
        height = BUTTON_HEIGHT,
        text = "GAME LOG",
        font = Font(size = 24, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.ITALIC),
        alignment = Alignment.CENTER,
        visual = ColorVisual(55, 55, 55, 0.5),
    ).apply {
        onMouseEntered = {
            this.isVisible = false
            gameLogListView.isVisible = true
        }
    }



    val gameLogListView: ListView<String> = ListView<String>(
        posX = PLAYER_LABEL_POS_X + (POS_X_2 - STACKS_POS_X),
        posY = GAME_LOG_POS_Y,
        width = PLAYER_LABEL_WIDTH,
        height = BUTTON_HEIGHT * 6,
        items = emptyList(),
        font = Font(size = 20, color = Color.WHITE,
            fontWeight = Font.FontWeight.NORMAL, fontStyle = Font.FontStyle.NORMAL),
        // alignment = Alignment.TOP_CENTER,
        visual = ColorVisual(55, 55, 55, 0.5),
        orientation = Orientation.VERTICAL,
    ).apply {
        this.isVisible = false
    }

    private fun refreshGameLog() {
        val gameLog = rootService.currentGame.gameLog
        gameLogListView.items.setAll(gameLog.asReversed())
    }



    fun refreshStacks() {
        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"refreshStacks() failed, currentGame is null"}

        val drawStack = currentGame.drawStack
        val discardStack = currentGame.discardStack

        // Refresh draw stack
        if (!drawStack.isEmpty()) {
            // If draw stack is not empty, show a card back
            drawStackView.apply {
                backVisual = cardImageLoader.backImage
                this.showBack()
            }
        } else {
            // If draw stack is empty, show a blank card front
            drawStackView.apply {
                frontVisual = cardImageLoader.blankImage
                this.showFront()
            }
        }

        // Refresh discard stack
        if (!discardStack.isEmpty()) {
            // If discard stack is not empty, show the card on top
            val topCard = discardStack.peek()
            discardStackView.apply {
                frontVisual = cardImageLoader.frontImageFor(topCard.suit, topCard.value)
                this.showFront()
            }
        } else {
            // If discard stack is empty, show a blank card front
            discardStackView.apply {
                frontVisual = cardImageLoader.blankImage
                this.showFront()
            }
        }

    }



    init {

        addAll(stackCardViews)
        addAll(gameLogLabel, gameLogListView)



    }


    override fun refreshAfterStartNewGame() {
        refreshStacks()
        refreshGameLog()
    }



    override fun refreshAfterStartTurn() {
        refreshStacks()
        refreshGameLog()
    }

    override fun refreshAfterDestroyCard() {
        refreshStacks()
        refreshGameLog()
    }

    override fun refreshAfterPlayCard() {
        refreshStacks()
        refreshGameLog()
    }

    override fun refreshAfterDiscardCard() {
        refreshStacks()
        refreshGameLog()
    }

    override fun refreshAfterEndTurn() {
        refreshStacks()
        refreshGameLog()
    }

    override fun refreshAfterShuffleStack() {
        refreshStacks()
        refreshGameLog()
    }

    override fun refreshAfterEndGame() {
        refreshStacks()
        refreshGameLog()
    }








}