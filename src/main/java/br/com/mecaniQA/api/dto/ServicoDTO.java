package br.com.mecaniQA.api.dto;

public class ServicoDTO {

	private Long codigoUnico;
	private String nomeServico;
	private Integer tempoEstimadoMinutos;
	private Double custoTabelado;

	public ServicoDTO() {
	}

	public Long getCodigoUnico() {
		return codigoUnico;
	}

	public void setCodigoUnico(Long codigoUnico) {
		this.codigoUnico = codigoUnico;
	}

	public String getNomeServico() {
		return nomeServico;
	}

	public void setNomeServico(String nomeServico) {
		this.nomeServico = nomeServico;
	}

	public Integer getTempoEstimadoMinutos() {
		return tempoEstimadoMinutos;
	}

	public void setTempoEstimadoMinutos(Integer tempoEstimadoMinutos) {
		this.tempoEstimadoMinutos = tempoEstimadoMinutos;
	}

	public Double getCustoTabelado() {
		return custoTabelado;
	}

	public void setCustoTabelado(Double custoTabelado) {
		this.custoTabelado = custoTabelado;
	}
}
