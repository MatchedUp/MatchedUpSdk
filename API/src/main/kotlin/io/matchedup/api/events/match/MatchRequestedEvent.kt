package io.matchedup.api.events.match

import io.matchedup.api.events.Event
import io.matchedup.api.events.IEvent
import io.matchedup.api.resources.MatchPlayer
import kotlinx.serialization.Serializable

@Event("MATCH_REQUESTED")
@Serializable
class MatchRequestedEvent: IEvent {

    lateinit var ticketId: String
    lateinit var matchType: String
    lateinit var players: List<MatchPlayer>

}