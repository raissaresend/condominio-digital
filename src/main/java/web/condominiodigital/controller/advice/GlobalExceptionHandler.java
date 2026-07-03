package web.condominiodigital.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        
        // Se for uma requisição do HTMX, renderiza apenas o fragmento do erro
        if (request.getHeader("HX-Request") != null) {
            mav.setViewName("error/500 :: conteudo");
        } else {
            mav.setViewName("error/500");
        }
        mav.addObject("mensagem", ex.getMessage());
        return mav;
    }
}
