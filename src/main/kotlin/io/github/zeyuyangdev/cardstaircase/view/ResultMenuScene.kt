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
import tools.aqua.bgw.core.Color



class ResultMenuScene(
    private val rootService: RootService
) : MenuScene(
    SCREEN_WIDTH,
    SCREEN_HEIGHT
), Refreshable {

    private val winnerLabel: ListView<String> = ListView(
        posX = WINNER_LABEL_POS_X,
        posY = WINNER_LABEL_POS_Y,
        width = WINNER_LABLE_WIDTH,
        height = WINNER_LABLE_HEIGHT,
        items = emptyList(),
        font = Font(size = 30, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.NORMAL),
        // alignment = Alignment.TOP_CENTER,
        visual = ColorVisual(55, 55, 55, 0.5),
        orientation = Orientation.VERTICAL,
    )

    private val loserLabel: ListView<String> = ListView(
        posX = LOSER_LABEL_POS_X,
        posY = WINNER_LABEL_POS_Y,
        width = WINNER_LABLE_WIDTH,
        height = WINNER_LABLE_HEIGHT,
        items = emptyList(),
        font = Font(size = 30, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.NORMAL),
        // alignment = Alignment.TOP_CENTER,
        visual = ColorVisual(55, 55, 55, 0.5),
        orientation = Orientation.VERTICAL,
    )

    val replayButton = Button(
        width = WINNER_LABLE_WIDTH,
        height = VIC_BUTTON_HEIGHT,
        posX = WINNER_LABEL_POS_X,
        posY = VIC_BUTTON_POS_Y,
        text = "REPLAY",
        font = Font(size = 24, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.NORMAL),
        visual = ColorVisual(55, 255, 55, 0.5)
    ).apply {
        onMouseClicked = {

        }
    }

    val exitButton = Button(
        width = WINNER_LABLE_WIDTH,
        height = VIC_BUTTON_HEIGHT,
        posX = LOSER_LABEL_POS_X,
        posY = VIC_BUTTON_POS_Y,
        text = "EXIT",
        font = Font(size = 24, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.NORMAL),
        visual = ColorVisual(255, 55, 55, 0.5)
    ).apply {
        onMouseClicked = {

        }
    }









    private fun refreshResult(indexOfWinner: Int = 0) {
        val currentGame = rootService.currentGame

        winnerLabel.items.setAll(
            listOf(
                "WINNER",
                currentGame.players[indexOfWinner].name,
                "SCORE: ${currentGame.players[indexOfWinner].score}"
            )
        )

        val indexOfLoser = (indexOfWinner + 1) % 2

        loserLabel.items.setAll(
            listOf(
                "LOSER",
                currentGame.players[indexOfLoser].name,
                "SCORE: ${currentGame.players[indexOfLoser].score}"
            )
        )

        // loserLabel.items.setAll(
        //     currentGame.gameLog
        // )
    }




    init {

        this.background = ImageVisual("end_background.png")

        addComponents(winnerLabel)
        addComponents(loserLabel)
        addComponents(replayButton)
        addComponents(exitButton)
    }




    override fun refreshAfterEndGame() {
        val currentGame = rootService.currentGame

        if (currentGame.players[0].score >= currentGame.players[1].score) {
            refreshResult(0)
        }
        else {
            refreshResult(1)
        }
    }





}