package br.com.fiap3esr.autoescola3esr.adapter.out.repository.mapper;

import br.com.fiap3esr.autoescola3esr.adapter.out.repository.entity.InstrutorEntity;
import br.com.fiap3esr.autoescola3esr.application.core.domain.Instrutor;
import org.springframework.stereotype.Component;

@Component
public class InstrutorEntityMapper {
    public Instrutor toDomain(InstrutorEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Instrutor(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getCnh(),
                entity.getEspecialidade(),
                entity.getEndereco(),
                entity.isAtivo()
        );
    }

    public InstrutorEntity toEntity(Instrutor domain) {
        return new InstrutorEntity(
                domain.getId(),
                domain.getNome(),
                domain.getEmail(),
                domain.getTelefone(),
                domain.getCnh(),
                domain.getEspecialidade(),
                domain.getEndereco(),
                domain.isAtivo()
        );
    }
}