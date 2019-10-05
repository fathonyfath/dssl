# Dead simple Service Locator

[![](https://jitpack.io/v/fathonyfath/dssl.svg)](https://jitpack.io/#fathonyfath/dssl)

This is simple Service Locator in Kotlin. I'm building this as a form of learning reified type parameter, and publishing library into JitPack.

## Setup

### Gradle

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2.** Add the dependency

```groovy
dependencies {
    implementation 'com.github.fathonyfath:dssl:Tag'
}
```

## Usage

There is only 3 API you can use to build your Service Locator.

```kotlin
val module = module {
    // Create singleton, instance will be retained in the module scope (named parameter is optional)
    single { ... }

    // Create factory, instance will be recreated everytime this instance is requested (named parameter is optional)
    factory { ... }

    single {
        // get function will try to resolve the dependency of Foo class
        Foo(get())
    }
}

// Resolve dependency
module.get()
```

## Example 

```kotlin
interface Foo
class FooImpl : Foo

class Bar(foo: Foo)

// Create module
val module = module {
    // Provide singleton dependency of Foo with name "fooInstance1"
    single<Foo>(named = "fooInstance1") {
        FooImpl()
    }
    
    // Provide factory dependency of Foo with name "fooInstance2", this will recreated everytime Foo with name "fooInstance2" is requested
    factory<Foo>(named = "fooInstance2") {
        FooImpl()
    }

    // Resolve dependency of Bar with Foo named "fooInstance2"
    single {
        Bar(get(named = "fooInstance1"))
    }
}

// Resolve dependency of Bar from module
val barInstance = module.get<Bar>()
```