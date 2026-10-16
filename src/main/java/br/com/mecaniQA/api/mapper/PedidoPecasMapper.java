package br.com.mecaniQA.api.mapper;

import br.com.mecaniQA.api.dto.ItemPedidoDTO;
import br.com.mecaniQA.api.dto.PedidoPecasDTO;
import br.com.mecaniQA.api.model.ItemPedido;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.model.PedidoPecas;

public class PedidoPecasMapper {

	// Todo pedido novo começa com status ORCANDO (definido no construtor de PedidoPecas).
	public static PedidoPecas toEntity(PedidoPecasDTO dto) {
		return new PedidoPecas(dto.getFornecedor());
	}

	public static PedidoPecasDTO toDTO(PedidoPecas pedidoPecas) {
		PedidoPecasDTO dto = new PedidoPecasDTO();
		dto.setCodigoUnico(pedidoPecas.getCodigoUnico());
		dto.setFornecedor(pedidoPecas.getFornecedor());
		dto.setStatus(pedidoPecas.getStatus().name());
		dto.setItens(pedidoPecas.getItens().stream().map(item -> {
			ItemPedidoDTO itemDTO = new ItemPedidoDTO();
			itemDTO.setCodigoPeca(item.getPeca().getCodigoUnico());
			itemDTO.setQuantidade(item.getQuantidade());
			return itemDTO;
		}).toList());
		dto.setValorTotal(pedidoPecas.calcularValorTotal());
		return dto;
	}

	// Cria o item (entidade associativa) a partir do DTO e da Peca já encontrada no repository.
	public static ItemPedido toItem(ItemPedidoDTO dto, Peca peca) {
		return new ItemPedido(peca, dto.getQuantidade());
	}
}
