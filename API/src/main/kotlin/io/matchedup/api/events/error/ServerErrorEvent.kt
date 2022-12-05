package io.matchedup.api.events.error

import io.matchedup.api.events.Event
import io.matchedup.api.events.IEvent
import kotlinx.serialization.Serializable

@Event("SERVER_ERROR")
@Serializable
class ServerErrorEvent: IEvent {

    lateinit var error: String

}