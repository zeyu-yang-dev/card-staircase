package io.github.zeyuyangdev.staircasecardgame.view

import io.github.zeyuyangdev.staircasecardgame.service.Refreshable
import io.github.zeyuyangdev.staircasecardgame.service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual


const val TEXT_FIELD_WIDTH = 300
const val TEXT_FIELD_HEIGHT = 100
const val TEXT_FIELD_POS_X = (1920 - TEXT_FIELD_WIDTH) / 2
const val START_POS_Y = 300
const val TEXT_FIELD_VERTICAL_DIS = TEXT_FIELD_HEIGHT * 1.5

const val TEXT_SIZE = 24


class MainMenuScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {

    private val titleLabel: Label = Label(
        posX = TEXT_FIELD_POS_X - TEXT_FIELD_WIDTH,
        posY = START_POS_Y,
        width = TEXT_FIELD_WIDTH * 3,
        height = TEXT_FIELD_HEIGHT,
        text = "STAIRCASE CARD GAME",
        font = Font(size = 35, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.ITALIC),
        alignment = Alignment.CENTER,
        // visual = ColorVisual(55, 55, 55, 0.5),
    )

    private val player1TextField: TextField = TextField(
        posX = TEXT_FIELD_POS_X,
        posY = START_POS_Y + TEXT_FIELD_VERTICAL_DIS,
        width = TEXT_FIELD_WIDTH,
        height = TEXT_FIELD_HEIGHT,
        text = "",
        prompt = "Enter player 1 name.",
        font = Font(size = TEXT_SIZE, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.NORMAL),
        visual = ColorVisual(55, 55, 55, 0.5),
    )

    private val player2TextField: TextField = TextField(
        posX = TEXT_FIELD_POS_X,
        posY = START_POS_Y + TEXT_FIELD_VERTICAL_DIS * 2,
        width = TEXT_FIELD_WIDTH,
        height = TEXT_FIELD_HEIGHT,
        text = "",
        prompt = "Enter player 2 name.",
        font = Font(size = TEXT_SIZE, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.NORMAL),
        visual = ColorVisual(55, 55, 55, 0.5),
    )

    val startGameButton = Button(
        posX = TEXT_FIELD_POS_X,
        posY = START_POS_Y + TEXT_FIELD_VERTICAL_DIS * 3,
        width = TEXT_FIELD_WIDTH,
        height = TEXT_FIELD_HEIGHT,
        text = "START",
        font = Font(size = TEXT_SIZE, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.NORMAL),
        visual = ColorVisual(55, 255, 55, 0.5)
    ).apply {
        onMouseClicked = {
            val player1Name = player1TextField.text
            val player2Name = player2TextField.text

            if (player1Name.isNotEmpty() && player2Name.isNotEmpty()) {
                rootService.gameService.startNewGame(player1Name, player2Name)
            }



        }
    }

    val exitGameButton = Button(
        posX = TEXT_FIELD_POS_X,
        posY = START_POS_Y + TEXT_FIELD_VERTICAL_DIS * 4,
        width = TEXT_FIELD_WIDTH,
        height = TEXT_FIELD_HEIGHT,
        text = "EXIT",
        font = Font(size = TEXT_SIZE, color = Color.WHITE,
            fontWeight = Font.FontWeight.SEMI_BOLD, fontStyle = Font.FontStyle.NORMAL),
        visual = ColorVisual(255, 55, 55, 0.5)
    ).apply {
        onMouseClicked = {



        }
    }











    init {
        this.background = ImageVisual("start_background.png")

        addComponents(titleLabel, player1TextField, player2TextField, startGameButton, exitGameButton)


    }
}
