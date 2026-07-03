package web.condominiodigital.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.condominiodigital.model.AvisoMural;
import web.condominiodigital.notification.NotificacaoSweetAlert2;
import web.condominiodigital.notification.TipoNotificaoSweetAlert2;
import web.condominiodigital.service.AvisoMuralService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/aviso")
public class AvisoMuralController {

    @Autowired
    private AvisoMuralService avisoMuralService;

    private String resolveView(HttpServletRequest request, String viewName) {
        return "true".equals(request.getHeader("HX-Request")) ? viewName + " :: conteudo" : viewName;
    }

    @GetMapping
    public ModelAndView pesquisar(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(resolveView(request, "aviso/lista"));
        mv.addObject("avisos", avisoMuralService.buscarTodos());
        return mv;
    }

    @GetMapping("/cadastrar")
    public ModelAndView abrirFormularioInclusao(AvisoMural aviso, HttpServletRequest request) {
        if (aviso.getDataPublicacao() == null) {
            aviso.setDataPublicacao(LocalDateTime.now());
        }
        ModelAndView mv = new ModelAndView(resolveView(request, "aviso/formulario"));
        mv.addObject("aviso", aviso);
        return mv;
    }

    @PostMapping("/cadastrar")
    public ModelAndView adicionar(@Valid AvisoMural aviso, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("aviso/formulario :: formulario");
            mv.addObject("aviso", aviso);
            return mv;
        }

        avisoMuralService.salvar(aviso);

        ModelAndView mv = new ModelAndView("aviso/formulario :: formulario");
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Sucesso!", "Aviso publicado com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        
        AvisoMural novoAviso = new AvisoMural();
        novoAviso.setDataPublicacao(LocalDateTime.now());
        mv.addObject("aviso", novoAviso);
        return mv;
    }

    @GetMapping("/alterar/{codigo}")
    public ModelAndView abrirFormularioAlteracao(@PathVariable("codigo") Long codigo, HttpServletRequest request) {
        AvisoMural aviso = avisoMuralService.buscarPorCodigo(codigo);
        ModelAndView mv = new ModelAndView(resolveView(request, "aviso/formulario"));
        mv.addObject("aviso", aviso);
        return mv;
    }

    @PostMapping("/alterar/{codigo}")
    public ModelAndView alterar(@PathVariable("codigo") Long codigo, @Valid AvisoMural aviso, BindingResult result) {
        if (result.hasErrors()) {
            aviso.setCodigo(codigo);
            ModelAndView mv = new ModelAndView("aviso/formulario :: formulario");
            mv.addObject("aviso", aviso);
            return mv;
        }

        aviso.setCodigo(codigo);
        avisoMuralService.salvar(aviso);

        ModelAndView mv = new ModelAndView("aviso/formulario :: formulario");
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Sucesso!", "Aviso alterado com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        mv.addObject("aviso", aviso);
        return mv;
    }

    @DeleteMapping("/remover/{codigo}")
    public ModelAndView remover(@PathVariable("codigo") Long codigo) {
        avisoMuralService.excluir(codigo);
        ModelAndView mv = new ModelAndView("aviso/lista :: tabelaAvisos");
        mv.addObject("avisos", avisoMuralService.buscarTodos());
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Removido!", "Aviso excluído com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        return mv;
    }
}
