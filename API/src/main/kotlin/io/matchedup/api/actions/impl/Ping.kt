package io.matchedup.api.actions.impl

import io.matchedup.api.actions.Action
import io.matchedup.api.actions.IAction
import kotlinx.serialization.Serializable

@io.matchedup.api.actions.Action("ping")
@Serializable
class Ping: IAction