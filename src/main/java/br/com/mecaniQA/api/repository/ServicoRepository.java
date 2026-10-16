package br.com.mecaniQA.api.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.mecaniQA.api.model.Servico;

// Singleton em memória (mesma ideia do PecaRepository).
public class ServicoRepository {

	private static ServicoRepository instance;

	private final List<Servico> tabelaServicos;
	private Long contadorId;

	private ServicoRepository() {
		this.tabelaServicos = new ArrayList<>();
		this.contadorId = 1L;
	}

	public static synchronized ServicoRepository getInstance() {
		if (instance == null) {
			instance = new ServicoRepository();
		}
		return instance;
	}

	public Servico salvar(Servico servico) {
		servico.setCodigoUnico(contadorId++);
		tabelaServicos.add(servico);
		return servico;
	}

	public List<Servico> listarTodos() {
		return new ArrayList<>(tabelaServicos);
	}

	public Optional<Servico> buscarPorId(Long id) {
		for (Servico servico : tabelaServicos) {
			if (servico.getCodigoUnico().equals(id)) {
				return Optional.of(servico);
			}
		}
		return Optional.empty();
	}

	public Optional<Servico> atualizar(Long id, Servico servicoAtualizado) {
		for (int i = 0; i < tabelaServicos.size(); i++) {
			if (tabelaServicos.get(i).getCodigoUnico().equals(id)) {
				servicoAtualizado.setCodigoUnico(id);
				tabelaServicos.set(i, servicoAtualizado);
				return Optional.of(servicoAtualizado);
			}
		}
		return Optional.empty();
	}

	public boolean deletar(Long id) {
		return tabelaServicos.removeIf(servico -> servico.getCodigoUnico().equals(id));
	}
}
