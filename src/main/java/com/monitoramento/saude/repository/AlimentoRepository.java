package com.monitoramento.saude.repository;

import com.monitoramento.saude.model.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlimentoRepository extends JpaRepository<Alimento, Long> {}
