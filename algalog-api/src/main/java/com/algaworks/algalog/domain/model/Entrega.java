package com.algaworks.algalog.domain.model;

import com.algaworks.algalog.domain.ValidationGroups;
import com.algaworks.algalog.domain.exception.NegocioException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Entrega {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Valid //Valid o que está dentro do cliente também!
    @ConvertGroup(from = Default.class, to = ValidationGroups.ClientId.class) // Valiadar bease na nova interface TAG ClientID
    @NotNull
    @ManyToOne //Muitas entregas possui um cliente
    @JoinColumn(name = "cliente_id") //default name. Sempre que quiser alterar nome padrão da tabela usar JoinColumn
    private Cliente cliente;

    @Valid
    @Embedded //Os atributos de destinatário estão numa classe a parte, porém não será uma tabela
    // a parte no banco e sim parte da tabela de entrega.
    @NotNull
    private Destinatario destinatario;

    @NotNull
    private BigDecimal taxa;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private StatusEntrega status;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime dataPedido;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime dataFinalizacao;

    /*@Transient //não mapeado para objeto relacional
    private String senha;*/

    @OneToMany(mappedBy = "entrega", cascade = CascadeType.ALL)
    private List<Ocorrencia> ocorrencias = new ArrayList<>();

    public Ocorrencia adicionarOcorrencia(String descricao) {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setDescricao(descricao);
        ocorrencia.setDataRegistro(OffsetDateTime.now());
        ocorrencia.setEntrega(this);

        this.getOcorrencias().add(ocorrencia);

        return ocorrencia;
    }

    public void finalizar(){
        if (naoPodeSerFinalizada()) {
            throw new NegocioException("Entrega não pode ser finalizada");
        }

        this.setStatus(StatusEntrega.FINALIZADO);
        this.setDataFinalizacao(OffsetDateTime.now());
    }

    public boolean podeSerFinalizada(){
        return StatusEntrega.PEDENTE.equals(getStatus());
    }

    public boolean naoPodeSerFinalizada(){
        return !podeSerFinalizada();
    }

}
