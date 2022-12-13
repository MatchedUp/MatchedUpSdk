package io.matchedup.api.actions

import io.matchedup.api.types.PlayerAttributeType
import kotlinx.serialization.Serializable

@Action("match-request")
@Serializable
class RequestMatch(
    val uuid: String,
    val matchName: String,
    val playerAttributes: Map<String, PlayerAttributeType> = HashMap()
) : IAction