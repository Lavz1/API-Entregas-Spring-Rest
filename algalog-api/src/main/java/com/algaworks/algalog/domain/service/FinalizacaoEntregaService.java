package com.algaworks.algalog.domain.service;

import com.algaworks.algalog.domain.model.Entrega;
import com.algaworks.algalog.domain.repository.EntregaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class FinalizacaoEntregaService {

    private BuscaServiceEntrega buscaServiceEntrega;
    private EntregaRepository entregaRepository;

    @Transactional
    public void finalizar(Long entregaId){
        Entrega entrega = buscaServiceEntrega.buscar(entregaId);

        entrega.finalizar();

        entregaRepository.save(entrega);
    }
}
