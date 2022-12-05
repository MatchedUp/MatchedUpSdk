package io.matchedup.api.events.match

import io.matchedup.api.events.Event
import kotlinx.serialization.Serializable

@Event("MATCH_REQUEST_TIMED_OUT")
@Serializable
class MatchRequestTimedOutEvent: AbstractMatchEvent() {
}