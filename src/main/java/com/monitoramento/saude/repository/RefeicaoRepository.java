package com.monitoramento.saude.repository;

import com.monitoramento.saude.model.Refeicao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RefeicaoRepository extends JpaRepository<Refeicao, Long> {
    List<Refeicao> findAllByUsuarioId(Long usuarioId);
}
