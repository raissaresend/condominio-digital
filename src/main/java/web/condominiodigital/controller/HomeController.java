package web.condominiodigital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import web.condominiodigital.service.AvisoMuralService;

@Controller
public class HomeController {

    @Autowired
    private AvisoMuralService avisoMuralService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("avisos", avisoMuralService.buscarTodos());
        return "home/index";
    }
}
