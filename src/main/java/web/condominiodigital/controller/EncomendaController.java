package web.condominiodigital.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.condominiodigital.model.Encomenda;
import web.condominiodigital.model.StatusEncomenda;
import web.condominiodigital.notification.NotificacaoSweetAlert2;
import web.condominiodigital.notification.TipoNotificaoSweetAlert2;
import web.condominiodigital.service.EncomendaService;
import web.condominiodigital.service.TransportadoraService;
import web.condominiodigital.service.UnidadeService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/encomenda")
public class EncomendaController {

    @Autowired
    private EncomendaService encomendaService;

    @Autowired
    private UnidadeService unidadeService;

    @Autowired
    private TransportadoraService transportadoraService;

    private String resolveView(HttpServletRequest request, String viewName) {
        return "true".equals(request.getHeader("HX-Request")) ? viewName + " :: conteudo" : viewName;
    }

    @GetMapping
    public ModelAndView pesquisar(@RequestParam(value = "page", defaultValue = "0") int page, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "codigo"));
        Page<Encomenda> paginaEncomendas = encomendaService.buscarTodasPaginado(pageable);
        
        ModelAndView mv = new ModelAndView(resolveView(request, "encomenda/lista"));
        mv.addObject("paginaEncomendas", paginaEncomendas);
        mv.addObject("statusList", StatusEncomenda.values());
        return mv;
    }

    @GetMapping("/cadastrar")
    public ModelAndView abrirFormularioInclusao(Encomenda encomenda, HttpServletRequest request) {
        if (encomenda.getDataRecebimento() == null) {
            encomenda.setDataRecebimento(LocalDateTime.now());
        }
        
        ModelAndView mv = new ModelAndView(resolveView(request, "encomenda/formulario"));
        mv.addObject("encomenda", encomenda);
        mv.addObject("unidades", unidadeService.buscarTodas());
        mv.addObject("transportadoras", transportadoraService.buscarTodas());
        mv.addObject("statusList", StatusEncomenda.values());
        return mv;
    }

    @PostMapping("/cadastrar")
    public ModelAndView adicionar(@Valid Encomenda encomenda, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("encomenda/formulario :: formulario");
            mv.addObject("encomenda", encomenda);
            mv.addObject("unidades", unidadeService.buscarTodas());
            mv.addObject("transportadoras", transportadoraService.buscarTodas());
            mv.addObject("statusList", StatusEncomenda.values());
            return mv;
        }

        encomendaService.salvar(encomenda);

        ModelAndView mv = new ModelAndView("encomenda/formulario :: formulario");
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Sucesso!", "Encomenda cadastrada com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        
        Encomenda novaEncomenda = new Encomenda();
        novaEncomenda.setDataRecebimento(LocalDateTime.now());
        
        mv.addObject("encomenda", novaEncomenda);
        mv.addObject("unidades", unidadeService.buscarTodas());
        mv.addObject("transportadoras", transportadoraService.buscarTodas());
        mv.addObject("statusList", StatusEncomenda.values());
        return mv;
    }

    @GetMapping("/alterar/{codigo}")
    public ModelAndView abrirFormularioAlteracao(@PathVariable("codigo") Long codigo, HttpServletRequest request) {
        Encomenda encomenda = encomendaService.buscarPorCodigo(codigo);
        ModelAndView mv = new ModelAndView(resolveView(request, "encomenda/formulario"));
        mv.addObject("encomenda", encomenda);
        mv.addObject("unidades", unidadeService.buscarTodas());
        mv.addObject("transportadoras", transportadoraService.buscarTodas());
        mv.addObject("statusList", StatusEncomenda.values());
        return mv;
    }

    @PostMapping("/alterar/{codigo}")
    public ModelAndView alterar(@PathVariable("codigo") Long codigo, @Valid Encomenda encomenda, BindingResult result) {
        if (result.hasErrors()) {
            encomenda.setCodigo(codigo);
            ModelAndView mv = new ModelAndView("encomenda/formulario :: formulario");
            mv.addObject("encomenda", encomenda);
            mv.addObject("unidades", unidadeService.buscarTodas());
            mv.addObject("transportadoras", transportadoraService.buscarTodas());
            mv.addObject("statusList", StatusEncomenda.values());
            return mv;
        }

        encomenda.setCodigo(codigo);
        encomendaService.salvar(encomenda);

        ModelAndView mv = new ModelAndView("encomenda/formulario :: formulario");
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Sucesso!", "Encomenda alterada com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        mv.addObject("encomenda", encomenda);
        mv.addObject("unidades", unidadeService.buscarTodas());
        mv.addObject("transportadoras", transportadoraService.buscarTodas());
        mv.addObject("statusList", StatusEncomenda.values());
        return mv;
    }

    @PostMapping("/alterar/status/{codigo}")
    public ModelAndView alterarStatusInline(@PathVariable("codigo") Long codigo, @RequestParam("status") StatusEncomenda novoStatus, @RequestParam(value = "page", defaultValue = "0") int page) {
        Encomenda encomenda = encomendaService.buscarPorCodigo(codigo);
        encomenda.setStatus(novoStatus);
        encomendaService.salvar(encomenda);
        
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "codigo"));
        Page<Encomenda> paginaEncomendas = encomendaService.buscarTodasPaginado(pageable);
        
        ModelAndView mv = new ModelAndView("encomenda/lista :: tabelaEncomendas");
        mv.addObject("paginaEncomendas", paginaEncomendas);
        mv.addObject("statusList", StatusEncomenda.values());
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Atualizado!", "Status alterado com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        return mv;
    }

    @DeleteMapping("/remover/{codigo}")
    public ModelAndView remover(@PathVariable("codigo") Long codigo, @RequestParam(value = "page", defaultValue = "0") int page) {
        encomendaService.excluir(codigo);
        
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "codigo"));
        Page<Encomenda> paginaEncomendas = encomendaService.buscarTodasPaginado(pageable);
        
        ModelAndView mv = new ModelAndView("encomenda/lista :: tabelaEncomendas");
        mv.addObject("paginaEncomendas", paginaEncomendas);
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Removido!", "Encomenda excluída com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        return mv;
    }
}
