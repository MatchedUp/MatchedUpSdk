package io.matchedup.api.events.error

import io.matchedup.api.events.Event
import io.matchedup.api.events.IEvent
import kotlinx.serialization.Serializable

@Event("USER_ERROR")
@Serializable
class UserErrorEvent: IEvent {

    lateinit var uuid: String
    lateinit var error: String

}