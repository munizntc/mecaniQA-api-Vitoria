package br.com.mecaniQA.api.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.mecaniQA.api.model.PedidoPecas;

// Singleton em memória, igual aos repositories da OAT 1.
public class PedidoPecasRepository {

	private static PedidoPecasRepository instance;

	private final List<PedidoPecas> tabelaPedidos;
	private Long contadorId;

	private PedidoPecasRepository() {
		this.tabelaPedidos = new ArrayList<>();
		this.contadorId = 1L;
	}

	public static synchronized PedidoPecasRepository getInstance() {
		if (instance == null) {
			instance = new PedidoPecasRepository();
		}
		return instance;
	}

	public PedidoPecas salvar(PedidoPecas pedidoPecas) {
		pedidoPecas.setCodigoUnico(contadorId++);
		tabelaPedidos.add(pedidoPecas);
		return pedidoPecas;
	}

	public List<PedidoPecas> listarTodos() {
		return new ArrayList<>(tabelaPedidos);
	}

	public Optional<PedidoPecas> buscarPorId(Long id) {
		for (PedidoPecas pedido : tabelaPedidos) {
			if (pedido.getCodigoUnico().equals(id)) {
				return Optional.of(pedido);
			}
		}
		return Optional.empty();
	}

	public Optional<PedidoPecas> atualizar(Long id, PedidoPecas pedidoPecas) {
		for (int i = 0; i < tabelaPedidos.size(); i++) {
			if (tabelaPedidos.get(i).getCodigoUnico().equals(id)) {
				pedidoPecas.setCodigoUnico(id);
				tabelaPedidos.set(i, pedidoPecas);
				return Optional.of(pedidoPecas);
			}
		}
		return Optional.empty();
	}

	public boolean deletar(Long id) {
		return tabelaPedidos.removeIf(pedido -> pedido.getCodigoUnico().equals(id));
	}
}
