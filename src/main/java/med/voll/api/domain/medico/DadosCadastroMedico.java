package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroMedico(
        @NotBlank // valida se o campo não é nulo e nem vazio (obs.: só para strings)
        String nome,
        @NotBlank
        @Email // valida formato de email
        String email,
        @NotBlank
        String telefone,
        @NotBlank
        @Pattern(regexp = "\\d{4,6}") // valida quantidade de dígitos (neste caso, de 4 a 6)
        String crm,
        @NotNull
        Especialidade especialidade,
        @NotNull
        @Valid // valida informações do DTO DadosEndereco
        DadosEndereco endereco
)
{ }
