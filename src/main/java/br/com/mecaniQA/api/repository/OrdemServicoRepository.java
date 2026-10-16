package br.com.mecaniQA.api.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.mecaniQA.api.model.OrdemServico;

// Singleton em memória, igual aos repositories da OAT 1.
public class OrdemServicoRepository {

	private static OrdemServicoRepository instance;

	private final List<OrdemServico> tabelaOrdens;
	private Long contadorId;

	private OrdemServicoRepository() {
		this.tabelaOrdens = new ArrayList<>();
		this.contadorId = 1L;
	}

	public static synchronized OrdemServicoRepository getInstance() {
		if (instance == null) {
			instance = new OrdemServicoRepository();
		}
		return instance;
	}

	public OrdemServico salvar(OrdemServico ordemServico) {
		ordemServico.setCodigoUnico(contadorId++);
		tabelaOrdens.add(ordemServico);
		return ordemServico;
	}

	public List<OrdemServico> listarTodos() {
		return new ArrayList<>(tabelaOrdens);
	}

	public Optional<OrdemServico> buscarPorId(Long id) {
		for (OrdemServico os : tabelaOrdens) {
			if (os.getCodigoUnico().equals(id)) {
				return Optional.of(os);
			}
		}
		return Optional.empty();
	}

	public Optional<OrdemServico> atualizar(Long id, OrdemServico ordemServico) {
		for (int i = 0; i < tabelaOrdens.size(); i++) {
			if (tabelaOrdens.get(i).getCodigoUnico().equals(id)) {
				ordemServico.setCodigoUnico(id);
				tabelaOrdens.set(i, ordemServico);
				return Optional.of(ordemServico);
			}
		}
		return Optional.empty();
	}

	public boolean deletar(Long id) {
		return tabelaOrdens.removeIf(os -> os.getCodigoUnico().equals(id));
	}
}
