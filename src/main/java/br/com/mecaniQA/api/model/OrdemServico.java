package br.com.mecaniQA.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {

	private Long codigoUnico;
	private String nomeCliente;
	private String placaVeiculo;
	private String descricaoProblema;
	private StatusOrdemServico status;
	private List<Servico> servicos;
	private Double valorTotal;
	private LocalDateTime dataAbertura;
	private LocalDateTime dataUltimaAtualizacao;

	// Construtor private: a OS só pode ser criada pelo Builder (OrdemServico.builder()...build()).
	private OrdemServico(Builder builder) {
		this.nomeCliente = builder.nomeCliente;
		this.placaVeiculo = builder.placaVeiculo;
		this.descricaoProblema = builder.descricaoProblema;
		this.status = builder.status != null ? builder.status : StatusOrdemServico.ABERTO;
		this.servicos = builder.servicos != null ? new ArrayList<>(builder.servicos) : new ArrayList<>();
		this.dataAbertura = builder.dataAbertura != null ? builder.dataAbertura : LocalDateTime.now();
		this.dataUltimaAtualizacao = this.dataAbertura;
		this.valorTotal = calcularValorTotal();
	}

	public static Builder builder() {
		return new Builder();
	}

	// Soma o custo tabelado de todos os serviços da OS.
	public Double calcularValorTotal() {
		double total = 0.0;
		for (Servico servico : servicos) {
			total += servico.getCustoTabelado();
		}
		return total;
	}

	public Long getCodigoUnico() {
		return codigoUnico;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public String getPlacaVeiculo() {
		return placaVeiculo;
	}

	public String getDescricaoProblema() {
		return descricaoProblema;
	}

	public StatusOrdemServico getStatus() {
		return status;
	}

	public List<Servico> getServicos() {
		return servicos;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}

	public LocalDateTime getDataUltimaAtualizacao() {
		return dataUltimaAtualizacao;
	}

	public void setCodigoUnico(Long codigoUnico) {
		this.codigoUnico = codigoUnico;
	}

	public void setStatus(StatusOrdemServico status) {
		this.status = status;
		this.dataUltimaAtualizacao = LocalDateTime.now();
	}

	// Padrão Builder: classe static interna. Cada método devolve "this"
	// para permitir encadear as chamadas, e o build() chama o construtor private.
	public static class Builder {

		private String nomeCliente;
		private String placaVeiculo;
		private String descricaoProblema;
		private StatusOrdemServico status;
		private List<Servico> servicos;
		private LocalDateTime dataAbertura;

		public Builder nomeCliente(String nomeCliente) {
			this.nomeCliente = nomeCliente;
			return this;
		}

		public Builder placaVeiculo(String placaVeiculo) {
			this.placaVeiculo = placaVeiculo;
			return this;
		}

		public Builder descricaoProblema(String descricaoProblema) {
			this.descricaoProblema = descricaoProblema;
			return this;
		}

		public Builder status(StatusOrdemServico status) {
			this.status = status;
			return this;
		}

		public Builder servicos(List<Servico> servicos) {
			this.servicos = servicos;
			return this;
		}

		public Builder dataAbertura(LocalDateTime dataAbertura) {
			this.dataAbertura = dataAbertura;
			return this;
		}

		public OrdemServico build() {
			return new OrdemServico(this);
		}
	}
}
