package web.condominiodigital.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import web.condominiodigital.notification.NotificacaoSweetAlert2;

@Component
public class NotificacaoSA2HeaderInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && modelAndView.getModel().containsKey("notificacao")) {
            NotificacaoSweetAlert2 notificacao = (NotificacaoSweetAlert2) modelAndView.getModel().get("notificacao");
            String jsonStr = String.format("{\"exibirAlerta\":{\"titulo\":\"%s\",\"mensagem\":\"%s\",\"tipo\":\"%s\"}}",
                    notificacao.getTitle(), notificacao.getText(), notificacao.getIcon());
            
            // HTMX expects HX-Trigger to dispatch client-side events
            response.setHeader("HX-Trigger", jsonStr);
        }
    }
}
