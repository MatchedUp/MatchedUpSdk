package io.matchedup.api.events.error

import io.matchedup.api.events.Event
import io.matchedup.api.events.IEvent
import kotlinx.serialization.Serializable

@Event("CLIENT_ERROR")
@Serializable
class ClientErrorEvent: IEvent {

    lateinit var error: String

}