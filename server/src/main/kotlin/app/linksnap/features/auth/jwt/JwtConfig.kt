package app.linksnap.features.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import io.ktor.server.auth.jwt.JWTAuthenticationProvider
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.Date

object JwtConfig {


    private const val SECRET = "linksnap_secret"
    private const val ISSUER = "linksnap"
    private const val AUDIENCE = "linksnap_audience"
    private const val VALIDITY_MS = 3600000 * 24 * 30 // 30 days

    val algorithm = Algorithm.HMAC256(SECRET)

    fun makeToken(userId: String) =
        JWT.create()
            .withSubject("AUTH")
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + VALIDITY_MS))
            .sign(algorithm)

    fun configureKtor(config: JWTAuthenticationProvider.Config) {
        config.verifier(
            JWT.require(algorithm).withIssuer(ISSUER).withAudience(AUDIENCE).build()
        )

        config.validate { credentials ->
            if (credentials.payload.getClaim("userId").asString() != null) {
                JWTPrincipal(credentials.payload)
            } else {
                null
            }
        }
    }


    class JWTPrincipal(val payload: Payload) {
        val userId: String
            get() = payload.getClaim("userId").asString()
    }


}