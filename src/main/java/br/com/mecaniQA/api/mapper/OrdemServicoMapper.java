package br.com.mecaniQA.api.mapper;

import java.time.LocalDateTime;
import java.util.List;

import br.com.mecaniQA.api.dto.OrdemServicoDTO;
import br.com.mecaniQA.api.model.OrdemServico;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.model.StatusOrdemServico;

public class OrdemServicoMapper {

	// Monta a OS pelo Builder. Toda OS nova começa com status ABERTO.
	public static OrdemServico toEntity(OrdemServicoDTO dto, List<Servico> servicos) {
		return OrdemServico.builder()
				.nomeCliente(dto.getNomeCliente())
				.placaVeiculo(dto.getPlacaVeiculo())
				.descricaoProblema(dto.getDescricaoProblema())
				.servicos(servicos)
				.status(StatusOrdemServico.ABERTO)
				.dataAbertura(LocalDateTime.now())
				.build();
	}

	public static OrdemServicoDTO toDTO(OrdemServico os) {
		OrdemServicoDTO dto = new OrdemServicoDTO();
		dto.setCodigoUnico(os.getCodigoUnico());
		dto.setNomeCliente(os.getNomeCliente());
		dto.setPlacaVeiculo(os.getPlacaVeiculo());
		dto.setDescricaoProblema(os.getDescricaoProblema());
		dto.setStatus(os.getStatus().name());
		dto.setCodigosServicos(os.getServicos().stream().map(Servico::getCodigoUnico).toList());
		dto.setValorTotal(os.getValorTotal());
		return dto;
	}
}
