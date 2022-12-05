package io.matchedup.api.resources

import kotlinx.serialization.Serializable

@Serializable
class MatchTicket {

    lateinit var id: String
    lateinit var players: List<MatchPlayer>

}