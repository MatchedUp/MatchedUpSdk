package io.matchedup.api.events.match

import io.matchedup.api.events.Event
import kotlinx.serialization.Serializable

@Event("MATCH_CREATED")
@Serializable
class MatchCreatedEvent: AbstractMatchEvent() {
}