package br.com.fiap3esr.autoescola3esr.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public interface StdFeaturesPort <C, R, U, D, DET, ID> {
    ResponseEntity<DET> cadastrar(C dados, UriComponentsBuilder uriBuilder);

    ResponseEntity<Page<R>> listar(Pageable paginacao);

    ResponseEntity<DET> detalhar(ID id);

    ResponseEntity<DET> atualizar(U dados);

    ResponseEntity<D> excluir(ID id);
}