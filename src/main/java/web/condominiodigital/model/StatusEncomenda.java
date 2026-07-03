package web.condominiodigital.model;

public enum StatusEncomenda {
    AGUARDANDO_RETIRADA("Aguardando Retirada"),
    ENTREGUE("Entregue"),
    DEVOLVIDA("Devolvida");

    private final String descricao;

    StatusEncomenda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
