package io.github.zeyu.staircasecardgame.service

/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the GUI classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * GUI classes only need to react to events relevant to them.
 *
 * @see AbstractRefreshingService
 */
interface Refreshable {

    fun refreshAfterStartNewGame() {}

    fun refreshAfterEndGame() {}

    fun refreshAfterShuffleStack() {}

    fun refreshAfterPlayCard() {}

    fun refreshAfterDestroyCard() {}

    fun refreshAfterDiscardCard() {}

    fun refreshAfterStartTurn() {}

    fun refreshAfterEndTurn() {}

}