package io.matchedup.api.events.match

import io.matchedup.api.events.IEvent
import io.matchedup.api.resources.MatchTicket
import io.matchedup.api.serializers.AnySerial
import kotlinx.serialization.Serializable

@Serializable
abstract class AbstractMatchEvent: IEvent {

    var matchId: String? = null
    lateinit var matchType: String
    lateinit var matchAttribs: Map<String, AnySerial>
    lateinit var tickets: List<MatchTicket>

}