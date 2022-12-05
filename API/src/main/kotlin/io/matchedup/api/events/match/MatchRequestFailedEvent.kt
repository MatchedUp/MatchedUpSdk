package io.matchedup.api.events.match

import io.matchedup.api.events.Event
import kotlinx.serialization.Serializable

@Event("MATCH_REQUEST_FAILED")
@Serializable
class MatchRequestFailedEvent: AbstractMatchEvent() {
}