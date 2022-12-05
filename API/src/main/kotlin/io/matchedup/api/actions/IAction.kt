package io.matchedup.api.actions

import java.lang.Error

interface IAction {

    companion object {
        fun getActionType(c: Class<out IAction>): String {
            if (
                c.annotations.firstOrNull {
                    it.annotationClass.java.name.equals(Action::class.java.name)
                } != null
            ) {
                val action = c.annotations.find { it is Action } as? Action
                return action!!.type
            }
            throw Error("No action type found for action '${Class.forName(c.name)}' to send")
        }
    }

}