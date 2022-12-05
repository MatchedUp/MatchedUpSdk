package io.matchedup.api.events

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import kotlin.reflect.KClass

class EventBus<E : IEvent> {

    private val listenersMap: Multimap<Class<out E>, Listener<E>> = HashMultimap.create()

    fun interface Listener<E : IEvent> : (E) -> Unit

    fun <T : E> registerListener(eventType: Class<T>, listener: Listener<T>) {
        listenersMap.put(eventType, listener as Listener<E>)
    }

    inline fun <reified T : E> registerListener(listener: Listener<T>) =
        registerListener(T::class.java, listener)

    fun dispatch(event: E) {
        val eventObjType = event.javaClass as Class<out E>
        val eventListeners = listenersMap[eventObjType] ?: return
        for (listener in eventListeners) {
            listener(event)
        }
    }

}