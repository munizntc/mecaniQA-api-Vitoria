package br.com.mecaniQA.api.mapper;

import br.com.mecaniQA.api.dto.ServicoDTO;
import br.com.mecaniQA.api.model.Servico;

public class ServicoMapper {

	public static Servico toEntity(ServicoDTO dto) {
		return new Servico(dto.getNomeServico(), dto.getTempoEstimadoMinutos(), dto.getCustoTabelado());
	}

	public static ServicoDTO toDTO(Servico servico) {
		ServicoDTO dto = new ServicoDTO();
		dto.setCodigoUnico(servico.getCodigoUnico());
		dto.setNomeServico(servico.getNomeServico());
		dto.setTempoEstimadoMinutos(servico.getTempoEstimadoMinutos());
		dto.setCustoTabelado(servico.getCustoTabelado());
		return dto;
	}
}
