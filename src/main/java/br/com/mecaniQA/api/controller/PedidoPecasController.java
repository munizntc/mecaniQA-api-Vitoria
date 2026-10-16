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
import br.com.mecaniQA.api.dto.ItemPedidoDTO;
import br.com.mecaniQA.api.dto.PedidoPecasDTO;
import br.com.mecaniQA.api.mapper.PedidoPecasMapper;
import br.com.mecaniQA.api.model.ItemPedido;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.model.PedidoPecas;
import br.com.mecaniQA.api.model.StatusPedido;
import br.com.mecaniQA.api.repository.PecaRepository;
import br.com.mecaniQA.api.repository.PedidoPecasRepository;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoPecasController {

	private final PedidoPecasRepository repository = PedidoPecasRepository.getInstance();
	// Necessário para encontrar a Peca ao adicionar itens no pedido (US04).
	private final PecaRepository pecaRepository = PecaRepository.getInstance();

	// US03 - Criar um Pedido (pode vir com itens ou vazio)
	@PostMapping
	public ResponseEntity<PedidoPecasDTO> criar(@RequestBody PedidoPecasDTO dto) {
		if (dto == null || dto.getFornecedor() == null || dto.getFornecedor().isBlank()) {
			return ResponseEntity.badRequest().build(); // 400
		}
		PedidoPecas pedido = PedidoPecasMapper.toEntity(dto);

		if (dto.getItens() != null) {
			List<ItemPedido> itens = new ArrayList<>();
			for (ItemPedidoDTO itemDTO : dto.getItens()) {
				if (!itemValido(itemDTO)) {
					return ResponseEntity.badRequest().build(); // 400
				}
				Optional<Peca> peca = pecaRepository.buscarPorId(itemDTO.getCodigoPeca());
				if (peca.isEmpty()) {
					return ResponseEntity.notFound().build(); // 404 - peça não encontrada
				}
				itens.add(PedidoPecasMapper.toItem(itemDTO, peca.get()));
			}
			for (ItemPedido item : itens) {
				pedido.adicionarItem(item);
			}
		}

		PedidoPecas salvo = repository.salvar(pedido);
		return ResponseEntity.status(HttpStatus.CREATED).body(PedidoPecasMapper.toDTO(salvo)); // 201
	}

	@GetMapping
	public ResponseEntity<List<PedidoPecasDTO>> listar() {
		List<PedidoPecasDTO> pedidos = repository.listarTodos().stream().map(PedidoPecasMapper::toDTO).toList();
		return ResponseEntity.ok(pedidos); // 200
	}

	@GetMapping("/{id}")
	public ResponseEntity<PedidoPecasDTO> buscarPorId(@PathVariable Long id) {
		Optional<PedidoPecas> pedido = repository.buscarPorId(id);
		if (pedido.isEmpty()) {
			return ResponseEntity.notFound().build(); // 404
		}
		return ResponseEntity.ok(PedidoPecasMapper.toDTO(pedido.get())); // 200
	}

	// US04 - Adicionar peças a um pedido existente
	@PostMapping("/{id}/itens")
	public ResponseEntity<PedidoPecasDTO> adicionarItem(@PathVariable Long id, @RequestBody ItemPedidoDTO dto) {
		Optional<PedidoPecas> existente = repository.buscarPorId(id);
		if (existente.isEmpty()) {
			return ResponseEntity.notFound().build(); // 404 - pedido não encontrado
		}
		if (!itemValido(dto)) {
			return ResponseEntity.badRequest().build(); // 400
		}
		Optional<Peca> peca = pecaRepository.buscarPorId(dto.getCodigoPeca());
		if (peca.isEmpty()) {
			return ResponseEntity.notFound().build(); // 404 - peça não encontrada
		}
		PedidoPecas pedido = existente.get();
		// Só dá para adicionar peças enquanto o pedido ainda está aberto (ORCANDO).
		if (pedido.getStatus() != StatusPedido.ORCANDO) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
		}
		pedido.adicionarItem(PedidoPecasMapper.toItem(dto, peca.get()));
		repository.atualizar(id, pedido);
		return ResponseEntity.ok(PedidoPecasMapper.toDTO(pedido)); // 200
	}

	// US05 - Modificar o status do Pedido
	@PutMapping("/{id}/status")
	public ResponseEntity<PedidoPecasDTO> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusDTO dto) {
		Optional<PedidoPecas> existente = repository.buscarPorId(id);
		if (existente.isEmpty()) {
			return ResponseEntity.notFound().build(); // 404
		}
		StatusPedido novoStatus;
		try {
			novoStatus = StatusPedido.valueOf(normalizarStatus(dto.getStatus()));
		} catch (IllegalArgumentException | NullPointerException e) {
			return ResponseEntity.badRequest().build(); // 400 - status inexistente
		}
		PedidoPecas pedido = existente.get();
		pedido.setStatus(novoStatus);
		repository.atualizar(id, pedido);
		return ResponseEntity.ok(PedidoPecasMapper.toDTO(pedido)); // 200
	}

	private boolean itemValido(ItemPedidoDTO dto) {
		return dto != null && dto.getCodigoPeca() != null
				&& dto.getQuantidade() != null && dto.getQuantidade() > 0;
	}

	// Aceita "PAGO_FATURADO", "Pago/Faturado", "orçando" etc.
	private String normalizarStatus(String status) {
		String semAcento = Normalizer.normalize(status.trim(), Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		return semAcento.toUpperCase().replaceAll("[\\s/-]+", "_");
	}
}
