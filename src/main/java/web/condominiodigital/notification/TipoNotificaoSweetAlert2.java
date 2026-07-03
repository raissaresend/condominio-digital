package web.condominiodigital.notification;

public enum TipoNotificaoSweetAlert2 {
    SUCCESS("success"),
    ERROR("error"),
    WARNING("warning"),
    INFO("info"),
    QUESTION("question");

    private final String tipo;

    TipoNotificaoSweetAlert2(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
