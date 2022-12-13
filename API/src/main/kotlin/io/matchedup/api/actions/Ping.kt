package io.matchedup.api.actions

import kotlinx.serialization.Serializable

@Action("ping")
@Serializable
class Ping: IAction