package com.algaworks.algalog.domain.service;

import com.algaworks.algalog.domain.model.Ocorrencia;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class RegistroOcorrenciaService {

    private BuscaServiceEntrega buscaServiceEntrega;

    @Transactional
    public Ocorrencia registrar(Long entregaId, String descricao){
        return buscaServiceEntrega.buscar(entregaId).adicionarOcorrencia(descricao);
    }

}
