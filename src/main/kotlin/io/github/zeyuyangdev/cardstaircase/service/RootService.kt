package io.github.zeyuyangdev.cardstaircase.service

import io.github.zeyuyangdev.cardstaircase.entity.*


/**
 * The root service class is responsible for managing services and the entity layer reference.
 * This class acts as a central hub for every other service within the application.
 *
 */
class RootService {

    val gameService = GameService(this)
    val playerActionService = PlayerActionService(this)

    lateinit var currentGame: CardStaircase

    /**
     * Adds the provided [newRefreshable] to all services
     * connected to this root service.
     */
    private fun addRefreshable(newRefreshable: Refreshable) {
        gameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service.
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }
}