package com.monitoramento.saude.repository;

import com.monitoramento.saude.model.Medidas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedidasRepository extends JpaRepository<Medidas, Long> {
    List<Medidas> findAllByUsuarioIdOrderByDataRegistroDesc(Long usuarioId);
}
