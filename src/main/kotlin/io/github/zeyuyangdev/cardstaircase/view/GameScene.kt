package io.github.zeyuyangdev.cardstaircase.view

import io.github.zeyuyangdev.cardstaircase.entity.*
import io.github.zeyuyangdev.cardstaircase.service.RootService
import io.github.zeyuyangdev.cardstaircase.service.Refreshable
import io.github.zeyuyangdev.cardstaircase.view.panes.*

import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.visual.ColorVisual

class GameScene(
    private val rootService: RootService
) : BoardGameScene(
    SCREEN_WIDTH,
    SCREEN_HEIGHT
), Refreshable {

    /**
     * Represents the state of a turn.
     */
    enum class UIState {
        TURN_READY_START,
        TURN_STARTED,

        HAS_DESTROYED,

        HAS_SELECTED,
        HAS_PLAYED,
        HAS_DISCARDED,
    }

    internal var state = UIState.TURN_READY_START

    internal var cardSelected: Card? = null


    val panePlayerLeft = PanePlayerLeft(rootService, this).apply {
        onMouseEntered = {
            paneStacks.gameLogListView.isVisible = false
            paneStacks.gameLogLabel.isVisible = true
        }
    }

    val panePlayerRight = PanePlayerRight(rootService, this).apply {
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

    val paneStacks = PaneStacks(rootService)

    // temp button to show ResultMenuScene:
    val tempButton = Button(
        width = PLAYER_LABEL_WIDTH,
        height = 100,
        posX = PLAYER_LABEL_POS_X + PPL_POS_X,
        posY = 300,
        text = "SHOW RESUALT MENU SCENE",
        visual = ColorVisual.CYAN
    ).apply {
        isVisible = false
    }

    init {
        this.background = ImageVisual("game_background.png")

        addComponents(
            panePlayerLeft,
            panePlayerRight,
            paneStairs,
            paneStacks,
            tempButton
        )
    }

    override fun refreshAfterStartNewGame() {
        state = UIState.TURN_READY_START
        cardSelected = null
    }
}