package io.github.zeyuyangdev.cardstaircase.view

import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

const val SCREEN_WIDTH = 1920
const val SCREEN_HEIGHT = 1080

// MainMenuScene:
const val TEXT_FIELD_WIDTH = 300
const val TEXT_FIELD_HEIGHT = 75
const val TEXT_FIELD_POS_X = (SCREEN_WIDTH - TEXT_FIELD_WIDTH) / 2
const val MMS_TEXT_FIELD_POS_Y = 450
const val TEXT_FIELD_VERTICAL_DIS = TEXT_FIELD_HEIGHT * 1.3

const val MMS_TEXT_SIZE = 24
val MMS_TEXT_FONT = Font(
    size = MMS_TEXT_SIZE,
    color = Color.WHITE,
    fontWeight = Font.FontWeight.SEMI_BOLD,
    fontStyle = Font.FontStyle.NORMAL
)
val TEXT_FIELD_VISUAL = ColorVisual(55, 55, 55, 0.5)

// PanePlayer, common properties:
val PANE_BG_VISUAL = ColorVisual(255, 55, 55, 0.5)
const val CARDS_CALE = 0.75
const val CARD_WIDTH = 130 * CARDS_CALE
const val CARD_HEIGHT = 200 * CARDS_CALE

const val DIS_BET_CARDS = 5
const val HORIZ_DIS = CARD_WIDTH + DIS_BET_CARDS
const val VERTIC_DIS = CARD_HEIGHT + DIS_BET_CARDS

const val BUTTON_WIDTH = 200
const val BUTTON_HEIGHT = BUTTON_WIDTH / 1.618 / 2
const val BUTTON_POS_X = ((CARD_WIDTH * 5 + DIS_BET_CARDS * 4) - BUTTON_WIDTH) / 2
const val BUTTON_POS_Y = -(BUTTON_HEIGHT + DIS_BET_CARDS * 3)

const val PLAYER_LABEL_WIDTH = CARD_WIDTH * 3 + DIS_BET_CARDS * 2
const val PLAYER_LABEL_HEIGHT = CARD_HEIGHT / 4
const val PLAYER_LABEL_POS_X = CARD_WIDTH + DIS_BET_CARDS
// LABEL 1 for score, LABEL_2 for name
const val PLAYER_LABEL_1_POS_Y = -(BUTTON_HEIGHT + DIS_BET_CARDS * 6 + PLAYER_LABEL_HEIGHT)
const val PLAYER_LABEL_2_POS_Y = -(BUTTON_HEIGHT + DIS_BET_CARDS * 6 + PLAYER_LABEL_HEIGHT * 2)

// PanePlayerLeft:
const val DIS_TO_BOTTOM = 60 // controls the distance to the bottom
const val PPL_POS_X = ((SCREEN_WIDTH - CARD_WIDTH * 5 - DIS_BET_CARDS * 4) / 2 - CARD_WIDTH * 5 - DIS_BET_CARDS * 4) / 2
const val PP_POS_Y = SCREEN_HEIGHT - CARD_HEIGHT - DIS_TO_BOTTOM
const val PP_WIDTH = CARD_WIDTH * 5 + DIS_BET_CARDS * 4
const val PP_HEIGHT = CARD_HEIGHT

// PanePlayerRight:
const val PPR_POS_X = PPL_POS_X * 3 + CARD_WIDTH * 10 + DIS_BET_CARDS * 8

// PaneStairs:
const val STAIRS_WIDTH = CARD_WIDTH * 5 + DIS_BET_CARDS * 4
const val STAIRS_HEIGHT = CARD_HEIGHT
const val STAIRS_POS_X = (SCREEN_WIDTH - CARD_WIDTH * 5 - DIS_BET_CARDS * 4) / 2
const val STAIRS_POS_Y = SCREEN_HEIGHT - DIS_TO_BOTTOM - CARD_HEIGHT * 5 - DIS_BET_CARDS * 4
// Relative coordinate of the top-left of the card on the bottom-left
const val BOTT_LEFT_OFFSET_X = 0
const val BOTT_LEFT_OFFSET_Y = CARD_HEIGHT * 4 + DIS_BET_CARDS * 4

// PaneStacks:
const val STACKS_POS_X = (SCREEN_WIDTH - CARD_WIDTH * 2 - DIS_BET_CARDS) / 2
const val STACKS_POS_Y = 30

const val STACKS_WIDTH = CARD_WIDTH * 2 + DIS_BET_CARDS
const val STACKS_HEIGHT = CARD_HEIGHT

const val GAME_LOG_POS_Y = (CARD_HEIGHT - BUTTON_HEIGHT) / 2 - STACKS_POS_Y







