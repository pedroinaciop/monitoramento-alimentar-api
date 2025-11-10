package com.monitoramento.saude.repository;

import com.monitoramento.saude.model.Medidas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MedidasRepository extends JpaRepository<Medidas, Long> {
    List<Medidas> findAllByUsuarioIdOrderByDataRegistroDesc(Long usuarioId);
    Optional<Medidas> findFirstByUsuarioIdOrderByDataRegistroDesc(Long usuarioId);
}
