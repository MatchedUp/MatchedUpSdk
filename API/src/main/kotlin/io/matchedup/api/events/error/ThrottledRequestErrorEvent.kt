package io.matchedup.api.events.error

import io.matchedup.api.events.Event
import io.matchedup.api.events.IEvent
import kotlinx.serialization.Serializable

@Event("THROTTLED_REQUEST_ERROR")
@Serializable
class ThrottledRequestErrorEvent: IEvent {

    lateinit var error: String

}