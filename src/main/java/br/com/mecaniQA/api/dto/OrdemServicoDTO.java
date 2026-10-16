package br.com.mecaniQA.api.dto;

import java.util.List;

public class OrdemServicoDTO {

	private Long codigoUnico;
	private String nomeCliente;
	private String placaVeiculo;
	private String descricaoProblema;
	private String status;
	private List<Long> codigosServicos;
	private Double valorTotal;

	public OrdemServicoDTO() {
	}

	public Long getCodigoUnico() {
		return codigoUnico;
	}

	public void setCodigoUnico(Long codigoUnico) {
		this.codigoUnico = codigoUnico;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getPlacaVeiculo() {
		return placaVeiculo;
	}

	public void setPlacaVeiculo(String placaVeiculo) {
		this.placaVeiculo = placaVeiculo;
	}

	public String getDescricaoProblema() {
		return descricaoProblema;
	}

	public void setDescricaoProblema(String descricaoProblema) {
		this.descricaoProblema = descricaoProblema;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Long> getCodigosServicos() {
		return codigosServicos;
	}

	public void setCodigosServicos(List<Long> codigosServicos) {
		this.codigosServicos = codigosServicos;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}
}
