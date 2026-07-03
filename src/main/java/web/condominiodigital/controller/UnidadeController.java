package web.condominiodigital.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.condominiodigital.model.Unidade;
import web.condominiodigital.notification.NotificacaoSweetAlert2;
import web.condominiodigital.notification.TipoNotificaoSweetAlert2;
import web.condominiodigital.service.UnidadeService;

@Controller
@RequestMapping("/unidade")
public class UnidadeController {

    @Autowired
    private UnidadeService unidadeService;

    private String resolveView(HttpServletRequest request, String viewName) {
        return "true".equals(request.getHeader("HX-Request")) ? viewName + " :: conteudo" : viewName;
    }

    @GetMapping
    public ModelAndView pesquisar(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(resolveView(request, "unidade/lista"));
        mv.addObject("unidades", unidadeService.buscarTodas());
        return mv;
    }

    @GetMapping("/cadastrar")
    public ModelAndView abrirFormularioInclusao(Unidade unidade, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(resolveView(request, "unidade/formulario"));
        mv.addObject("unidade", unidade);
        return mv;
    }

    @PostMapping("/cadastrar")
    public ModelAndView adicionar(@Valid Unidade unidade, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("unidade/formulario :: formulario");
            mv.addObject("unidade", unidade);
            return mv;
        }

        unidadeService.salvar(unidade);

        ModelAndView mv = new ModelAndView("unidade/formulario :: formulario");
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Sucesso!", "Unidade cadastrada com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        mv.addObject("unidade", new Unidade());
        return mv;
    }

    @GetMapping("/alterar/{codigo}")
    public ModelAndView abrirFormularioAlteracao(@PathVariable("codigo") Long codigo, HttpServletRequest request) {
        Unidade unidade = unidadeService.buscarPorCodigo(codigo);
        ModelAndView mv = new ModelAndView(resolveView(request, "unidade/formulario"));
        mv.addObject("unidade", unidade);
        return mv;
    }

    @PostMapping("/alterar/{codigo}")
    public ModelAndView alterar(@PathVariable("codigo") Long codigo, @Valid Unidade unidade, BindingResult result) {
        if (result.hasErrors()) {
            unidade.setCodigo(codigo);
            ModelAndView mv = new ModelAndView("unidade/formulario :: formulario");
            mv.addObject("unidade", unidade);
            return mv;
        }

        unidade.setCodigo(codigo);
        unidadeService.salvar(unidade);

        ModelAndView mv = new ModelAndView("unidade/formulario :: formulario");
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Sucesso!", "Unidade alterada com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        mv.addObject("unidade", unidade);
        return mv;
    }

    @DeleteMapping("/remover/{codigo}")
    public ModelAndView remover(@PathVariable("codigo") Long codigo) {
        unidadeService.excluir(codigo);
        ModelAndView mv = new ModelAndView("unidade/lista :: tabelaUnidades");
        mv.addObject("unidades", unidadeService.buscarTodas());
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Removido!", "Unidade excluída com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        return mv;
    }
}
