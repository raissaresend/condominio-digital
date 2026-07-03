package web.condominiodigital.notification;

public class NotificacaoSweetAlert2 {

    private final String titulo;
    private final String texto;
    private final TipoNotificaoSweetAlert2 tipo;

    public NotificacaoSweetAlert2(String titulo, String texto, TipoNotificaoSweetAlert2 tipo) {
        this.titulo = titulo;
        this.texto = texto;
        this.tipo = tipo;
    }

    public String getTitle() {
        return titulo;
    }

    public String getText() {
        return texto;
    }

    public String getIcon() {
        return tipo.getTipo();
    }
}
