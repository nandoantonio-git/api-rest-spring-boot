package br.com.fiap3esr.autoescola3esr.exception.type;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
