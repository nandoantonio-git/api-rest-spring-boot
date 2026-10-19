package br.com.fiap3esr.autoescola3esr.application.service;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.usuario.DadosAtualizacaoUsuario;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.usuario.DadosCadastroUsuario;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.usuario.DadosDetalhamentoUsuario;
import br.com.fiap3esr.autoescola3esr.application.core.domain.Usuario;
import br.com.fiap3esr.autoescola3esr.application.port.out.UsuarioRepository;
import br.com.fiap3esr.autoescola3esr.exception.type.UsuarioNotFoundException;
import br.com.fiap3esr.autoescola3esr.exception.type.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public DadosDetalhamentoUsuario cadastrar(DadosCadastroUsuario dados) {
        validarLoginDisponivel(dados.login());
        Usuario usuario = new Usuario(null, dados.login(), passwordEncoder.encode(dados.senha()), dados.perfil());
        return new DadosDetalhamentoUsuario(repository.save(usuario));
    }

    @Transactional(readOnly = true)
    public Page<DadosDetalhamentoUsuario> listar(Pageable pageable) {
        return repository.findAllByAtivoTrue(pageable).map(DadosDetalhamentoUsuario::new);
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoUsuario detalhar(Long id) {
        return new DadosDetalhamentoUsuario(buscar(id));
    }

    @Transactional
    public DadosDetalhamentoUsuario atualizar(DadosAtualizacaoUsuario dados) {
        Usuario usuario = buscar(dados.id());
        if (dados.login() != null && !dados.login().equals(usuario.getLogin())) {
            validarLoginDisponivel(dados.login());
        }
        String senha = dados.senha() == null || dados.senha().isBlank() ? null : passwordEncoder.encode(dados.senha());
        usuario.atualizarInformacoes(dados.login(), senha, dados.perfil());
        return new DadosDetalhamentoUsuario(repository.save(usuario));
    }

    @Transactional
    public void excluir(Long id) {
        Usuario usuario = buscar(id);
        usuario.excluir();
        repository.save(usuario);
    }

    private void validarLoginDisponivel(String login) {
        if (repository.existsByLogin(login)) {
            throw new ValidacaoException("Login informado já está em uso!");
        }
    }

    private Usuario buscar(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new UsuarioNotFoundException("ID do usuário informado não existe!"));
    }
}
