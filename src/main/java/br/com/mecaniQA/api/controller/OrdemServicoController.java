package br.com.mecaniQA.api.controller;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mecaniQA.api.dto.AtualizarStatusDTO;
import br.com.mecaniQA.api.dto.OrdemServicoDTO;
import br.com.mecaniQA.api.mapper.OrdemServicoMapper;
import br.com.mecaniQA.api.model.OrdemServico;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.model.StatusOrdemServico;
import br.com.mecaniQA.api.repository.OrdemServicoRepository;
import br.com.mecaniQA.api.repository.ServicoRepository;

@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {

	private final OrdemServicoRepository repository = OrdemServicoRepository.getInstance();
	private final ServicoRepository servicoRepository = ServicoRepository.getInstance();

	// US01 - Criar uma Ordem de Serviço (segue o diagrama de atividade)
	@PostMapping
	public ResponseEntity<OrdemServicoDTO> criar(@RequestBody OrdemServicoDTO dto) {
		// 1. Campos obrigatórios preenchidos?
		if (dto == null
				|| vazio(dto.getNomeCliente())
				|| vazio(dto.getPlacaVeiculo())
				|| vazio(dto.getDescricaoProblema())
				|| dto.getCodigosServicos() == null || dto.getCodigosServicos().isEmpty()) {
			return ResponseEntity.badRequest().build(); // 400
		}

		// 2. Busca os serviços informados no ServicoRepository
		List<Servico> servicos = new ArrayList<>();
		for (Long codigo : dto.getCodigosServicos()) {
			Optional<Servico> servico = servicoRepository.buscarPorId(codigo);
			if (servico.isEmpty()) {
				return ResponseEntity.notFound().build(); // 404 - serviço não encontrado
			}
			servicos.add(servico.get());
		}

		// 3. Mapper monta a OS pelo Builder (status ABERTO), 4. salva, 5. converte para DTO
		OrdemServico salva = repository.salvar(OrdemServicoMapper.toEntity(dto, servicos));
		return ResponseEntity.status(HttpStatus.CREATED).body(OrdemServicoMapper.toDTO(salva)); // 201
	}

	@GetMapping
	public ResponseEntity<List<OrdemServicoDTO>> listar() {
		List<OrdemServicoDTO> ordens = repository.listarTodos().stream().map(OrdemServicoMapper::toDTO).toList();
		return ResponseEntity.ok(ordens); // 200
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrdemServicoDTO> buscarPorId(@PathVariable Long id) {
		Optional<OrdemServico> os = repository.buscarPorId(id);
		if (os.isEmpty()) {
			return ResponseEntity.notFound().build(); // 404
		}
		return ResponseEntity.ok(OrdemServicoMapper.toDTO(os.get())); // 200
	}

	// US02 - Modificar status da Ordem de Serviço
	@PutMapping("/{id}/status")
	public ResponseEntity<OrdemServicoDTO> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusDTO dto) {
		Optional<OrdemServico> existente = repository.buscarPorId(id);
		if (existente.isEmpty()) {
			return ResponseEntity.notFound().build(); // 404
		}
		StatusOrdemServico novoStatus;
		try {
			novoStatus = StatusOrdemServico.valueOf(normalizarStatus(dto.getStatus()));
		} catch (IllegalArgumentException | NullPointerException e) {
			return ResponseEntity.badRequest().build(); // 400 - status inexistente
		}
		OrdemServico os = existente.get();
		os.setStatus(novoStatus);
		repository.atualizar(id, os);
		return ResponseEntity.ok(OrdemServicoMapper.toDTO(os)); // 200
	}

	// Aceita "EM_EXECUCAO", "em execução", "Em Execucao" etc.
	private String normalizarStatus(String status) {
		String semAcento = Normalizer.normalize(status.trim(), Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		return semAcento.toUpperCase().replaceAll("[\\s/-]+", "_");
	}

	private boolean vazio(String texto) {
		return texto == null || texto.isBlank();
	}
}
