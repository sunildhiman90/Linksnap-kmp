package app.linksnap

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform