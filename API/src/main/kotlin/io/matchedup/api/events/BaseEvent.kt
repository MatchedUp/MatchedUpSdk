package io.matchedup.api.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
class BaseEvent: IEvent {

    lateinit var event: String
    lateinit var payload: JsonObject

}