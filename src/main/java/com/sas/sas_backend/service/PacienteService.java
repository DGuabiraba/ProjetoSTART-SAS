package com.sas.sas_backend.service;

import com.sas.sas_backend.dtos.PacienteDto;
import com.sas.sas_backend.exceptions.paciente.CredentialsNotMatchException;
import com.sas.sas_backend.exceptions.paciente.PacienteAlreadyExistsException;
import com.sas.sas_backend.exceptions.paciente.PacienteNotFoundException;
import com.sas.sas_backend.mappers.PacienteMapper;
import com.sas.sas_backend.models.Paciente;
import com.sas.sas_backend.models.enumerated.RolesEnum;
import com.sas.sas_backend.repository.PacienteRepository;
import com.sas.sas_backend.utils.password.HashPassword;
import com.sas.sas_backend.utils.password.PasswordHashResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Importação adicionada

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;
    private final TokenService tokenService;
    // private final ExameMapper exameMapper; // Não usado neste serviço, pode remover se não for usar

    public PacienteDto buscarPorCpf(String cpf) {
        Paciente pacienteCPF = pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new PacienteNotFoundException(String.format("Paciente com CPF '%s' não encontrado.", cpf)));
        return pacienteMapper.toPacienteDto(pacienteCPF);
    }

    public String loginPaciente(String email, String password) {
        Paciente user = pacienteRepository.findByEmail(email)
                .orElseThrow(() -> new CredentialsNotMatchException("Email ou senha inválidos.")); // Mensagem mais genérica por segurança

        boolean passwordMatch = HashPassword.checkPassword(password, user.getSenha(), user.getSalt());
        if (!passwordMatch) {
            throw new CredentialsNotMatchException("Email ou senha inválidos."); // Mensagem mais genérica
        }
        return tokenService.gerarToken(user.getId(), RolesEnum.PACIENTE.getNome());
    }

    @Transactional
    public PacienteDto cadastrarPaciente(PacienteDto dto) {
        if (pacienteRepository.findByEmailOrCpf(dto.email(), dto.cpf())) {
            throw new PacienteAlreadyExistsException("Email ou CPF já cadastrado.");
        }

        Paciente paciente = pacienteMapper.toPaciente(dto);

        // Gera hash e salt para a senha
        PasswordHashResponse response = HashPassword.hashPassword(paciente.getSenha());
        paciente.setSenha(response.hash());
        paciente.setSalt(response.salt());

        // Define como ativo=true após o cadastro, se não houver um fluxo de ativação por email.
        // Se houver ativação por email, 'ativo' deve ser false e ser ativado por um link.
        // Para este exemplo, manterei `false` como está no modelo, assumindo um processo de ativação.
        // paciente.setAtivo(false); // Já é o padrão se não for definido no construtor
        // paciente.setTokenAtivacao(...); // Se houver token de ativação.
        // paciente.setDataExpiracaoToken(...);

        Paciente savedPaciente = pacienteRepository.save(paciente);
        return pacienteMapper.toPacienteDto(savedPaciente);
    }

    @Transactional
    public void alterarSenha(String id, String senhaAtual, String novaSenha) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNotFoundException(String.format("Paciente com ID '%s' não encontrado.", id)));

        boolean senhaCorreta = HashPassword.checkPassword(senhaAtual, paciente.getSenha(), paciente.getSalt());
        if (!senhaCorreta) {
            throw new CredentialsNotMatchException("Senha atual incorreta.");
        }

        PasswordHashResponse novaHash = HashPassword.hashPassword(novaSenha);
        paciente.setSenha(novaHash.hash());
        paciente.setSalt(novaHash.salt());

        pacienteRepository.save(paciente);
    }

    public List<PacienteDto> buscarTodos() {
        List<Paciente> listPaciente = pacienteRepository.findAll();
        // Retorna lista vazia se não encontrar, em vez de lançar exceção
        return listPaciente.stream().map(pacienteMapper::toPacienteDto).collect(Collectors.toList());
    }

    @Transactional
    public PacienteDto atualizarPaciente(String id, PacienteDto dto) {
        Paciente pacienteExistente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNotFoundException(String.format("Paciente com ID '%s' não encontrado para atualização.", id)));

        // Mapeia o DTO para a entidade existente, atualizando apenas os campos necessários.
        // Se o PacienteMapper tiver um método de merge, seria ideal.
        // Por enquanto, faça a cópia manual das propriedades que podem ser atualizadas
        pacienteExistente.setNome(dto.nome());
        pacienteExistente.setEmail(dto.email());
        // Não atualize a senha diretamente aqui, use o método alterarSenha
        pacienteExistente.setDataNascimento(dto.dataNascimento());
        pacienteExistente.setGenero(dto.genero());
        pacienteExistente.setTelefone(dto.telefone());
        pacienteExistente.setGrauInstrucao(dto.grauInstrucao());
        pacienteExistente.setNotificacoesAtivadas(dto.notificacoesAtivadas());

        // Se o endereço for nulo no DTO, pode significar que não deve ser atualizado
        // Ou que deve ser desassociado/excluído, dependendo da sua regra de negócio.
        // Aqui, assumimos que se o DTO de endereço estiver presente, ele deve ser atualizado ou criado.
        if (dto.endereco() != null) {
            if (pacienteExistente.getEndereco() != null) {
                // Atualiza o endereço existente
                pacienteExistente.getEndereco().setRua(dto.endereco().rua());
                pacienteExistente.getEndereco().setComplemento(dto.endereco().complemento());
                pacienteExistente.getEndereco().setNumero(dto.endereco().numero());
                pacienteExistente.getEndereco().setBairro(dto.endereco().bairro());
                pacienteExistente.getEndereco().setCidade(dto.endereco().cidade());
                pacienteExistente.getEndereco().setUf(dto.endereco().uf());
                pacienteExistente.getEndereco().setCep(dto.endereco().cep());
            } else {
                // Cria um novo endereço e associa ao paciente
                pacienteExistente.setEndereco(pacienteMapper.toPaciente(dto).getEndereco());
            }
        } else if (pacienteExistente.getEndereco() != null) {
            // Se o DTO de endereço é nulo, mas o paciente tem um endereço,
            // você pode querer desassociá-lo ou excluí-lo.
            // Dependendo da sua regra de negócio, você pode definir:
            // pacienteExistente.setEndereco(null);
            // Ou chamar um serviço para deletar o endereço se for uma exclusão em cascata separada.
        }

        // Não atualize o CPF aqui, pois é um identificador único e geralmente não deve ser alterado.
        // Se precisar alterar o CPF, isso deve ser um processo separado e mais complexo.

        return pacienteMapper.toPacienteDto(pacienteRepository.save(pacienteExistente));
    }

    @Transactional
    public void deletarPaciente(String id) {
        if (!pacienteRepository.existsById(id)) {
            throw new PacienteNotFoundException(String.format("Paciente com ID '%s' não encontrado para exclusão.", id));
        }
        pacienteRepository.deleteById(id);
    }
}