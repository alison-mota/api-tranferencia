package com.transferencias.api.aplicacao.servico.regras.gerais

import com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.dto.ApiValidadoraResponse
import com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.servico.ApiValidadoraDeTransferencia
import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.auxiliares.excecoes.BusinessException
import com.transferencias.api.auxiliares.excecoes.SaldoInsuficienteException
import com.transferencias.api.auxiliares.excecoes.ServicoIndisponivelException
import com.transferencias.api.auxiliares.excecoes.UsuarioNaoAutorizadoException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException.BadRequest
import java.math.BigDecimal

@Service
class ValidadoresGeraisDeTransferencia(
    private val apiValidadora: ApiValidadoraDeTransferencia
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun validaSolicitacao(operadores: Pair<DadosOrigem, DadosDestino>) {
        val (dadosOrigem, _) = operadores
        val (_, dadosDestino) = operadores

        if (!dadosOrigem.temSaldoSuficiente()) throw SaldoInsuficienteException()
        if (dadosOrigem.usuario.id == dadosDestino.usuario.id) throw BusinessException("Usuário de origem e usuário de destino são iguais")
    }

    fun valorTotalDaTransacao(operadores: Pair<DadosOrigem, DadosDestino>): BigDecimal {
        val (dadosOrigem, _) = operadores
        val (_, dadosDestino) = operadores

        return dadosOrigem.valorDaTransferencia
            .plus(dadosOrigem.usuario.carteira.getSaldoAtual())
            .plus(dadosDestino.usuario.carteira.getSaldoAtual())
    }

    fun validacoesFinais(
        valorTransacaoInicial: Pair<DadosOrigem, DadosDestino>,
        operacaoRealizada: Pair<DadosOrigem, DadosDestino>
    ) {
        logger.info("[START - 06] Iniciando as validações finais da transação")
        validaValorInicialEFinal(valorTransacaoInicial, operacaoRealizada)
        validacoesExternas()
        logger.info("[END - 06] Validações concluídas. Transação será finalizada.")
    }

    private fun validaValorInicialEFinal(
        valorTransacaoInicial: Pair<DadosOrigem, DadosDestino>,
        operacaoRealizada: Pair<DadosOrigem, DadosDestino>
    ) {
        if (valorTotalDaTransacao(valorTransacaoInicial) != valorTotalDaTransacao(operacaoRealizada)) throw ServicoIndisponivelException(
            "Houve um problema no processamento da transferência. Tente novamente mais tarde"
        )
    }

    private fun validacoesExternas() {
        val validadora: ApiValidadoraResponse?

        try {
            validadora = apiValidadora.valida()
            when (validadora.podeTransferir) {
                false -> throw UsuarioNaoAutorizadoException("Serviço não autorizado")
                true -> return
            }
        } catch (ex: BadRequest) {
            throw BusinessException()
        } catch (ex: Exception) {
            throw ex
        }
    }
}