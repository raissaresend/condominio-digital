package web.condominiodigital.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.condominiodigital.model.Usuario;
import web.condominiodigital.notification.NotificacaoSweetAlert2;
import web.condominiodigital.notification.TipoNotificaoSweetAlert2;
import web.condominiodigital.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private String resolveView(HttpServletRequest request, String viewName) {
        return "true".equals(request.getHeader("HX-Request")) ? viewName + " :: conteudo" : viewName;
    }

    @GetMapping
    public ModelAndView pesquisar(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(resolveView(request, "usuario/lista"));
        mv.addObject("usuarios", usuarioService.buscarTodos());
        return mv;
    }

    @GetMapping("/cadastrar")
    public ModelAndView abrirFormularioInclusao(Usuario usuario, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(resolveView(request, "usuario/formulario"));
        mv.addObject("usuario", usuario);
        return mv;
    }

    @PostMapping("/cadastrar")
    public ModelAndView adicionar(@Valid Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("usuario/formulario :: formulario");
            mv.addObject("usuario", usuario);
            return mv;
        }

        usuarioService.salvar(usuario);

        ModelAndView mv = new ModelAndView("usuario/formulario :: formulario");
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Sucesso!", "Usuário cadastrado com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        mv.addObject("usuario", new Usuario());
        return mv;
    }

    @GetMapping("/alterar/{codigo}")
    public ModelAndView abrirFormularioAlteracao(@PathVariable("codigo") Long codigo, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscarPorCodigo(codigo);
        // Não enviar a senha para a tela
        usuario.setPassword("");
        ModelAndView mv = new ModelAndView(resolveView(request, "usuario/formulario"));
        mv.addObject("usuario", usuario);
        return mv;
    }

    @PostMapping("/alterar/{codigo}")
    public ModelAndView alterar(@PathVariable("codigo") Long codigo, @Valid Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            usuario.setCodigo(codigo);
            ModelAndView mv = new ModelAndView("usuario/formulario :: formulario");
            mv.addObject("usuario", usuario);
            return mv;
        }

        usuario.setCodigo(codigo);
        usuarioService.salvar(usuario);

        // Limpar a senha novamente para a tela
        usuario.setPassword("");
        ModelAndView mv = new ModelAndView("usuario/formulario :: formulario");
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Sucesso!", "Usuário alterado com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        mv.addObject("usuario", usuario);
        return mv;
    }

    @DeleteMapping("/remover/{codigo}")
    public ModelAndView remover(@PathVariable("codigo") Long codigo) {
        usuarioService.excluir(codigo);
        ModelAndView mv = new ModelAndView("usuario/lista :: tabelaUsuarios");
        mv.addObject("usuarios", usuarioService.buscarTodos());
        mv.addObject("notificacao", new NotificacaoSweetAlert2("Removido!", "Usuário excluído com sucesso.", TipoNotificaoSweetAlert2.SUCCESS));
        return mv;
    }
}
