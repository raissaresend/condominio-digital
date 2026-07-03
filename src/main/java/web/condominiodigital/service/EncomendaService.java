package web.condominiodigital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.condominiodigital.model.Encomenda;
import web.condominiodigital.repository.EncomendaRepository;

@Service
public class EncomendaService {

    @Autowired
    private EncomendaRepository encomendaRepository;

    public Page<Encomenda> buscarTodasPaginado(Pageable pageable) {
        return encomendaRepository.findAllWithRelationships(pageable);
    }

    public Encomenda buscarPorCodigo(Long codigo) {
        return encomendaRepository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Encomenda inválida: " + codigo));
    }

    @Transactional
    public Encomenda salvar(Encomenda encomenda) {
        return encomendaRepository.save(encomenda);
    }

    @Transactional
    public void excluir(Long codigo) {
        Encomenda e = buscarPorCodigo(codigo);
        encomendaRepository.delete(e);
    }
}
