package io.matchedup.api.events.state

import io.matchedup.api.events.IEvent
import kotlinx.serialization.Serializable

@Serializable
class InternalDisconnectedEvent constructor(
    val code: Int,
    val reason: String?,
) : IEvent