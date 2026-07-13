package web.condominiodigital.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "encomenda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Encomenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotBlank(message = "A descrição da encomenda é obrigatória")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @NotNull(message = "A data de recebimento é obrigatória")
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "data_recebimento", nullable = false)
    private LocalDateTime dataRecebimento;

    @NotNull(message = "O status da encomenda é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatusEncomenda status;

    @NotNull(message = "A unidade de destino é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_unidade", nullable = false)
    private Unidade unidade;

    @NotNull(message = "A transportadora é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_transportadora", nullable = false)
    private Transportadora transportadora;
}
