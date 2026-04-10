package io.github.zeyuyangdev.cardstaircase.view

import io.github.zeyuyangdev.cardstaircase.service.RootService
import io.github.zeyuyangdev.cardstaircase.service.Refreshable

import tools.aqua.bgw.core.BoardGameApplication

/**
 * Represents the main application.
 * The application initializes the [RootService] and displays the scenes.
 */
class CSApplication : BoardGameApplication("Card Staircase"), Refreshable {

    /**
     * The root service instance. This is used to call service methods and access the entity layer.
     */
    val rootService: RootService = RootService()

    /**
     * The main game scene displayed in the application.
     */
    private val gameScene = GameScene(rootService)

    private val resultMenuScene = ResultMenuScene(rootService).apply {
        replayButton.onMouseClicked = {
            // Changes the player order before starts a new game.
            val player1Name = rootService.currentGame.players[1].name
            val player2Name = rootService.currentGame.players[0].name

            rootService.gameService.startNewGame(player1Name, player2Name)
        }

        exitButton.onMouseClicked = {
            exit()
        }
    }

    private val mainMenuScene = MainMenuScene(rootService).apply {
        exitGameButton.onMouseClicked = {
            exit()
        }
    }

    init {

        // Adds the following refreshables to all services connected to rootService.
        rootService.addRefreshables(
            this,
            gameScene,
            gameScene.paneStairs,
            gameScene.paneStacks,
            gameScene.panePlayerLeft,
            gameScene.panePlayerRight,
            resultMenuScene,
            mainMenuScene
        )

        // Shows the main menu scene upon application start.
        showMenuScene(mainMenuScene)

        // temp button to show ResultMenuScene:
        gameScene.tempButton.apply {
            onMouseClicked = {
                rootService.gameService.endGame()
                showMenuScene(resultMenuScene)
            }
        }
    }

    override fun refreshAfterStartNewGame() {
        hideMenuScene()
        showGameScene(gameScene)
    }

    override fun refreshAfterEndGame() {
        showMenuScene(resultMenuScene)
    }
}

