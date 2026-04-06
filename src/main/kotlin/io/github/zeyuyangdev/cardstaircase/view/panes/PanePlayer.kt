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

    protected var cardsRevealed: Boolean = false

    protected val handCardViews: MutableList<CardView> = mutableListOf()
    protected val handCardViewsForAnimation: MutableList<CardView> = mutableListOf()

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
                playFlipOpenAnimation()
            }

            // 2. After a card is matched or discarded:
            // Condition 2 must be placed before condition 3,
            // otherwise 2 will be automatically fulfilled.
            if (gameScene.state in setOf(UIState.HAS_DISCARDED, UIState.HAS_PLAYED)) {
                cardsRevealed = false
                gameScene.state = UIState.TURN_READY_START
                playerActionService.endTurn()
                playFlipCloseAnimation()
            }

            // 3. After the player has selected a card from hand:
            if (gameScene.state == UIState.HAS_SELECTED) {
                gameScene.state = UIState.HAS_DISCARDED
                playerActionService.discardCard(gameScene.cardSelected!!)
                gameScene.cardSelected = null
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    init {

        for (i in 0 until 5) {
            handCardViews.add(createCardView(i))
        }

        for (i in 0 until 5) {
            handCardViewsForAnimation.add(createCardView(i))
            handCardViewsForAnimation[i].apply {
                isVisible = false
            }
        }

        // 定义每一个CardView的按钮行为
        for (i in handCardViews.indices) {
            handCardViews[i].apply {
                onMouseClicked = {

                    if (gameScene.state in setOf(UIState.TURN_STARTED, UIState.HAS_DESTROYED, UIState.HAS_SELECTED)) {
                        val handCards = rootService.currentGame.players[playerOfThisPane].hand
                        gameScene.cardSelected = handCards[i]
                        gameScene.state = UIState.HAS_SELECTED
                    }
                    refreshButton()

                }
            }
        }

        addAll(handCardViews)
        addAll(button)
        addAll(playerLabel1, playerLabel2)
        addAll(handCardViewsForAnimation)
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
            button.visual = ColorVisual(255, 55, 55, 0.5)
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

    //------------------------------------------------------------------------------------------------------------------
    protected fun playFlipOpenAnimation() {

        val flipAnimationList = mutableListOf<FlipAnimation<CardView>>()

        val hand = rootService.currentGame.players[playerOfThisPane].hand

        // 关闭原来的CardViews，启动CardViewsForAnimation
        for (cardView in handCardViewsForAnimation) {cardView.isVisible = true}
        for (cardView in handCardViews) {cardView.isVisible = false}

        for (i in handCardViewsForAnimation.indices) {
            val flipOpenAnimation = FlipAnimation(
                gameComponentView = handCardViewsForAnimation[i],
                fromVisual = cardImageLoader.backImage,
                toVisual = cardImageLoader.frontImageFor(hand[i].suit, hand[i].value),
                duration = 500
            ).apply {
                onFinished = {
                    // refreshCardSide()
                }
            }
            flipAnimationList.add(flipOpenAnimation)
        }

        var animationsFinished = 0
        for (animation in flipAnimationList) {
            animation.onFinished = {
                animationsFinished++
                if (animationsFinished == flipAnimationList.size) {
                    // 关闭CardViewsForAnimation，启动原来的CardViews
                    for (cardView in handCardViews) {cardView.isVisible = true}
                    for (cardView in handCardViewsForAnimation) {cardView.isVisible = false}
                    refreshCardContent()
                    refreshCardSide()


                }
            }
        }

        for (animation in flipAnimationList) {
            gameScene.playAnimation(animation)
        }
    }

    protected fun playFlipCloseAnimation() {

        val flipAnimationList = mutableListOf<FlipAnimation<CardView>>()

        val hand = rootService.currentGame.players[playerOfThisPane].hand

        // 关闭原来的CardViews，启动CardViewsForAnimation
        for (cardView in handCardViewsForAnimation) {cardView.isVisible = true}
        for (cardView in handCardViews) {cardView.isVisible = false}
        handCardViewsForAnimation[4].isVisible = false // 让第5张卡在翻转完成后再显示

        for (i in handCardViewsForAnimation.indices) {
            val flipOpenAnimation = FlipAnimation(
                gameComponentView = handCardViewsForAnimation[i],
                fromVisual = cardImageLoader.frontImageFor(hand[i].suit, hand[i].value),
                toVisual = cardImageLoader.backImage,
                duration = 500
            ).apply {
                onFinished = {
                    // refreshCardSide()
                }
            }
            flipAnimationList.add(flipOpenAnimation)
        }

        var animationsFinished = 0
        for (animation in flipAnimationList) {
            animation.onFinished = {
                animationsFinished++
                if (animationsFinished == flipAnimationList.size) {
                    // 关闭CardViewsForAnimation，启动原来的CardViews
                    for (cardView in handCardViews) {cardView.isVisible = true}
                    for (cardView in handCardViewsForAnimation) {cardView.isVisible = false}
                    refreshCardContent()
                    refreshCardSide()

                }
            }
        }

        for (animation in flipAnimationList) {
            gameScene.playAnimation(animation)
        }

    }

    //------------------------------------------------------------------------------------------------------------------
    override fun refreshAfterStartNewGame() {
        cardsRevealed = false
        refreshCardContent()
        refreshCardSide()
        refreshButton()
        refreshPlayerLable()

    }

    override fun refreshAfterStartTurn() {
        // refreshCardContent()
        // refreshCardSide()
        refreshButton()
        refreshPlayerLable()
    }

    override fun refreshAfterDestroyCard() {
        refreshCardContent()
        refreshCardSide()
        refreshButton()
        refreshPlayerLable()
    }

    override fun refreshAfterPlayCard() {
        refreshCardContent()
        refreshCardSide()
        refreshButton()
        refreshPlayerLable()
    }

    override fun refreshAfterDiscardCard() {
        refreshCardContent()
        refreshCardSide()
        refreshButton()
        refreshPlayerLable()
    }

    override fun refreshAfterEndTurn() {
        // refreshCardContent()
        // refreshCardSide()
        refreshButton()
        refreshPlayerLable()
    }

    override fun refreshAfterEndGame() {
        refreshCardContent()
        refreshCardSide()
        refreshButton()
        refreshPlayerLable()
    }

}