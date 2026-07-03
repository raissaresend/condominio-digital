package web.condominiodigital.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.condominiodigital.model.Transportadora;
import web.condominiodigital.notification.NotificacaoSweetAlert2;
import web.condominiodigital.notification.TipoNotificaoSweetAlert2;
import web.condominiodigital.service.TransportadoraService;

import java.util.List;

@Controller
@RequestMapping("/transportadora")
public class TransportadoraController {

    @Autowired
    private TransportadoraService transportadoraService;

    private String resolveView(HttpServletRequest request, String viewName) {
        return "true".equals(request.getHeader("HX-Request")) ? viewName + " :: conteudo" : viewName;
    }

    @GetMapping
    public ModelAndView pesquisar(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(resolveView(request, "transportadora/lista"));
        mv.addObject("transportadoras", transportadoraService.buscarTodas());
        return mv;
    }

    @GetMapping("/cadastrar")
    public ModelAndView abrirFormularioInclusao(Transportadora transportadora, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(resolveView(request, "transportadora/formulario"));
        mv.addObject("transportadora", transportadora);
        return mv;
    }

    @PostMapping("/cadastrar")
    public ModelAndView adicionar(@Valid Transportadora transportadora, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("transportadora/formulario :: formulario");
            mv.addObject("transportadora", transportadora);
            return mv;
        }

        transportadoraService.salvar(transportadora);

        ModelAndView mv = new ModelAndView("transportadora/formulario :: formulario");
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Sucesso!", "Transportadora salva com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        mv.addObject("transportadora", new Transportadora());
        return mv;
    }

    @GetMapping("/alterar/{codigo}")
    public ModelAndView abrirFormularioAlteracao(@PathVariable("codigo") Long codigo, HttpServletRequest request) {
        Transportadora transportadora = transportadoraService.buscarPorCodigo(codigo);
        ModelAndView mv = new ModelAndView(resolveView(request, "transportadora/formulario"));
        mv.addObject("transportadora", transportadora);
        return mv;
    }

    @PostMapping("/alterar/{codigo}")
    public ModelAndView alterar(@PathVariable("codigo") Long codigo, @Valid Transportadora transportadora, BindingResult result) {
        if (result.hasErrors()) {
            transportadora.setCodigo(codigo);
            ModelAndView mv = new ModelAndView("transportadora/formulario :: formulario");
            mv.addObject("transportadora", transportadora);
            return mv;
        }

        transportadora.setCodigo(codigo);
        transportadoraService.salvar(transportadora);

        ModelAndView mv = new ModelAndView("transportadora/formulario :: formulario");
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Sucesso!", "Transportadora alterada com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        mv.addObject("transportadora", transportadora);
        return mv;
    }

    @DeleteMapping("/remover/{codigo}")
    public ModelAndView remover(@PathVariable("codigo") Long codigo) {
        transportadoraService.excluir(codigo);
        ModelAndView mv = new ModelAndView("transportadora/lista :: tabelaTransportadoras");
        mv.addObject("transportadoras", transportadoraService.buscarTodas());
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Removido!", "Transportadora excluída com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        return mv;
    }
}
