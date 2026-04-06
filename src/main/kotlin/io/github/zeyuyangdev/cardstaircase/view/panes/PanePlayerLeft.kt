package io.github.zeyuyangdev.cardstaircase.view.panes

import io.github.zeyuyangdev.cardstaircase.view.*
import io.github.zeyuyangdev.cardstaircase.service.RootService

class PanePlayerLeft(
    rootService: RootService,
    gameScene: GameScene
) : PanePlayer(
    rootService,
    gameScene,
    0,
    PPL_POS_X,
    PP_POS_Y,
    PP_WIDTH,
    PP_HEIGHT
) {
}