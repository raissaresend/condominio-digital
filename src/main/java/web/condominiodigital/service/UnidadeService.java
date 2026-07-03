package web.condominiodigital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.condominiodigital.model.Unidade;
import web.condominiodigital.repository.UnidadeRepository;

import java.util.List;

@Service
public class UnidadeService {

    @Autowired
    private UnidadeRepository unidadeRepository;

    public List<Unidade> buscarTodas() {
        return unidadeRepository.findAll();
    }

    public Unidade buscarPorCodigo(Long codigo) {
        return unidadeRepository.findById(codigo).orElse(null);
    }

    public void salvar(Unidade unidade) {
        unidadeRepository.save(unidade);
    }

    public void excluir(Long codigo) {
        unidadeRepository.deleteById(codigo);
    }
}
