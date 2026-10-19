package br.com.fiap3esr.autoescola3esr.exception.type;

public class AlunoNotFoundException extends RuntimeException {
    public AlunoNotFoundException(String message) {
        super(message);
    }
}