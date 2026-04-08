package io.github.zeyuyangdev.cardstaircase.view.panes

import io.github.zeyuyangdev.cardstaircase.entity.*
import io.github.zeyuyangdev.cardstaircase.view.*
import io.github.zeyuyangdev.cardstaircase.view.GameScene.UIState
import io.github.zeyuyangdev.cardstaircase.service.Refreshable
import io.github.zeyuyangdev.cardstaircase.service.RootService

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.animation.FlipAnimation
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.animation.ParallelAnimation
import tools.aqua.bgw.visual.ImageVisual

/**
 * Abstract base class for all player panes.
 */
abstract class PanePlayer(
    protected val rootService: RootService,
    protected val gameScene: GameScene,
    protected val playerOfThisPane: Int, // O for left, 1 for right
    posX: Number,
    posY: Number,
    width: Number,
    height: Number
) : Pane<ComponentView>(
    posX,
    posY,
    width,
    height,
    visual = PANE_BG_VISUAL
), Refreshable {

    protected val cardImageLoader = CardImageLoader()
    protected val playerActionService = rootService.playerActionService

    protected val handCardViews: MutableList<CardView> = mutableListOf()
    protected val handCardViewsForAnimation: MutableList<CardView> = mutableListOf()

    protected var cardsRevealed: Boolean = false

    /**
     * Displays the score of the player of this pane.
     */
    protected val playerLabel1: Label = Label(
        posX = PLAYER_LABEL_POS_X,
        posY = PLAYER_LABEL_1_POS_Y,
        width = PLAYER_LABEL_WIDTH,
        height = PLAYER_LABEL_HEIGHT,
        font = PLAYER_LABEL_FONT,
        alignment = Alignment.CENTER,
        visual = PLAYER_LABEL_VISUAL,
    )

    /**
     * Displays the name of the player of this pane.
     */
    protected val playerLabel2: Label = Label(
        posX = PLAYER_LABEL_POS_X,
        posY = PLAYER_LABEL_2_POS_Y,
        width = PLAYER_LABEL_WIDTH,
        height = PLAYER_LABEL_HEIGHT,
        font = PLAYER_LABEL_FONT,
        alignment = Alignment.CENTER,
        visual = PLAYER_LABEL_VISUAL,
    )

    /**
     * The only button used by the player.
     */
    protected val button = Button(
        width = BUTTON_WIDTH,
        height = BUTTON_HEIGHT,
        posX = BUTTON_POS_X,
        posY = BUTTON_POS_Y,
        text = "START TURN",
        font = BUTTON_FONT,
        visual = BUTTON_DEFAULT_VISUAL
    ).apply {
        onMouseClicked = {
            // 1. When player's turn is ready to start:
            if (gameScene.state == UIState.TURN_READY_START) {
                cardsRevealed = true
                gameScene.state = UIState.TURN_STARTED
                playerActionService.startTurn()
                this.isVisible = false
                gameScene.cardSelected = null
                playFlipAnimation(true)
            }

            // 2. After a card is matched or discarded:
            // Condition 2 must be placed before condition 3,
            // otherwise 2 will be automatically fulfilled.
            if (gameScene.state in setOf(UIState.HAS_DISCARDED, UIState.HAS_PLAYED)) {
                this.isVisible = false
                cardsRevealed = false
                gameScene.state = UIState.TURN_READY_START
                playFlipAnimation(false)
            }

            // 3. After the player has selected a card from hand:
            if (gameScene.state == UIState.HAS_SELECTED) {
                gameScene.state = UIState.HAS_DISCARDED
                playerActionService.discardCard(gameScene.cardSelected!!)
                gameScene.cardSelected = null
            }
        }
    }

    protected val indicator = Label(
        width = INDICATOR_WIDTH,
        height = INDICATOR_HEIGHT,
        posX = INDICATOR_POS_X,
        posY = INDICATOR_POS_Y,
        visual = ImageVisual("indicator.png")
    )

    //------------------------------------------------------------------------------------------------------------------
    init {

        for (i in 0 until 5) {
            handCardViews.add(createCardView(i))
        }

        // Define the function upon click for each cardView.
        for (i in handCardViews.indices) {
            handCardViews[i].apply {
                onMouseClicked = {

                    if (gameScene.state in setOf(UIState.TURN_STARTED, UIState.HAS_DESTROYED, UIState.HAS_SELECTED)) {
                        val handCards = rootService.currentGame.players[playerOfThisPane].hand
                        gameScene.cardSelected = handCards[i]
                        gameScene.state = UIState.HAS_SELECTED
                        moveIndicator(i)
                    }
                    refreshButton()
                }
            }
        }

        addAll(handCardViews)
        addAll(
            button,
            playerLabel1,
            playerLabel2,
            indicator
        )
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Move the indicator to postion 0, 1, 2, 3, 4.
     */
    protected fun moveIndicator(pos: Int) {
        val coordinateX = INDICATOR_POS_X + HORIZ_DIS * pos
        indicator.apply {
            isVisible = true
            posX = coordinateX
        }
    }

    protected fun hideIndicator() {
        indicator.isVisible = false
    }
    //------------------------------------------------------------------------------------------------------------------

    protected fun playFlipAnimation(flipOpen: Boolean) {

        // show cardViews for animation
        addCardViewsForAnimation(flipOpen)

        // delay animation before flip-open animation to avoid briefly empty deck
        gameScene.playAnimation(
            DelayAnimation(DELAY_FOR_FLICKER_REMOVAL).apply {
                onFinished = {
                    // hide cardViews for normal gameplay
                    for (cardView in handCardViews) {cardView.isVisible = false}

                    // play flip animation
                    val flipAnimationList = createFlipAnimationList(flipOpen)
                    for (cardView in handCardViewsForAnimation) {cardView.toFront()}
                    val parallelAnimation = ParallelAnimation(flipAnimationList).apply {
                        onFinished = {

                            // hide cardViews for animation
                            for (cardView in handCardViewsForAnimation) {cardView.isVisible = false}

                            removeCardViewsForAnimation()
                            if (!flipOpen) {
                                rootService.playerActionService.endTurn()
                            }

                        }
                    }
                    gameScene.playAnimation(parallelAnimation)

                    // slightly shorter delay animation to avoid briefly empty deck
                    gameScene.playAnimation(
                        DelayAnimation(FLIP_ANIMATION_DURATION - DELAY_FOR_FLICKER_REMOVAL).apply {
                            onFinished = {
                                // show cardViews for normal gameplay
                                showNormalCardViews(flipOpen)
                            }
                        }
                    )
                }
            }
        )
    }

    protected fun createFlipAnimationList(flipOpen: Boolean): List<FlipAnimation<CardView>> {

        val hand = rootService.currentGame.players[playerOfThisPane].hand
        val flipAnimationList = mutableListOf<FlipAnimation<CardView>>()

        for (i in handCardViewsForAnimation.indices) {
            val flipAnimation = FlipAnimation(
                gameComponentView = handCardViewsForAnimation[i],
                fromVisual = if (flipOpen) cardImageLoader.backImage else cardImageLoader.frontImageFor(hand[i].suit, hand[i].value),
                toVisual = if (flipOpen) cardImageLoader.frontImageFor(hand[i].suit, hand[i].value) else cardImageLoader.backImage,
                duration = FLIP_ANIMATION_DURATION
            )
            flipAnimationList.add(flipAnimation)
        }

        return flipAnimationList
    }

    /**
     * Show cardViews for normal gameplay.
     */
    protected fun showNormalCardViews(flipOpen: Boolean) {
        refreshCardContent()
        refreshCardSide()

        val numOfCardsInHand = if (flipOpen) 5 else 4
        for (i in 0 until numOfCardsInHand) {
            handCardViews[i].apply {
                isVisible = true
                toBack()
            }
        }
    }

    protected fun addCardViewsForAnimation(flipOpen: Boolean) {
        val numOfCardsInHand = if (flipOpen) 5 else 4
        for (i in 0 until numOfCardsInHand) {
            handCardViewsForAnimation.add(createCardView(i))
            handCardViewsForAnimation[i].apply {

                if(flipOpen) {
                    showBack()
                    isVisible = true
                } else {
                    val hand = rootService.currentGame.players[playerOfThisPane].hand
                    frontVisual = cardImageLoader.frontImageFor(hand[i].suit, hand[i].value)
                    showFront()
                    isVisible = true
                }
            }
        }
        addAll(handCardViewsForAnimation)
        handCardViewsForAnimation.forEach {it.toFront()}
    }

    protected fun removeCardViewsForAnimation() {
        removeAll(handCardViewsForAnimation)
        handCardViewsForAnimation.clear()
    }
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Refreshes the front visual for each cardView,
     * if the player only has 4 hand cards, hide the 5th cardView.
     */
    protected fun refreshCardContent() {

        val numOfCards = rootService.currentGame.players[playerOfThisPane].hand.size
        val handCards = rootService.currentGame.players[playerOfThisPane].hand

        for (i in 0 until numOfCards) {
            handCardViews[i].apply {
                val card = handCards[i]
                frontVisual = cardImageLoader.frontImageFor(card.suit, card.value)
            }
        }

        // The 5th cardView is only visible, when the player has 5 hand cards.
        handCardViews[4].isVisible = (numOfCards == 5)
    }

    /**
     * Refreshes the card side of all cardViews based on cardsRevealed.
     */
    protected fun refreshCardSide() {
        for (cardView in handCardViews) {
            if (cardsRevealed) cardView.showFront() else cardView.showBack()
        }
    }

    /**
     * Refreshes the visual of the button.
     */
    protected fun refreshButton() {

        if (gameScene.state == UIState.TURN_READY_START) {
            button.visual = BUTTON_DEFAULT_VISUAL
            button.text = "START TURN"
            button.isVisible = true
        }

        if (gameScene.state == UIState.HAS_SELECTED) {
            button.visual = RED_BTN_VISUAL
            button.text = "DISCARD"
            button.isVisible = true

        }

        if (gameScene.state in setOf(UIState.HAS_DISCARDED, UIState.HAS_PLAYED)) {
            button.visual = BUTTON_DEFAULT_VISUAL
            button.text = "END TURN"
            button.isVisible = true
        }

        if (rootService.currentGame.currentPlayer != playerOfThisPane) {
            button.isVisible = false
        }
    }

    /**
     * Refreshes labels displaying player's score and name.
     */
    protected fun refreshPlayerLable() {
        val currentPlayer = rootService.currentGame.players[playerOfThisPane]
        val playerName = currentPlayer.name
        val playerScore = currentPlayer.score.toString()
        playerLabel1.text = "SCORE: $playerScore"
        playerLabel2.text = "PLAYER: $playerName"
    }

    protected fun refreshPaneComponents() {
        refreshCardContent()
        refreshCardSide()
        refreshButton()
        refreshPlayerLable()
    }
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Creates a cardView for this pane.
     * @param index Index of the CardView in hand.
     */
    protected fun createCardView(index: Int): CardView {
        val cardView = CardView(
            posX = HORIZ_DIS * index,
            posY = 0,
            width = CARD_WIDTH,
            height = CARD_HEIGHT,
            front = cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE),
            back = cardImageLoader.backImage,
        )
        return cardView
    }
    //------------------------------------------------------------------------------------------------------------------
    override fun refreshAfterStartNewGame() {
        cardsRevealed = false
        hideIndicator()
        refreshPaneComponents()
    }

    override fun refreshAfterPlayCard() {
        refreshPaneComponents()
        hideIndicator()
    }

    override fun refreshAfterDiscardCard() {
        refreshPaneComponents()
        hideIndicator()
    }

    override fun refreshAfterStartTurn() {}
    override fun refreshAfterDestroyCard() = refreshPaneComponents()
    override fun refreshAfterEndTurn() = refreshPaneComponents()
    override fun refreshAfterEndGame() = refreshPaneComponents()
}