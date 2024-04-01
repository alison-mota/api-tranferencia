package com.transferencias.api.aplicacao.servico.regras.gerais

import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.auxiliares.excecoes.BusinessException
import com.transferencias.api.auxiliares.excecoes.SaldoInsuficienteException
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ValidadoresGeraisDeTransferencia {
    fun validaSolicitacao(operadores: Pair<DadosOrigem, DadosDestino>) {
        val (dadosOrigem, _) = operadores
        val (_, dadosDestino) = operadores

        if (!dadosOrigem.temSaldoSuficiente()) throw SaldoInsuficienteException()
        if(dadosOrigem.usuario.id == dadosDestino.usuario.id) throw BusinessException("Usuário de origem e usuário de destino são iguais")
    }

    fun valorTotalDaTransacao(operadores: Pair<DadosOrigem, DadosDestino>): BigDecimal {
        val (dadosOrigem, _) = operadores
        val (_, dadosDestino) = operadores

        return dadosOrigem.valorDaTransferencia
            .plus(dadosOrigem.usuario.carteira.getSaldoAtual())
            .plus(dadosDestino.usuario.carteira.getSaldoAtual())
    }

    /*
        Este método soma todos os valores antes e depois e os compara para poder validar se houve fraudes sistêmicas.
     */
    fun validaValorInicialEFinal(
        valorTransacaoInicial: Pair<DadosOrigem, DadosDestino>,
        operacaoRealizada: Pair<DadosOrigem, DadosDestino>
    ) {
        // TODO: atualizar a exceção
        if (valorTotalDaTransacao(valorTransacaoInicial) != valorTotalDaTransacao(operacaoRealizada)) throw RuntimeException()
    }
}