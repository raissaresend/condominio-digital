package web.condominiodigital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.condominiodigital.model.AvisoMural;
import web.condominiodigital.repository.AvisoMuralRepository;

import java.util.List;

@Service
public class AvisoMuralService {

    @Autowired
    private AvisoMuralRepository avisoMuralRepository;

    public List<AvisoMural> buscarTodos() {
        return avisoMuralRepository.findAll();
    }

    public AvisoMural buscarPorCodigo(Long codigo) {
        return avisoMuralRepository.findById(codigo).orElse(null);
    }

    public void salvar(AvisoMural aviso) {
        avisoMuralRepository.save(aviso);
    }

    public void excluir(Long codigo) {
        avisoMuralRepository.deleteById(codigo);
    }
}
