package com.transferencias.api.auxiliares.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@Component
class JwtUtils(
    @Value("\${segredos.jwtSecret}") internal val jwtSecret: String,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    internal val signingKey = Base64.getDecoder().decode(jwtSecret)

    fun extrairUsuarioId(token: String): String {
        return try {
            Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(limpaToken(token))
                .body["usuario_id"]
                .toString()
        } catch (ex: Exception) {
            // TODO: trocar exception
            throw RuntimeException()
        }
    }

    fun generateToken(secretKey: String, usuarioId: Long = 1): String {
        val claims = Jwts.claims()
            .setSubject("api-transferencia")
            .add("usuario_id", usuarioId)
            .build()
        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    private fun limpaToken(token: String): String = token.replace("Bearer ", "")
}

@RestController
@RequestMapping("/auth")
class GeradorDeToken(
    private val jwtUtils: JwtUtils
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{usuario_id}")
    fun gerar(
        @PathVariable("usuario_id") usuarioId: String
    ): LoginResult {
        logger.info("[START - 01] Gerando token para o id $usuarioId")

        return LoginResult(jwtUtils.generateToken(jwtUtils.jwtSecret))
            .also {
                logger.info("[END - 01] Token gerado")
            }
    }
}

data class LoginResult(
    val token: String
)