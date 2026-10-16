package br.com.mecaniQA.api.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.mecaniQA.api.model.Peca;

// Singleton: existe uma única instância, guardada no atributo static "instance".
// O construtor é private, então ninguém fora da classe consegue dar "new PecaRepository()".
// A lista fica dentro dessa instância única e por isso os dados continuam
// na memória enquanto o servidor estiver rodando.
public class PecaRepository {

	private static PecaRepository instance;

	private final List<Peca> tabelaPecas;
	private Long contadorId;

	private PecaRepository() {
		this.tabelaPecas = new ArrayList<>();
		this.contadorId = 1L;
	}

	public static synchronized PecaRepository getInstance() {
		if (instance == null) {
			instance = new PecaRepository();
		}
		return instance;
	}

	public Peca salvar(Peca peca) {
		peca.setCodigoUnico(contadorId++);
		tabelaPecas.add(peca);
		return peca;
	}

	public List<Peca> listarTodos() {
		return new ArrayList<>(tabelaPecas);
	}

	public Optional<Peca> buscarPorId(Long id) {
		for (Peca peca : tabelaPecas) {
			if (peca.getCodigoUnico().equals(id)) {
				return Optional.of(peca);
			}
		}
		return Optional.empty();
	}

	public Optional<Peca> atualizar(Long id, Peca pecaAtualizada) {
		for (int i = 0; i < tabelaPecas.size(); i++) {
			if (tabelaPecas.get(i).getCodigoUnico().equals(id)) {
				pecaAtualizada.setCodigoUnico(id);
				tabelaPecas.set(i, pecaAtualizada);
				return Optional.of(pecaAtualizada);
			}
		}
		return Optional.empty();
	}

	public boolean deletar(Long id) {
		return tabelaPecas.removeIf(peca -> peca.getCodigoUnico().equals(id));
	}
}
