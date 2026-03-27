package io.github.zeyu.staircasecardgame.view

import io.github.zeyu.staircasecardgame.entity.*
import io.github.zeyu.staircasecardgame.view.panes.*
import io.github.zeyu.staircasecardgame.service.RootService
import io.github.zeyu.staircasecardgame.service.Refreshable

import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ImageVisual

class GameScene(private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable {


    /**
     * Represents the state of a turn, mainly for UI.
     */
    enum class State {
        TURN_READY_START,
        TURN_STARTED,

        HAS_DESTROYED,

        HAS_SELECTED,
        HAS_PLAYED,
        HAS_DISCARDED,
    }

    internal var state = State.TURN_READY_START

    internal var cardSelected: Card? = null


    val panePlayer1 = PanePlayer1(rootService, this).apply {
        onMouseEntered = {
            paneStacks.gameLogListView.isVisible = false
            paneStacks.gameLogLabel.isVisible = true
        }
    }

    val panePlayer2 = PanePlayer2(rootService, this).apply {
        onMouseEntered = {
            paneStacks.gameLogListView.isVisible = false
            paneStacks.gameLogLabel.isVisible = true
        }
    }

    val paneStairs = PaneStairs(rootService, this).apply {
        onMouseEntered = {
            paneStacks.gameLogListView.isVisible = false
            paneStacks.gameLogLabel.isVisible = true
        }
    }

    val paneStacks = PaneStacks(rootService, this)

    init {


        this.background = ImageVisual("game_background.png")

        addComponents(panePlayer1)
        addComponents(panePlayer2)
        addComponents(paneStairs)
        addComponents(paneStacks)

    }

    override fun refreshAfterStartNewGame() {
        state = State.TURN_READY_START
        cardSelected = null
    }





}