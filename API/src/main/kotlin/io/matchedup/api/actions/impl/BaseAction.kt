package io.matchedup.api.actions.impl

import io.matchedup.api.actions.IAction
import kotlinx.serialization.Serializable

@Serializable
class BaseAction(val action: String, val payload: IAction):
    IAction