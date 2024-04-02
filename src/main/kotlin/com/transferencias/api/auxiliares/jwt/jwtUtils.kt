package com.transferencias.api.auxiliares.jwt

import com.transferencias.api.auxiliares.excecoes.TokenInvalidoException
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
    internal val signingKey = Base64.getDecoder().decode(jwtSecret)
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun extrairUsuarioId(token: String): String {
        logger.info("[START - 03] Extraindo usuario_id do token")
        return try {
            Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(limpaToken(token))
                .body["usuario_id"]
                .toString()
                .also {
                    logger.info("[END - 03] Usuario_id extraído do token: $it")
                }
        } catch (ex: Exception) {
            throw TokenInvalidoException()
        }
    }

    fun generateToken(usuarioId: Long): String {
        val claims = Jwts.claims()
            .setSubject("api-transferencia")
            .add("usuario_id", usuarioId)
            .build()
        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, jwtSecret)
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
        @PathVariable("usuario_id") usuarioId: Long
    ): LoginResult {
        logger.info("[START - 01] Gerando token para o id $usuarioId")

        return LoginResult(jwtUtils.generateToken(usuarioId))
            .also {
                logger.info("[END - 01] Token gerado")
            }
    }
}

data class LoginResult(
    val token: String
)