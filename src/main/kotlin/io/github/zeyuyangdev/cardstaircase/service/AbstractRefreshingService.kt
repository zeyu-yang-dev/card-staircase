package io.github.zeyuyangdev.cardstaircase.service

/**
 * Abstract service class that manages multiple [Refreshable] instances,
 * (usually UI elements, such as specialized [tools.aqua.bgw.core.BoardGameScene] classes/instances)
 * which are notified of changes to refresh via the [onAllRefreshables] method.
 */
abstract class AbstractRefreshingService {

    private val refreshables = mutableListOf<Refreshable>()

    /**
     * Adds a new [Refreshable] to the list of refreshables.
     *
     * @param newRefreshable The [Refreshable] to be added
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        refreshables += newRefreshable
    }

    /**
     * Executes the passed method (usually a lambda) on all
     * [Refreshable]s registered with the service class that
     * extends this [AbstractRefreshingService]
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) =
        refreshables.forEach { it.method() }
}