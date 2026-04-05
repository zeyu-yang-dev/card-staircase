package io.github.zeyuyangdev.cardstaircase.view.panes

import io.github.zeyuyangdev.cardstaircase.entity.*
import io.github.zeyuyangdev.cardstaircase.service.Refreshable
import io.github.zeyuyangdev.cardstaircase.service.RootService
import io.github.zeyuyangdev.cardstaircase.view.*
import io.github.zeyuyangdev.cardstaircase.view.GameScene.State

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

    val cardViewsCol_1: MutableList<CardView> = mutableListOf()
    val cardViewsCol_2: MutableList<CardView> = mutableListOf()
    val cardViewsCol_3: MutableList<CardView> = mutableListOf()
    val cardViewsCol_4: MutableList<CardView> = mutableListOf()
    val cardViewsCol_5: MutableList<CardView> = mutableListOf()

    val stairCardViews = listOf(cardViewsCol_1, cardViewsCol_2, cardViewsCol_3, cardViewsCol_4, cardViewsCol_5)

    // 刷新 卡牌内容 + CardView可见性
    private fun refreshCardContent() {
        val stairs = rootService.currentGame.stairs

        // 逐个设置 卡牌内容 + 可见性
        for (col_index in stairCardViews.indices) {
            // 如果stairs中的这一个Stack里有卡，则这一列CardView可见
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
            // 否则这一列都不可见
            else {
                for (cardView in stairCardViews[col_index]) {
                    cardView.isVisible = false
                }

            }
        }
    }

    // 刷新 卡牌是否翻开 + 是否可点击，注意翻开的卡牌同时是可点击的卡牌
    private fun refreshCardSide() {
        val stairs = rootService.currentGame.stairs

        // 将所有CardView设置为背面
        for (col_index in stairCardViews.indices) {
            for (row_index in stairCardViews[col_index].indices) {
                stairCardViews[col_index][row_index].apply {
                    this.showBack()
                }
            }
        }

        // 将mask中的所有值设置为false
        for (column in stairsMask) {
            for (row_index in column.indices) {
                column[row_index] = false
            }
        }

        // 将对应stairs中栈顶的那些CardView设置为正面
        for (col_index in stairs.indices) {
            if (stairs[col_index].isNotEmpty()) {
                stairCardViews[col_index][stairs[col_index].size - 1].showFront()
            }
        }

        // 将对应stairs中栈顶的那些CardView的mask值设置为true
        for (col_index in stairs.indices) {
            if (stairs[col_index].isNotEmpty()) {
                stairsMask[col_index][stairs[col_index].size - 1] = true
            }
        }

    }





    init {

        // Add CardViews for a FULL stair
        var numOfCardsInCol = 5
        for (col in stairCardViews.indices) {
            // row from bottom to top
            for (row in 0 until numOfCardsInCol) {
                stairCardViews[col].add(createCardView(col, row))
            }
            numOfCardsInCol -= 1
        }

        // 定义每个CardView被点击时的功能
        numOfCardsInCol = 5
        for (col in stairCardViews.indices) {
            for (row in 0 until numOfCardsInCol) {
                stairCardViews[col][row].apply {
                    onMouseClicked = {
                        if (stairsMask[col][row]) {

                            val currentPlayerIndex = rootService.currentGame.currentPlayer
                            val currentPlayer = rootService.currentGame.players[currentPlayerIndex]

                            // DESTROY
                            if (gameScene.state == State.TURN_STARTED && currentPlayer.score >= 5) {
                                val stairs = rootService.currentGame.stairs
                                playerActionService.destroyCard(stairs[col][row])
                                gameScene.state = State.HAS_DESTROYED
                            }

                            // 当已经选择了一张手牌
                            if (gameScene.state == State.HAS_SELECTED) {
                                // 检查从手牌中选择的牌和阶梯中选择的牌是否匹配
                                val stairs = rootService.currentGame.stairs
                                if (gameScene.cardSelected!!.value == stairs[col][row].value || gameScene.cardSelected!!.suit == stairs[col][row].suit) {

                                    // 牌序很重要，需要先改state再刷新
                                    gameScene.state = State.HAS_PLAYED
                                    playerActionService.playCard(gameScene.cardSelected!!, stairs[col][row])

                                    gameScene.cardSelected = null
                                }


                            }

                        }
                    }
                }
            }
            numOfCardsInCol -= 1
        }



        for (i in stairCardViews.indices) {
            addAll(stairCardViews[i])
        }



    }


    override fun refreshAfterStartNewGame() {
        refreshCardContent()
        refreshCardSide()
    }

    override fun refreshAfterStartTurn() {
        refreshCardContent()
        refreshCardSide()
    }

    override fun refreshAfterDestroyCard() {
        refreshCardContent()
        refreshCardSide()
    }

    override fun refreshAfterPlayCard() {
        refreshCardContent()
        refreshCardSide()
    }


    override fun refreshAfterDiscardCard() {
        refreshCardContent()
        refreshCardSide()
    }

    override fun refreshAfterEndTurn() {
        refreshCardContent()
        refreshCardSide()
    }

    override fun refreshAfterEndGame() {
        refreshCardContent()
        refreshCardSide()
    }




}