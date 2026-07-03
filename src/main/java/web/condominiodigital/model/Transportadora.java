package web.condominiodigital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "transportadora")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transportadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotBlank(message = "O nome da transportadora é obrigatório")
    @Size(max = 255, message = "O nome não pode exceder 255 caracteres")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório")
    @Size(max = 20, message = "O CNPJ não pode exceder 20 caracteres")
    @Column(nullable = false, unique = true, length = 20)
    private String cnpj;

    @OneToMany(mappedBy = "transportadora")
    private List<Encomenda> encomendas = new ArrayList<>();
}
