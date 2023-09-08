package io.matchedup.api

import io.matchedup.api.actions.IAction
import io.matchedup.api.events.EventBus
import io.matchedup.api.events.EventRegistry
import io.matchedup.api.events.IEvent
import io.matchedup.api.events.state.*
import io.matchedup.api.services.MatchedUpService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread

class MatchedUpClient @JvmOverloads constructor(
    private val accessKey: String,
    private val secretKey: String,
    private val log: Logger = LoggerFactory.getLogger("MatchedUp"),
    private val autoReconnect: Boolean = true,
) {

    val eventBus = EventBus<IEvent>()
    private val wsUrl = "wss://ws.matchedup.io/v5"
    private var isManuallyClosed = false
    private var hasConnected = false

    private lateinit var matchedUpService: MatchedUpService

    init {
        EventRegistry // init events
        eventBus.registerListener(this::onWsDisconnect)
    }

    fun connect(): Boolean {
        matchedUpService = MatchedUpService(log, wsUrl, accessKey, secretKey, eventBus)
        if (matchedUpService.connectBlocking()) {
            hasConnected = true
            eventBus.dispatch(ConnectedEvent())
            return true
        }
        return false
    }

    fun connectAsync() {
        thread {
            connect()
        }
    }

    private fun onWsDisconnect(event: InternalDisconnectedEvent) {
        if (!hasConnected) {
            log.debug("Failed to initially connect to server")
            eventBus.dispatch(FailedToConnectEvent(event.code, event.reason))
        } else if (isManuallyClosed) {
            log.debug("Manually closed connection to server")
            eventBus.dispatch(DisconnectedEvent(event.code, event.reason))
        } else if (!autoReconnect) {
            log.debug("Disconnected, not auto reconnecting since its disabled")
            eventBus.dispatch(DisconnectedEvent(event.code, event.reason))
        } else if (!isManuallyClosed && autoReconnect) {
            log.debug("Lost connection to server, attempting to reconnect")
            eventBus.dispatch(DisconnectedEvent(event.code, event.reason))
            Thread.sleep(10000)
            eventBus.dispatch(ReconnectingEvent())
            connect()
        }
    }

    fun submitAction(action: IAction) =
        matchedUpService.sendAction(action)

    fun close() {
        log.debug("Shutting down...")
        isManuallyClosed = true
        matchedUpService.close()
    }
}