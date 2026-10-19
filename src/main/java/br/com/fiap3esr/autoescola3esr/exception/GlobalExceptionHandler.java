package br.com.fiap3esr.autoescola3esr.exception;

import br.com.fiap3esr.autoescola3esr.exception.type.ExternalServiceException;
import br.com.fiap3esr.autoescola3esr.exception.type.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Stream;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({EntityNotFoundException.class,
            br.com.fiap3esr.autoescola3esr.exception.type.AlunoNotFoundException.class,
            br.com.fiap3esr.autoescola3esr.exception.type.InstrutorNotFoundException.class})
    public ResponseEntity<Void> tratarNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Stream<DadosBadRequest>> tratarBadRequest(MethodArgumentNotValidException e) {
        List<FieldError> erros = e.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosBadRequest::new));
    }

    @ExceptionHandler({ValidacaoException.class, ExternalServiceException.class})
    public ResponseEntity<DadosBadRequest> tratarRegraDeNegocio(RuntimeException e) {
        return ResponseEntity.unprocessableEntity().body(new DadosBadRequest("regra", e.getMessage()));
    }

    private record DadosBadRequest(String campo, String mensagem) {
        public DadosBadRequest(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
