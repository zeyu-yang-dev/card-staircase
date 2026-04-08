package io.github.zeyuyangdev.cardstaircase.view

import io.github.zeyuyangdev.cardstaircase.service.RootService
import io.github.zeyuyangdev.cardstaircase.service.Refreshable

import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Color



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
        // alignment = Alignment.TOP_CENTER,
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
        // alignment = Alignment.TOP_CENTER,
        visual = RMS_LIST_BG_VISUAL,
        orientation = Orientation.VERTICAL
    )



    val replayButton = Button(
        width = RMS_BTN_WIDTH,
        height = RMS_BTN_HEIGHT,
        posX = RMS_REPLAY_BTN_POS_X,
        posY = RMS_BTN_POS_Y,
        text = "REPLAY",
        font = Font(size = 24, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.NORMAL),
        visual = ColorVisual(55, 255, 55, 0.5)
    )

    val exitButton = Button(
        width = RMS_BTN_WIDTH,
        height = RMS_BTN_HEIGHT,
        posX = RMS_EXIT_BTN_POS_X,
        posY = RMS_BTN_POS_Y,
        text = "EXIT",
        font = Font(size = 24, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.NORMAL),
        visual = ColorVisual(255, 55, 55, 0.5)
    )









    private fun refreshScoreBoard(indexOfWinner: Int = 0) {
        val currentGame = rootService.currentGame
        val winner = currentGame.players[indexOfWinner]
        val loser = currentGame.players[(indexOfWinner + 1) % 2]

        nameList.items.setAll(
            listOf(
                "PLAYER",
                winner.name,
                loser.name
            )
        )

        scoreList.items.setAll(
            listOf(
                "SCORE",
                winner.score.toString(),
                loser.score.toString()
            )
        )
    }




    init {

        this.background = ImageVisual("end_background.png")

        addComponents(
            nameList,
            scoreList,
            replayButton,
            exitButton,

        )


    }




    override fun refreshAfterEndGame() {
        val currentGame = rootService.currentGame

        if (currentGame.players[0].score >= currentGame.players[1].score) {
            refreshScoreBoard(0)
        }
        else {
            refreshScoreBoard(1)
        }
    }





}