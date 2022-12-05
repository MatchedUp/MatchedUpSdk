package io.matchedup.api.actions.impl

import io.matchedup.api.actions.IAction
import io.matchedup.api.types.PlayerAttributeType
import kotlinx.serialization.Serializable

@io.matchedup.api.actions.Action("match-request")
@Serializable
class RequestMatch(
    val uuid: String,
    val matchName: String,
    val playerAttributes: Map<String, PlayerAttributeType> = HashMap()
) : IAction