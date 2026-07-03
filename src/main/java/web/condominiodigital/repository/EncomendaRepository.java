package web.condominiodigital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.condominiodigital.model.Encomenda;

public interface EncomendaRepository extends JpaRepository<Encomenda, Long> {

    @EntityGraph(attributePaths = {"unidade", "transportadora"})
    @Query("SELECT e FROM Encomenda e")
    Page<Encomenda> findAllWithRelationships(Pageable pageable);
}
