package web.condominiodigital.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import web.condominiodigital.interceptor.NotificacaoSA2HeaderInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private NotificacaoSA2HeaderInterceptor notificacaoSA2HeaderInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(notificacaoSA2HeaderInterceptor);
    }
}
