package io.matchedup.api.actions

import kotlinx.serialization.Serializable

@Serializable
class BaseAction(val action: String, val payload: IAction):
    IAction