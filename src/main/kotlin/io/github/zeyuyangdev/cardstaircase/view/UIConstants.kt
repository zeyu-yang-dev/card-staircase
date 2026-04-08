package io.github.zeyuyangdev.cardstaircase.view

import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

const val SCREEN_WIDTH = 1920
const val SCREEN_HEIGHT = 1080

// MainMenuScene:
val GREEN_BTN_VISUAL = ColorVisual(55, 255, 55, 0.5)
val RED_BTN_VISUAL = ColorVisual(255, 55, 55, 0.5)
const val TEXT_FIELD_WIDTH = 300
const val TEXT_FIELD_HEIGHT = 75
const val TEXT_FIELD_POS_X = (SCREEN_WIDTH - TEXT_FIELD_WIDTH) / 2
const val MMS_TEXT_FIELD_POS_Y = 450
const val TEXT_FIELD_VERTICAL_DIS = TEXT_FIELD_HEIGHT * 1.3

val MMS_TEXT_FONT = Font(
    size = 24,
    color = Color.WHITE,
    fontWeight = Font.FontWeight.SEMI_BOLD,
    fontStyle = Font.FontStyle.NORMAL
)
val TEXT_FIELD_VISUAL = ColorVisual(55, 55, 55, 0.5)

const val MMS_TITLE_SCALE = 0.5
const val MMS_TITLE_WIDTH = 1008 * MMS_TITLE_SCALE
const val MMS_TITLE_HEIGHT = 304 * MMS_TITLE_SCALE
const val MMS_TITLE_POS_X = (SCREEN_WIDTH - MMS_TITLE_WIDTH) / 2
const val MMS_TITLE_POS_Y = 230

// PanePlayer, common properties:
const val DELAY_FOR_FLICKER_REMOVAL = 10
const val FLIP_ANIMATION_DURATION = 600
val PANE_BG_VISUAL = ColorVisual(255, 55, 55, 0.3)
const val CARDS_CALE = 0.75
const val CARD_WIDTH = 130 * CARDS_CALE
const val CARD_HEIGHT = 200 * CARDS_CALE

const val DIS_BET_CARDS = 5
const val HORIZ_DIS = CARD_WIDTH + DIS_BET_CARDS
const val VERTIC_DIS = CARD_HEIGHT + DIS_BET_CARDS

const val BUTTON_WIDTH = CARD_WIDTH * 2 + DIS_BET_CARDS * 2
const val BUTTON_HEIGHT = BUTTON_WIDTH / 4
const val BUTTON_POS_X = ((CARD_WIDTH * 5 + DIS_BET_CARDS * 4) - BUTTON_WIDTH) / 2
const val BUTTON_POS_Y = -(BUTTON_HEIGHT + DIS_BET_CARDS * 3)
val BUTTON_FONT = Font(
    size = 24,
    color = Color.WHITE,
    fontWeight = Font.FontWeight.SEMI_BOLD,
    fontStyle = Font.FontStyle.ITALIC
)
val BUTTON_DEFAULT_VISUAL = ColorVisual(55, 55, 55, 0.5)

const val PLAYER_LABEL_WIDTH = CARD_WIDTH * 3 + DIS_BET_CARDS * 2
const val PLAYER_LABEL_HEIGHT = CARD_HEIGHT / 4
const val PLAYER_LABEL_POS_X = CARD_WIDTH + DIS_BET_CARDS
// LABEL 1 for score, LABEL_2 for name
const val PLAYER_LABEL_1_POS_Y = -(-BUTTON_POS_Y + DIS_BET_CARDS * 3 + PLAYER_LABEL_HEIGHT)
const val PLAYER_LABEL_2_POS_Y = -(-BUTTON_POS_Y + DIS_BET_CARDS * 3 + PLAYER_LABEL_HEIGHT * 2)
val PLAYER_LABEL_FONT = Font(
    size = 20,
    color = Color.WHITE,
    fontWeight = Font.FontWeight.SEMI_BOLD,
    fontStyle = Font.FontStyle.NORMAL
)
val PLAYER_LABEL_VISUAL = ColorVisual(55, 55, 55, 0.5)

const val INDICATOR_WIDTH = DIS_BET_CARDS * 3
const val INDICATOR_HEIGHT = INDICATOR_WIDTH
const val INDICATOR_POS_X = CARD_WIDTH / 2 - INDICATOR_WIDTH / 2
const val INDICATOR_POS_Y = -INDICATOR_HEIGHT

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
const val STAIRS_HEIGHT = CARD_HEIGHT * 5 + DIS_BET_CARDS * 4
const val STAIRS_POS_X = (SCREEN_WIDTH - CARD_WIDTH * 5 - DIS_BET_CARDS * 4) / 2
const val STAIRS_POS_Y = SCREEN_HEIGHT - DIS_TO_BOTTOM - CARD_HEIGHT * 5 - DIS_BET_CARDS * 4
// Relative coordinate of the top-left of the card on the bottom-left
const val BOTT_LEFT_OFFSET_X = 0
const val BOTT_LEFT_OFFSET_Y = CARD_HEIGHT * 4 + DIS_BET_CARDS * 4

// PaneStacks:
const val STACKS_WIDTH = CARD_WIDTH * 2 + DIS_BET_CARDS
const val STACKS_HEIGHT = CARD_HEIGHT
const val STACKS_POS_X = PPL_POS_X + (CARD_WIDTH * 3 + DIS_BET_CARDS * 3) / 2
const val STACKS_POS_Y = DIS_TO_BOTTOM + 15

const val GAME_LOG_SCALE = 1.25
const val GAME_LOG_WIDTH = BUTTON_WIDTH * GAME_LOG_SCALE
const val GAME_LOG_HEIGHT = BUTTON_HEIGHT * GAME_LOG_SCALE
const val GAME_LOG_POS_X = PPR_POS_X + (CARD_WIDTH * 5 + DIS_BET_CARDS * 4 - GAME_LOG_WIDTH) / 2 - STACKS_POS_X
const val GAME_LOG_POS_Y = 0

val GAME_LOG_LIST_FONT = Font(
    size = 16,
    color = Color.WHITE,
    fontWeight = Font.FontWeight.NORMAL,
    fontStyle = Font.FontStyle.NORMAL
)

// ResultMenuScene:
const val RMS_WIDTH = 600
const val RMS_HEIGHT = RMS_WIDTH
const val RMS_MIDDLE_LINE = RMS_HEIGHT / 2
const val RMS_DIS_TO_MIDDLE_LINE = 20

const val RMS_LIST_WIDTH = RMS_WIDTH * 0.2
const val RMS_LIST_HEIGHT = 100
val RMS_LIST_BG_VISUAL = ColorVisual.TRANSPARENT
val RMS_LIST_FONT = Font(
    size = 18,
    color = Color.WHITE,
    fontWeight = Font.FontWeight.SEMI_BOLD,
    fontStyle = Font.FontStyle.NORMAL
)

const val RMS_LIST_HORIZ_SPACER = 20
const val RMS_NAME_LIST_POS_X = (RMS_WIDTH - RMS_LIST_WIDTH * 2 - RMS_LIST_HORIZ_SPACER) / 2
const val RMS_SCORE_LIST_POS_X = RMS_NAME_LIST_POS_X + RMS_LIST_WIDTH + RMS_LIST_HORIZ_SPACER
const val RMS_LIST_POS_Y = RMS_MIDDLE_LINE + RMS_DIS_TO_MIDDLE_LINE

const val RMS_BTN_WIDTH = RMS_LIST_WIDTH
const val RMS_BTN_HEIGHT = RMS_LIST_HEIGHT / 3
const val RMS_REPLAY_BTN_POS_X = RMS_NAME_LIST_POS_X
const val RMS_EXIT_BTN_POS_X = RMS_SCORE_LIST_POS_X
const val RMS_BTN_POS_Y = RMS_LIST_POS_Y + RMS_LIST_HEIGHT + 30
val RMS_BTN_FONT = Font(
    size = 18,
    color = Color.WHITE,
    fontWeight = Font.FontWeight.SEMI_BOLD,
    fontStyle = Font.FontStyle.NORMAL
)
val WINNER_NAME_FONT = Font(
    size = 26,
    color = Color.WHITE,
    fontWeight = Font.FontWeight.SEMI_BOLD,
    fontStyle = Font.FontStyle.NORMAL
)
const val WINNER_NAME_WIDTH = RMS_WIDTH * 0.25
const val WINNER_NAME_HEIGHT = WINNER_NAME_WIDTH / 4
const val WINNER_NAME_POS_X = (RMS_WIDTH - WINNER_NAME_WIDTH) / 2
const val WINNER_NAME_POS_Y = RMS_MIDDLE_LINE - RMS_DIS_TO_MIDDLE_LINE - WINNER_NAME_HEIGHT

const val WINNER_TITLE_WIDTH = RMS_WIDTH * 0.4
const val WINNER_TITLE_HEIGHT = WINNER_TITLE_WIDTH / 3
const val WINNER_TITLE_POS_X = (RMS_WIDTH - WINNER_TITLE_WIDTH) / 2 + WINNER_TITLE_WIDTH * 0.025
const val WINNER_TITLE_POS_Y = WINNER_NAME_POS_Y - WINNER_TITLE_HEIGHT + WINNER_TITLE_WIDTH * 0.025






