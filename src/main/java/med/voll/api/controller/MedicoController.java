package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    // Injeção de dependências
    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    // padrão DTO com Java Record
    // @Valid utiliza do Bean Validation para verificar se as informações que chegam estão de acordo com as anotações (NotNull, NotBlank...)
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados){
        repository.save(new Medico(dados));
    }

    @GetMapping
    // Pageable - criar paginação dos dados
    public Page<DadosListagemMedico> listar(@PageableDefault(size=10, sort={"nome"}) Pageable paginacao){
        // convertendo um page de médicos para um page de DadosListagemMedico, listando apenas o médicos ativos
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);

        // para controlar paginação e ordem de listagem, utilizar os seguintes tópicos na URL:
        // localhost:8080/medicos?page=0 - carrega a página 0
        // localhost:8080/medicos?size=1 - carrega cada página com 1 registro
        // localhost:8080/medicos?size=1&page=2 - carrega a página 2 contendo o seu respectivo único registro
        // localhost:8080/medicos?sort=nome - ordena crescente os registros por nome
        // localhost:8080/medicos?sort=nome,desc - ordena decrescente os registros por nome
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id}")
    // anotação PathVariable diz para o spring que o id do método excluir é o mesmo id informado no DeleteMapping
    // conhecido como parâmetro dinâmico
    @Transactional
    public void excluir(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        medico.excluir();
    }
}
