# 📌 Sistema de Agendamento a Saúde

<p> Somos a SAS (Sistema de Agendamento de Saúde), uma startup de tecnologia dedicada a transformar a experiência na área da saúde por meio de soluções digitais inovadoras. Nosso propósito é simplificar e agilizar o agendamento e o atendimento médico, proporcionando praticidade para os pacientes e eficiência para os profissionais da saúde.

<b>Praticidade para Pacientes:</b> Agendamento rápido, acesso a prontuários digitais e informações centralizadas.

<b>Eficiência para Profissionais:</b> Gestão simplificada de consultas, integração de históricos médicos e otimização de
processos.

<b>Inovação Contínua:</b> Soluções personalizadas e atualizadas para atender às necessidades do setor de saúde.

Na SAS, acreditamos que a tecnologia pode ser uma grande aliada para melhorar a saúde e o bem-estar da sociedade.

# 🚀 Tecnologias Utilizadas

- Frontend: HTML,CSS, Javascript e React.js
- Backend: Java (Spring Boot)
- Banco de Dados: MySQL
- Notificações: SMS/WhatsApp
- Docker

# 📑 Modelo Entidade de Relacionamento (EER)

```mermaid
erDiagram
    %% Entidades Principais
    PACIENTE {
        string id_paciente PK
        string nome
        string cpf
        string email
        string senha
        date data_nascimento
        enum genero
        string telefone
        string salt
        string endereco_id FK
        timestamp criado_em
        timestamp atualizado_em
        string grau_instrucao
        bit notificacoes_ativadas
        bit ativo
        datetime data_expiracao_token
        string token_ativacao
    }

    ENDERECO {
        string id_endereco PK
        string rua
        string complemento
        string numero
        string bairro
        string cidade
        string uf
        string cep
        datetime criado_em
        datetime atualizado_em
    }

    UNIDADE_SAUDE {
        string id_unidade PK
        string nome
        enum tipo
        string cnpj
        string salt
        string email_unidade
        string senha_unidade
        string endereco_id FK
        timestamp criado_em
        timestamp atualizado_em
    }

    ESPECIALIDADE {
        int id_especialidade PK
        string nome
        timestamp criado_em
        timestamp atualizado_em
        text descricao
    }

    %% Entidades Secundárias
    PROFISSIONAL {
        string id_profissional PK
        string nome
        string telefone
        string email
        string registro
        string senha
        string salt
        int especialidade_id FK
        string unidade_id FK
        timestamp criado_em
        timestamp atualizado_em
        tinyint especialidade
    }

    AGENDAMENTO {
        string id_agendamento PK
        string paciente_id FK
        string profissional_id FK
        string unidade_id FK
        enum status
        datetime data_hora_inicio
        datetime data_hora_fim
        timestamp criado_em
        timestamp atualizado_em
        text observacoes
    }

    PRONTUARIO {
        string id_prontuario PK
        string profissional_id FK
        string paciente_id FK
        string tipo_sanguineo
        string alergias
        string doencas_cronicas
        text observacoes
        timestamp criado_em
        timestamp atualizado_em
        string descricao
        text historico_familiar
    }

    EXAME {
        string id_exame PK
        string prontuario_id FK
        string paciente_id FK
        string profissional_id FK
        string agendamento_id FK
        string tipo_exame
        text descricao
        date data_solicitacao
        date data_realizacao
        text resultado
        enum status
        timestamp criado_em
        timestamp atualizado_em
    }

    %% Relacionamentos
    PACIENTE }|--|| ENDERECO : "reside"
    UNIDADE_SAUDE }|--|| ENDERECO : "localizada"
    PROFISSIONAL }|--|| ESPECIALIDADE : "possui"
    PROFISSIONAL }|--|| UNIDADE_SAUDE : "vinculado"
    PACIENTE ||--o{ AGENDAMENTO : "realiza"
    PROFISSIONAL ||--o{ AGENDAMENTO : "atende"
    UNIDADE_SAUDE ||--o{ AGENDAMENTO : "recebe"
    PACIENTE ||--|| PRONTUARIO : "contém"
    PROFISSIONAL ||--o{ PRONTUARIO : "elabora"
    PRONTUARIO ||--o{ EXAME : "registra"
    AGENDAMENTO ||--|| EXAME : "gera"

```

# 📦 Instalação e Configuração

🔧 Pré-requisitos
Antes de começar, certifique-se de ter as seguintes ferramentas instaladas:

- Node.js
- Java JDK 21
- MySQL
- Docker
- React.js

# 🎯 Passos para rodar o projeto🔹Backend (Java)

# 🔗 Clone o repositório

```git
git clone https://github.com/SAS-Organizacao/SAS_BackEnd
```

# 🛠️ Endpoints da API

<p>Em construção...</p>

# 📌 Autenticação

<p>Em construção...</p>

# 📌 Agendamentos

<p>Em construção...</p>

# 📌 Prontuário

<p>Em construção...</p>

# 📌 Contato

📧Email: sas@gmail.com

🌐Site: www.sas.com.br

# Integrantes

</tr>
  <tr align=center>
    <td>
      <a href="https://github.com/DGuabiraba">
        <img src="https://avatars.githubusercontent.com/u/81264511?v=4" height="200px" width="200px">
      </a>
    </td>
    <td>
      <a href="https://github.com/Gabrielteles001">
        <img src="https://avatars.githubusercontent.com/u/127240150?v=4" height="200px" width="200px">
      </a>
    </td>
    <td>
      <a href="https://github.com/WalterSantos08">
        <img src="https://avatars.githubusercontent.com/u/178443270?v=4" height="200px" width="200px">
      </a>
    </td>
    <td>
      <a href="https://github.com/LeonardoIrineu">
        <img src="https://avatars.githubusercontent.com/u/112736650?v=4" height="200px" width="200px">
      </a>
    </td>
    <td>
      <a href="https://github.com/dorotrodrigues">
        <img src="https://avatars.githubusercontent.com/u/111395320?v=4" height="200px" width="200px">
      </a>
    </td>
    <td>
      <a href="https://github.com/alvesrafaelaa">
        <img src="https://avatars.githubusercontent.com/u/192259118?v=4" height="200px" width="200px">
      </a>
    </td>
    <td>
      <a href="https://github.com/CeloDigital">
        <img src="https://avatars.githubusercontent.com/u/147448840?v=4" height="200px" width="200px">
      </a>
    </td>
     <td>
      <a href="https://github.com/mattheus536">
        <img src="https://avatars.githubusercontent.com/u/171884376?v=4" height="200px" width="200px">
      </a>
    </td>
    
    
"# ProjetoSTART-SAS" 
"# ProjetoSTART-SAS" 
