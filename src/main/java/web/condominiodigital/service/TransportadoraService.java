package web.condominiodigital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.condominiodigital.model.Transportadora;
import web.condominiodigital.repository.TransportadoraRepository;

import java.util.List;

@Service
public class TransportadoraService {

    @Autowired
    private TransportadoraRepository transportadoraRepository;

    public List<Transportadora> buscarTodas() {
        return transportadoraRepository.findAll();
    }

    public Transportadora buscarPorCodigo(Long codigo) {
        return transportadoraRepository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Transportadora inválida: " + codigo));
    }

    @Transactional
    public Transportadora salvar(Transportadora transportadora) {
        return transportadoraRepository.save(transportadora);
    }

    @Transactional
    public void excluir(Long codigo) {
        // Verifica se existe antes de excluir, o HTMX vai tratar o erro caso não exista
        Transportadora t = buscarPorCodigo(codigo);
        transportadoraRepository.delete(t);
    }
}
