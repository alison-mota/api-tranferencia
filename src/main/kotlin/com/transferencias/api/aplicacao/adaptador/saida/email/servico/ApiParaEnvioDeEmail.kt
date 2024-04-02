package com.transferencias.api.aplicacao.adaptador.saida.email.servico

import com.transferencias.api.aplicacao.adaptador.saida.email.api.EnvioDeEmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ApiParaEnvioDeEmail(
    private val envioDeEmail: EnvioDeEmail
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun envia() {
        val escopo = CoroutineScope(IO)

        escopo.launch {
            try {
                envioDeEmail.envia()
                logger.info("Email enviado")
            } catch (ex: Exception) {
                logger.error("Houve um problema ao tentar enviar o e-mail de confirmação. Fallback ainda não foi implementado")
                // Aqui poderia ter um fallback para uma fila dlq
            }
        }
    }
}