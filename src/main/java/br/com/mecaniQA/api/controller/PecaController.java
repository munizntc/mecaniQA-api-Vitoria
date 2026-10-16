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

import br.com.mecaniQA.api.dto.PecaDTO;
import br.com.mecaniQA.api.mapper.PecaMapper;
import br.com.mecaniQA.api.model.CategoriaPeca;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.repository.PecaRepository;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

	// Sem injeção de dependência: o repository é obtido pelo getInstance() do Singleton.
	private final PecaRepository repository = PecaRepository.getInstance();

	// US01 - Cadastro de nova Peça
	@PostMapping
	public ResponseEntity<PecaDTO> cadastrar(@RequestBody PecaDTO dto) {
		if (!dadosValidos(dto)) {
			return ResponseEntity.badRequest().build(); // 400
		}
		Peca salva = repository.salvar(PecaMapper.toEntity(dto));
		return ResponseEntity.status(HttpStatus.CREATED).body(PecaMapper.toDTO(salva)); // 201
	}

	// US02 - Listar catálogo de peças
	@GetMapping
	public ResponseEntity<List<PecaDTO>> listar() {
		List<PecaDTO> pecas = repository.listarTodos().stream().map(PecaMapper::toDTO).toList();
		return ResponseEntity.ok(pecas); // 200
	}

	// US02 - Buscar uma peça específica pelo código único
	@GetMapping("/{id}")
	public ResponseEntity<PecaDTO> buscarPorId(@PathVariable Long id) {
		Optional<Peca> peca = repository.buscarPorId(id);
		if (peca.isEmpty()) {
			return ResponseEntity.notFound().build(); // 404
		}
		return ResponseEntity.ok(PecaMapper.toDTO(peca.get())); // 200
	}

	// US03 - Atualizar peça do catálogo
	@PutMapping("/{id}")
	public ResponseEntity<PecaDTO> atualizar(@PathVariable Long id, @RequestBody PecaDTO dto) {
		Optional<Peca> existente = repository.buscarPorId(id);
		if (existente.isEmpty()) {
			return ResponseEntity.notFound().build(); // 404
		}
		if (!dadosValidos(dto)) {
			return ResponseEntity.badRequest().build(); // 400
		}
		Peca pecaAtualizada = PecaMapper.toEntity(dto);
		pecaAtualizada.setDataCadastro(existente.get().getDataCadastro()); // mantém a data original
		pecaAtualizada.setDataUltimaAtualizacao(LocalDateTime.now());
		Peca salva = repository.atualizar(id, pecaAtualizada).get();
		return ResponseEntity.ok(PecaMapper.toDTO(salva)); // 200
	}

	// US04 - Excluir peça do catálogo
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		if (!repository.deletar(id)) {
			return ResponseEntity.notFound().build(); // 404
		}
		return ResponseEntity.noContent().build(); // 204
	}

	// Confere os campos obrigatórios (tamanho e cor são opcionais).
	private boolean dadosValidos(PecaDTO dto) {
		if (dto == null
				|| vazio(dto.getNome())
				|| vazio(dto.getCodigoBarras())
				|| vazio(dto.getFornecedorMarca())
				|| dto.getQuantidadeEstoque() == null || dto.getQuantidadeEstoque() < 0
				|| dto.getPrecoCusto() == null || dto.getPrecoCusto() < 0
				|| dto.getPrecoVenda() == null || dto.getPrecoVenda() < 0
				|| vazio(dto.getCategoria())) {
			return false;
		}
		try {
			CategoriaPeca.valueOf(dto.getCategoria().trim().toUpperCase());
			return true;
		} catch (IllegalArgumentException e) {
			return false; // categoria que não existe no enum
		}
	}

	private boolean vazio(String texto) {
		return texto == null || texto.isBlank();
	}
}
