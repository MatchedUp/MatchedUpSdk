package io.matchedup.api.resources

import io.matchedup.api.serializers.AnySerial
import kotlinx.serialization.Serializable

@Serializable
class MatchPlayer {

    lateinit var uuid: String
    lateinit var playerAttribs: Map<String, AnySerial>
    var team: String? = null

}