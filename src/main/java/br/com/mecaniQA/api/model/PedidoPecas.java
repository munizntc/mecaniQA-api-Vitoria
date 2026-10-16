package br.com.mecaniQA.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoPecas {

	private Long codigoUnico;
	private String fornecedor;
	private StatusPedido status;
	private List<ItemPedido> itens;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataUltimaAtualizacao;

	public PedidoPecas(String fornecedor) {
		this.fornecedor = fornecedor;
		this.status = StatusPedido.ORCANDO;
		this.itens = new ArrayList<>();
		this.dataCriacao = LocalDateTime.now();
		this.dataUltimaAtualizacao = this.dataCriacao;
	}

	// Se a peça já estiver no pedido, soma a quantidade em vez de duplicar o item.
	public void adicionarItem(ItemPedido item) {
		for (ItemPedido existente : itens) {
			if (existente.getPeca().getCodigoUnico().equals(item.getPeca().getCodigoUnico())) {
				existente.setQuantidade(existente.getQuantidade() + item.getQuantidade());
				this.dataUltimaAtualizacao = LocalDateTime.now();
				return;
			}
		}
		itens.add(item);
		this.dataUltimaAtualizacao = LocalDateTime.now();
	}

	public Double calcularValorTotal() {
		double total = 0.0;
		for (ItemPedido item : itens) {
			total += item.calcularSubtotal();
		}
		return total;
	}

	public Long getCodigoUnico() {
		return codigoUnico;
	}

	public void setCodigoUnico(Long codigoUnico) {
		this.codigoUnico = codigoUnico;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public StatusPedido getStatus() {
		return status;
	}

	public void setStatus(StatusPedido status) {
		this.status = status;
		this.dataUltimaAtualizacao = LocalDateTime.now();
	}

	public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataUltimaAtualizacao() {
		return dataUltimaAtualizacao;
	}

	public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) {
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}
}
