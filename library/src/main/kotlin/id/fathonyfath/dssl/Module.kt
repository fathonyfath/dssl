package id.fathonyfath.dssl

class Module internal constructor() {

    private val definitions = mutableMapOf<Identifier, Lazy<*>>()

    inline fun <reified T> single(named: String = "", noinline instance: () -> T): Identifier {
        val identifier = Identifier(T::class, named)
        val lazyInstance = lazy(instance)
        updateDefinitions(identifier, lazyInstance)
        return identifier
    }

    inline fun <reified T> factory(named: String = "", noinline instance: () -> T): Identifier {
        val identifier = Identifier(T::class, named)
        val lazyInstance = factoryOf(instance)
        updateDefinitions(identifier, lazyInstance)
        return identifier
    }

    fun <T> updateDefinitions(identifier: Identifier, instance: Lazy<T>) {
        check(!definitions.containsKey(identifier)) { "Cannot inject 2 instance with same definition" }
        definitions[identifier] = instance as Lazy<*>
    }

    inline fun <reified T> get(named: String = ""): T {
        val identifier = Identifier(T::class, named)
        return resolveDependency(identifier)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> resolveDependency(identifier: Identifier): T {
        val lazy = definitions[identifier]
                ?: throw IllegalStateException(
                        "No instance found based on the class ${identifier.instanceClass}" +
                                if (identifier.named.isNotEmpty()) "named ${identifier.named}" else ""
                )

        return lazy.value as T
    }

}

fun module(config: Module.() -> Unit): Module {
    return Module().apply(config)
}

