package io.matchedup.api.services

import io.matchedup.api.actions.IAction
import io.matchedup.api.actions.Ping
import io.matchedup.api.events.EventBus
import io.matchedup.api.events.EventRegistry
import io.matchedup.api.events.IEvent
import io.matchedup.api.events.BaseEvent
import io.matchedup.api.events.error.ServerErrorEvent
import io.matchedup.api.events.state.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import org.slf4j.Logger
import java.net.URI
import kotlin.concurrent.thread

class MatchedUpService(
    val log: Logger,
    matchedUpUrl: String,
    accessKey: String,
    secretKey: String,
    private val eventBus: EventBus<IEvent>,
) : WebSocketClient(URI(matchedUpUrl), Draft_6455(), getHeaders(accessKey, secretKey), 30000) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        log.debug("Connected to MatchedUps server, starting ping")
        doPing()
    }

    @OptIn(InternalSerializationApi::class)
    fun sendAction(action: IAction) {
        if (!this.isOpen) {
            if (action is Ping) return // out of user control, not a problem
            throw Error("Could not send action, not connected to MatchedUp server")
        }

        val actionType = IAction.getActionType(action::class.java)
        val actionJson = json.encodeToString(action::class.serializer() as SerializationStrategy<IAction>, action)

        val fullEvent = "{ \"action\": \"${actionType}\", \"payload\": $actionJson }"
        if (actionType != "ping") {
            log.debug("Sending action $fullEvent")
        }
        send(fullEvent)
    }

    override fun onMessage(message: String?) {
        if (message == null
            || ! json.decodeFromString<JsonObject>(message).containsKey("event")) return

        log.debug("Got message from service: $message")

        // Get event data & serializer
        val rootEvent = json.decodeFromString<BaseEvent>(message)
        val serializer = EventRegistry.getSerializer(rootEvent.event)

        if (serializer == null) {
            log.debug("Got event '${rootEvent.event}' that does not have handler")
            return
        }

        // Dispatch event
        val payloadEvent =
            json.decodeFromJsonElement(serializer as DeserializationStrategy<*>, rootEvent.payload) as IEvent
        eventBus.dispatch(payloadEvent)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        log.debug("Closed server connection. code='$code' reason='$reason'")
        var finalCode = code

        // Check unauthorized
        if (reason !== null && reason.indexOf("401") >= 0) {
            finalCode = 401
        }

        eventBus.dispatch(InternalDisconnectedEvent(finalCode, reason))
    }

    override fun onError(ex: Exception?) {
        log.debug("Error from websocket service: $ex")
        val errEvent = ServerErrorEvent()
        errEvent.error = ex!!.message!!
        eventBus.dispatch(errEvent)
    }

    private fun doPing() {
        thread(start = true) {
            while (!this.isClosed) {
                try {
                    sendAction(Ping())
                    Thread.sleep(15000)
                } catch (e: Exception) {
                    log.debug("Error pinging: $e")
                }
            }
        }
    }

    companion object {
        fun getHeaders(accessKey: String, secretKey: String): Map<String, String> {
            val headers = HashMap<String, String>()
            headers["ACCESS_KEY"] = accessKey
            headers["SECRET_KEY"] = secretKey
            return headers
        }
    }

}