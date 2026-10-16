package br.com.mecaniQA.api.model;

// Entidade associativa entre PedidoPecas e Peca:
// guarda qual peça entrou no pedido, a quantidade e o preço unitário no momento do pedido.
public class ItemPedido {

	private Peca peca;
	private Integer quantidade;
	private Double precoUnitario;

	public ItemPedido(Peca peca, Integer quantidade) {
		this.peca = peca;
		this.quantidade = quantidade;
		// Pedido de compra ao fornecedor: usa o preço de custo da peça.
		this.precoUnitario = peca.getPrecoCusto();
	}

	public Double calcularSubtotal() {
		return precoUnitario * quantidade;
	}

	public Peca getPeca() {
		return peca;
	}

	public void setPeca(Peca peca) {
		this.peca = peca;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(Double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}
}
