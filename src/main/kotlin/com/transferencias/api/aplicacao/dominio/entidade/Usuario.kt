package com.transferencias.api.aplicacao.dominio.entidade

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "usuarios")
class Usuario(
    var nome: String,
    @Column(name = "cpf_cnpj", unique = true)
    var cpfOuCnpj: String,
    @Column(unique = true)
    var email: String,
    var senha: String,
    @Enumerated(EnumType.STRING)
    var perfil: Perfil,
    @Embedded
    var carteira: Carteira
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    val id: Long = 0

    @Column(name = "instante_criacao", updatable = false)
    val instanteCriacao: LocalDateTime = LocalDateTime.now()

    enum class Perfil { LOJISTA, COMUM }

    @Embeddable
    class Carteira {
        private var dinheiro: BigDecimal = BigDecimal.ZERO
        fun getSaldoAtual() = dinheiro
        fun adicionaSaldo(valorTransacao: BigDecimal) = this.let {
            it.dinheiro = it.dinheiro.add(valorTransacao); it
        }

        fun subtraiSaldo(valorTransacao: BigDecimal) = this.let {
            it.dinheiro = it.dinheiro.minus(valorTransacao); it
        }
    }
}