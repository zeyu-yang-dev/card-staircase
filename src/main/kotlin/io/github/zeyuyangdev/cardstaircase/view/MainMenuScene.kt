package io.github.zeyuyangdev.cardstaircase.view

import io.github.zeyuyangdev.cardstaircase.service.Refreshable
import io.github.zeyuyangdev.cardstaircase.service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class MainMenuScene(
    private val rootService: RootService
) : MenuScene(
    SCREEN_WIDTH,
    SCREEN_HEIGHT
), Refreshable {

    private val player1TextField: TextField = TextField(
        posX = TEXT_FIELD_POS_X,
        posY = MMS_TEXT_FIELD_POS_Y,
        width = TEXT_FIELD_WIDTH,
        height = TEXT_FIELD_HEIGHT,
        text = "Martin",
        prompt = "Enter player 1 name.",
        font = MMS_TEXT_FONT,
        visual = TEXT_FIELD_VISUAL,
    )

    private val player2TextField: TextField = TextField(
        posX = TEXT_FIELD_POS_X,
        posY = MMS_TEXT_FIELD_POS_Y + TEXT_FIELD_VERTICAL_DIS * 1,
        width = TEXT_FIELD_WIDTH,
        height = TEXT_FIELD_HEIGHT,
        text = "Erich",
        prompt = "Enter player 2 name.",
        font = MMS_TEXT_FONT,
        visual = TEXT_FIELD_VISUAL,
    )

    val startGameButton = Button(
        posX = TEXT_FIELD_POS_X,
        posY = MMS_TEXT_FIELD_POS_Y + TEXT_FIELD_VERTICAL_DIS * 2,
        width = TEXT_FIELD_WIDTH,
        height = TEXT_FIELD_HEIGHT,
        text = "START",
        font = MMS_TEXT_FONT,
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
        posY = MMS_TEXT_FIELD_POS_Y + TEXT_FIELD_VERTICAL_DIS * 3,
        width = TEXT_FIELD_WIDTH,
        height = TEXT_FIELD_HEIGHT,
        text = "EXIT",
        font = MMS_TEXT_FONT,
        visual = ColorVisual(255, 55, 55, 0.5)
    )

    init {
        this.background = ImageVisual("start_background.png")

        addComponents(player1TextField, player2TextField, startGameButton, exitGameButton)


    }
}
