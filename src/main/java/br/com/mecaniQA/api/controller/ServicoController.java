package br.com.mecaniQA.api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mecaniQA.api.dto.ServicoDTO;
import br.com.mecaniQA.api.mapper.ServicoMapper;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.repository.ServicoRepository;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

	private final ServicoRepository repository = ServicoRepository.getInstance();

	// US05 - Cadastro de novo Serviço
	@PostMapping
	public ResponseEntity<ServicoDTO> cadastrar(@RequestBody ServicoDTO dto) {
		if (!dadosValidos(dto)) {
			return ResponseEntity.badRequest().build(); // 400
		}
		Servico salvo = repository.salvar(ServicoMapper.toEntity(dto));
		return ResponseEntity.status(HttpStatus.CREATED).body(ServicoMapper.toDTO(salvo)); // 201
	}

	// US06 - Listar catálogo de serviços
	@GetMapping
	public ResponseEntity<List<ServicoDTO>> listar() {
		List<ServicoDTO> servicos = repository.listarTodos().stream().map(ServicoMapper::toDTO).toList();
		return ResponseEntity.ok(servicos); // 200
	}

	@GetMapping("/{id}")
	public ResponseEntity<ServicoDTO> buscarPorId(@PathVariable Long id) {
		Optional<Servico> servico = repository.buscarPorId(id);
		if (servico.isEmpty()) {
			return ResponseEntity.notFound().build(); // 404
		}
		return ResponseEntity.ok(ServicoMapper.toDTO(servico.get())); // 200
	}

	// US07 - Atualizar serviço do catálogo
	@PutMapping("/{id}")
	public ResponseEntity<ServicoDTO> atualizar(@PathVariable Long id, @RequestBody ServicoDTO dto) {
		Optional<Servico> existente = repository.buscarPorId(id);
		if (existente.isEmpty()) {
			return ResponseEntity.notFound().build(); // 404
		}
		if (!dadosValidos(dto)) {
			return ResponseEntity.badRequest().build(); // 400
		}
		Servico servicoAtualizado = ServicoMapper.toEntity(dto);
		servicoAtualizado.setDataCriacao(existente.get().getDataCriacao()); // mantém a data original
		servicoAtualizado.setDataUltimaAtualizacao(LocalDateTime.now());
		Servico salvo = repository.atualizar(id, servicoAtualizado).get();
		return ResponseEntity.ok(ServicoMapper.toDTO(salvo)); // 200
	}

	// US08 - Excluir serviço do catálogo
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		if (!repository.deletar(id)) {
			return ResponseEntity.notFound().build(); // 404
		}
		return ResponseEntity.noContent().build(); // 204
	}

	private boolean dadosValidos(ServicoDTO dto) {
		return dto != null
				&& dto.getNomeServico() != null && !dto.getNomeServico().isBlank()
				&& dto.getTempoEstimadoMinutos() != null && dto.getTempoEstimadoMinutos() > 0
				&& dto.getCustoTabelado() != null && dto.getCustoTabelado() >= 0;
	}
}
