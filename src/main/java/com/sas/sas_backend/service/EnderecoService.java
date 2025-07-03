package com.sas.sas_backend.service;

import com.sas.sas_backend.dtos.EnderecoDto;
import com.sas.sas_backend.exceptions.endereco.EnderecoNotFoundException; // Nova exceção
import com.sas.sas_backend.mappers.EnderecoMapper;
import com.sas.sas_backend.models.Endereco;
import com.sas.sas_backend.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importação adicionada

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoMapper enderecoMapper;
    private final EnderecoRepository enderecoRepository;

    @Transactional
    public EnderecoDto cadastrarEndereco(EnderecoDto dto) {
        Endereco endereco = enderecoMapper.toEndereco(dto);
        Endereco enderecoSalvo = enderecoRepository.save(endereco);
        return enderecoMapper.toEnderecoDto(enderecoSalvo);
    }

    @Transactional
    public EnderecoDto atualizarEndereco(String id, EnderecoDto dto) {
        Endereco enderecoExistente = enderecoRepository.findById(id)
                .orElseThrow(() -> new EnderecoNotFoundException(String.format("Endereço com ID '%s' não encontrado para atualização.", id)));

        // Mapeia o DTO para uma nova instância de Endereco
        Endereco enderecoAtualizado = enderecoMapper.toEndereco(dto);
        // Define o ID da entidade existente para garantir que o JPA atualize a entidade correta
        enderecoAtualizado.setId(enderecoExistente.getId());

        return enderecoMapper.toEnderecoDto(enderecoRepository.save(enderecoAtualizado));
    }

    @Transactional
    public void deletarEndereco(String id) {
        // Verifica se o endereço existe antes de tentar deletar
        if (!enderecoRepository.existsById(id)) { // Usar existsById é mais eficiente que findById().orElseThrow() para apenas verificar existência
            throw new EnderecoNotFoundException(String.format("Endereço com ID '%s' não encontrado para exclusão.", id));
        }
        enderecoRepository.deleteById(id);
    }
}