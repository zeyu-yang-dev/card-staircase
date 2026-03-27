package io.github.zeyu.staircasecardgame.view.panes

import io.github.zeyu.staircasecardgame.entity.*
import io.github.zeyu.staircasecardgame.view.*
import io.github.zeyu.staircasecardgame.view.GameScene.State

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import io.github.zeyu.staircasecardgame.service.Refreshable
import io.github.zeyu.staircasecardgame.service.RootService

import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.animation.FlipAnimation


// Pane properties
const val POS_X_2 = POS_X * 3 + CARD_WIDTH * 10 + DIS_BET_CARDS * 8

class PanePlayer2(private val rootService: RootService, private val gameScene: GameScene) : Pane<ComponentView>(
    POS_X_2, POS_Y, WIDTH, HEIGHT, visual = ColorVisual.TRANSPARENT), Refreshable {

    private val cardImageLoader = CardImageLoader()
    private val playerActionService = rootService.playerActionService

    private var cardsRevealed: Boolean = false
    private val playerOfThisPane: Int = 1

    val handCardViews: MutableList<CardView> = mutableListOf()
    val handCardViewsForAnimation: MutableList<CardView> = mutableListOf()

    /**
     * Creates a Cardview for this pane.
     * @param index Index of the CardView in hand.
     */
    private fun createCardView(index: Int): CardView {
        val cardView = CardView(
            posX = HORIZ_DIS * index,
            posY = 0,
            width = CARD_WIDTH,
            height = CARD_HEIGHT,
            front = cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE),
            back = cardImageLoader.backImage,
        ).apply {
            // this.showFront()
        }
        return cardView
    }





    private val playerLabel1: Label = Label(
        posX = PLAYER_LABEL_POS_X,
        posY = PLAYER_LABEL_1_POS_Y,
        width = PLAYER_LABEL_WIDTH,
        height = PLAYER_LABEL_HEIGHT,
        text = "",
        font = Font(size = 20, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.NORMAL),
        alignment = Alignment.CENTER,
        visual = ColorVisual(55, 55, 55, 0.5),
    )

    private val playerLabel2: Label = Label(
        posX = PLAYER_LABEL_POS_X,
        posY = PLAYER_LABEL_2_POS_Y,
        width = PLAYER_LABEL_WIDTH,
        height = PLAYER_LABEL_HEIGHT,
        text = "",
        font = Font(size = 20, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.NORMAL),
        alignment = Alignment.CENTER,
        visual = ColorVisual(55, 55, 55, 0.5),
    )





    val button = Button(
        width = BUTTON_WIDTH,
        height = BUTTON_HEIGHT,
        posX = BUTTON_POS_X,
        posY = BUTTON_POS_Y,
        text = "START TURN",
        font = Font(size = 24, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.ITALIC),
        visual = ColorVisual(55, 55, 55, 0.5)
    ).apply {
        onMouseClicked = {
            // 当回合准备开始时
            if (gameScene.state == State.TURN_READY_START) {
                cardsRevealed = true
                gameScene.state = State.TURN_STARTED
                playerActionService.startTurn()
                this.isVisible = false
                gameScene.cardSelected = null

                playFlipOpenAnimation()
            }

            // 当已经打出一张牌或已经弃掉一张牌后
            // 不能把这一段放在下一段后面，否则这一段的点击会自动触发
            if (gameScene.state in setOf(State.HAS_DISCARDED, State.HAS_PLAYED)) {

                // 牌序很重要，需要先确定是否揭示再刷新
                cardsRevealed = false // 测试
                gameScene.state = State.TURN_READY_START
                playerActionService.endTurn()

                playFlipCloseAnimation()
            }

            // 当已经选择了一张卡牌时
            if (gameScene.state == State.HAS_SELECTED) {
                gameScene.state = State.HAS_DISCARDED
                playerActionService.discardCard(gameScene.cardSelected!!)
                gameScene.cardSelected = null
            }




        }
    }


    // 刷新按钮的外观
    private fun refreshButton() {

        if (gameScene.state == State.TURN_READY_START) {
            button.visual = ColorVisual(55, 55, 55, 0.5)
            button.text = "START TURN"
            button.isVisible = true
        }


        if (gameScene.state == State.HAS_SELECTED) {
            button.visual = ColorVisual(255, 55, 55, 0.5)
            button.text = "DISCARD"
            button.isVisible = true

        }

        if (gameScene.state in setOf(State.HAS_DISCARDED, State.HAS_PLAYED)) {
            button.visual = ColorVisual(55, 55, 55, 0.5)
            button.text = "END TURN"
            button.isVisible = true
        }




        if (rootService.currentGame.currentPlayer != playerOfThisPane) {
            button.isVisible = false
        }
    }

    // 刷新玩家名和分数显示
    private fun refreshPlayerLable() {
        val currentPlayer = rootService.currentGame.players[playerOfThisPane]
        val playerName = currentPlayer.name
        val playerScore = currentPlayer.score.toString()
        playerLabel1.text = "SCORE: $playerScore"
        playerLabel2.text = "PLAYER: $playerName"
    }





    // 刷新 卡牌内容 + CardView可见性
    private fun refreshCardContent() {

        val numOfCards = rootService.currentGame.players[playerOfThisPane].hand.size
        val handCards = rootService.currentGame.players[playerOfThisPane].hand

        for (i in 0 until numOfCards) {
            handCardViews[i].apply {
                handCards[i].let { handCard ->
                    this.frontVisual = cardImageLoader.frontImageFor(handCard.suit, handCard.value)
                    this.isVisible = true
                }
            }
        }

        // 如果实际只有4张手牌，第5个CardView不可见
        for (i in numOfCards until 5) {
            handCardViews[i].isVisible = false
        }
    }

    // 刷新卡牌是否翻开
    private fun refreshCardSide() {
        for (cardView in handCardViews) {
            if (cardsRevealed) cardView.showFront() else cardView.showBack()
        }
    }

    private fun playFlipOpenAnimation() {

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





    private fun playFlipCloseAnimation() {

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

                    if (gameScene.state in setOf(State.TURN_STARTED, State.HAS_DESTROYED, State.HAS_SELECTED)) {
                        val handCards = rootService.currentGame.players[playerOfThisPane].hand
                        gameScene.cardSelected = handCards[i]
                        gameScene.state = State.HAS_SELECTED
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