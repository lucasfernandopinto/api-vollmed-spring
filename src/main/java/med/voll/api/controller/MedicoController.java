package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder){
        var medico = new Medico(dados);
        repository.save(medico);

        // classe do Spring que realiza o encapsulamento do endereço da API
        var uri =  uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        // codigo 201
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping
    // Pageable - criar paginação dos dados
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size=10, sort={"nome"}) Pageable paginacao){
        // convertendo um page de médicos para um page de DadosListagemMedico, listando apenas os médicos ativos
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);

        // codigo 200
        return ResponseEntity.ok(page);

        // para controlar paginação e ordem de listagem, utilizar os seguintes tópicos na URL:
        // localhost:8080/medicos?page=0 - carrega a página 0
        // localhost:8080/medicos?size=1 - carrega cada página com 1 registro
        // localhost:8080/medicos?size=1&page=2 - carrega a página 2 contendo o seu respectivo único registro
        // localhost:8080/medicos?sort=nome - ordena crescente os registros por nome
        // localhost:8080/medicos?sort=nome,desc - ordena decrescente os registros por nome
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        // codigo 200
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @DeleteMapping("/{id}")
    // anotação PathVariable diz para o spring que o id do método excluir é o mesmo id informado no DeleteMapping
    // conhecido como parâmetro dinâmico
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        medico.excluir();

        //return código 204 - no content
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var medico = repository.getReferenceById(id);

        // codigo 200
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }
}
