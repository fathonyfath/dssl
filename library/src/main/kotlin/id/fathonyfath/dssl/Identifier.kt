package id.fathonyfath.dssl

import kotlin.reflect.KClass

data class Identifier(
        val instanceClass: KClass<*>,
        val named: String
)