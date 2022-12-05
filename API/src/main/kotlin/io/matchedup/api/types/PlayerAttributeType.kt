package io.matchedup.api.types

import kotlinx.serialization.Serializable

@Serializable
class PlayerAttributeType {

    var value: String;

    constructor(str: String) {
        this.value = str
    }

    constructor(num: Number) {
        this.value = num.toString()
    }

}