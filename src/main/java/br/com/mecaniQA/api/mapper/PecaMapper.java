package br.com.mecaniQA.api.mapper;

import br.com.mecaniQA.api.dto.PecaDTO;
import br.com.mecaniQA.api.model.CategoriaPeca;
import br.com.mecaniQA.api.model.Peca;

public class PecaMapper {

	// Converte o DTO recebido na requisição para a entidade Peca.
	// Lança IllegalArgumentException se a categoria não existir no enum.
	public static Peca toEntity(PecaDTO dto) {
		Peca peca = new Peca(
				dto.getNome(),
				dto.getCodigoBarras(),
				dto.getFornecedorMarca(),
				dto.getQuantidadeEstoque(),
				dto.getPrecoCusto(),
				dto.getPrecoVenda(),
				CategoriaPeca.valueOf(dto.getCategoria().trim().toUpperCase()));
		peca.setTamanho(dto.getTamanho());
		peca.setCor(dto.getCor());
		return peca;
	}

	public static PecaDTO toDTO(Peca peca) {
		PecaDTO dto = new PecaDTO();
		dto.setCodigoUnico(peca.getCodigoUnico());
		dto.setNome(peca.getNome());
		dto.setCodigoBarras(peca.getCodigoBarras());
		dto.setFornecedorMarca(peca.getFornecedorMarca());
		dto.setQuantidadeEstoque(peca.getQuantidadeEstoque());
		dto.setPrecoCusto(peca.getPrecoCusto());
		dto.setPrecoVenda(peca.getPrecoVenda());
		dto.setCategoria(peca.getCategoria().name());
		dto.setTamanho(peca.getTamanho());
		dto.setCor(peca.getCor());
		return dto;
	}
}
