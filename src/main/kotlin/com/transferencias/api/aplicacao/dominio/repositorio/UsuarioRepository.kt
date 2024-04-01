package com.transferencias.api.aplicacao.dominio.repositorio;

import com.transferencias.api.aplicacao.dominio.entidade.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository : JpaRepository<Usuario, Long> {
}