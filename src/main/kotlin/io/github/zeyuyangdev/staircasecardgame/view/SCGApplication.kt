package io.github.zeyuyangdev.staircasecardgame.view

import io.github.zeyuyangdev.staircasecardgame.service.RootService
import io.github.zeyuyangdev.staircasecardgame.service.Refreshable

import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.core.BoardGameApplication


/**
 * Represents the main application.
 * The application initializes the [RootService] and displays the scenes.
 */
class SCGApplication : BoardGameApplication("Staircase Card Game"), Refreshable {

    /**
     * The root service instance. This is used to call service methods and access the entity layer.
     */
    val rootService: RootService = RootService()


    /**
     * The main game scene displayed in the application.
     */
    private val gameScene = GameScene(rootService).apply {
        onKeyPressed = { event ->
            if (event.keyCode in listOf(KeyCode.ENTER)) {
                rootService.gameService.shuffleDrawStack(true)
            } else if (event.keyCode in listOf(KeyCode.DELETE)) {
                rootService.gameService.shuffleDrawStack(false)
            }
        }
    }

    private val victoryScene = VictoryScene(rootService).apply {
        replayButton.onMouseClicked = {
            val player1Name = rootService.currentGame.players[1].name
            val player2Name = rootService.currentGame.players[0].name

            rootService.gameService.startNewGame(player1Name, player2Name)

            hideMenuScene()
            showGameScene(gameScene)

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

        rootService.addRefreshables(
            this,
            gameScene,
            gameScene.paneStairs,
            gameScene.paneStacks,
            gameScene.panePlayer1,
            gameScene.panePlayer2,
            victoryScene,
            mainMenuScene
        )


        this.showMenuScene(mainMenuScene)
    }


    override fun refreshAfterStartNewGame() {
        this.hideMenuScene()
        this.showGameScene(gameScene)
    }


    override fun refreshAfterEndGame() {
        this.showMenuScene(victoryScene)
    }

}

