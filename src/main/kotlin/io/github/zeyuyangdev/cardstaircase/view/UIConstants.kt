package io.github.zeyuyangdev.cardstaircase.view

import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

const val SCREEN_WIDTH = 1920
const val SCREEN_HEIGHT = 1080

// MainMenuScene
const val TEXT_FIELD_WIDTH = 300
const val TEXT_FIELD_HEIGHT = 100
const val TEXT_FIELD_POS_X = (SCREEN_WIDTH - TEXT_FIELD_WIDTH) / 2
const val MMS_TEXT_FIELD_POS_Y = 300
const val TEXT_FIELD_VERTICAL_DIS = TEXT_FIELD_HEIGHT * 1.3

const val MMS_TEXT_SIZE = 24
val MMS_TEXT_FONT = Font(
    size = MMS_TEXT_SIZE,
    color = Color.WHITE,
    fontWeight = Font.FontWeight.SEMI_BOLD,
    fontStyle = Font.FontStyle.NORMAL
)
val TEXT_FIELD_VISUAL = ColorVisual(55, 55, 55, 0.5)

// GameScene
