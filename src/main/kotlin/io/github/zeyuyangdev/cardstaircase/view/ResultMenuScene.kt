package io.github.zeyuyangdev.cardstaircase.view

import io.github.zeyuyangdev.cardstaircase.service.RootService
import io.github.zeyuyangdev.cardstaircase.service.Refreshable

import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label

/**
 * The menu scene showing the game result.
 */
class ResultMenuScene(
    private val rootService: RootService
) : MenuScene(
    RMS_WIDTH,
    RMS_HEIGHT
), Refreshable {

    private val nameList = ListView<String>(
        width = RMS_LIST_WIDTH,
        height = RMS_LIST_HEIGHT,
        posX = RMS_NAME_LIST_POS_X,
        posY = RMS_LIST_POS_Y,
        items = emptyList(),
        font = RMS_LIST_FONT,
        visual = RMS_LIST_BG_VISUAL,
        orientation = Orientation.VERTICAL
    )

    private val scoreList = ListView<String>(
        width = RMS_LIST_WIDTH,
        height = RMS_LIST_HEIGHT,
        posX = RMS_SCORE_LIST_POS_X,
        posY = RMS_LIST_POS_Y,
        items = emptyList(),
        font = RMS_LIST_FONT,
        visual = RMS_LIST_BG_VISUAL,
        orientation = Orientation.VERTICAL
    )

    val replayButton = Button(
        width = RMS_BTN_WIDTH,
        height = RMS_BTN_HEIGHT,
        posX = RMS_REPLAY_BTN_POS_X,
        posY = RMS_BTN_POS_Y,
        text = "REPLAY",
        font = RMS_BTN_FONT,
        visual = GREEN_BTN_VISUAL
    )

    val exitButton = Button(
        width = RMS_BTN_WIDTH,
        height = RMS_BTN_HEIGHT,
        posX = RMS_EXIT_BTN_POS_X,
        posY = RMS_BTN_POS_Y,
        text = "EXIT",
        font = RMS_BTN_FONT,
        visual = RED_BTN_VISUAL
    )

    val winnerName = Label(
        width = WINNER_NAME_WIDTH,
        height = WINNER_NAME_HEIGHT,
        posX = WINNER_NAME_POS_X,
        posY = WINNER_NAME_POS_Y,
        font = WINNER_NAME_FONT,
        visual = RMS_LIST_BG_VISUAL
    )

    val winnerTitle = Label(
        width = WINNER_TITLE_WIDTH,
        height = WINNER_TITLE_HEIGHT,
        posX = WINNER_TITLE_POS_X,
        posY = WINNER_TITLE_POS_Y,
        visual = ImageVisual("winner_title.png")
    )

    val gameEndLabel = Label(
        width = GAME_END_LABEL_WIDTH,
        height = GAME_END_LABEL_HEIGHT,
        posX = GAME_END_LABEL_POS_X,
        posY = GAME_END_LABEL_POS_Y,
        visual = ImageVisual("game_end_label.png")

    )

    init {
        this.background = ImageVisual("end_background.png")

        addComponents(
            nameList,
            scoreList,
            replayButton,
            exitButton,
            winnerName,
            winnerTitle,
            gameEndLabel
        )
    }

    /**
     * Refreshes every variable component.
     */
    private fun refreshScoreBoard() {
        val players = rootService.currentGame.players
        val winner = players.maxBy { it.score }
        val loser = players.first { it != winner }

        // refreshes nameList
        nameList.items.setAll(
            listOf(
                "PLAYER",
                winner.name,
                loser.name
            )
        )

        // refreshes scoreList
        scoreList.items.setAll(
            listOf(
                "SCORE",
                winner.score.toString(),
                loser.score.toString()
            )
        )

        // refreshes name of the winner
        winnerName.text = winner.name
    }

    override fun refreshAfterEndGame() {
        refreshScoreBoard()
    }
}