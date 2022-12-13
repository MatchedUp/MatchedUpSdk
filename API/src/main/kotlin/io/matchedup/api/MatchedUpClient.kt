package io.matchedup.api

import io.matchedup.api.actions.IAction
import io.matchedup.api.events.EventBus
import io.matchedup.api.events.EventRegistry
import io.matchedup.api.events.IEvent
import io.matchedup.api.events.state.DisconnectedEvent
import io.matchedup.api.exceptions.ConnectionFailedException
import io.matchedup.api.exceptions.InvalidServerException
import io.matchedup.api.services.MatchedUpService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URISyntaxException
import kotlin.concurrent.thread

class MatchedUpClient(
    private val accessKey: String,
    private val secretKey: String,
    private val log: Logger = LoggerFactory.getLogger("MatchedUp")
) {

    val eventBus = EventBus<IEvent>()
    private val wsUrl = "wss://ws.matchedup.io/v2"
    private var isManuallyClosed = false;

    private lateinit var matchedUpService: MatchedUpService

    init {
        EventRegistry // init events
        eventBus.registerListener(this::onWsDisconnect)
    }

    fun connect() {
        try {
            matchedUpService = MatchedUpService(log, wsUrl, accessKey, secretKey, eventBus)
            matchedUpService.connect()
        } catch (e: IOException) {
            throw ConnectionFailedException(e)
        } catch (e: URISyntaxException) {
            throw InvalidServerException()
        }
    }

    fun connectAsync() {
        thread {
            connect()
        }
    }

    private fun onWsDisconnect(event: DisconnectedEvent) {
        if (!isManuallyClosed) {
            log.debug("Attempting to reconnect")
            Thread.sleep(10000)
            connect()
        }
    }

    fun submitAction(action: IAction) =
        matchedUpService.sendAction(action)

    fun close() {
        log.debug("Shutting down...")
        matchedUpService.close()
        isManuallyClosed = true
    }
}