package be.test.genesis.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.test.genesis.dao.EntrepriseRepository;
import be.test.genesis.domain.Entreprise;
import be.test.genesis.service.EntrepriseService;

@Service
public class EntrepriseServiceImp implements EntrepriseService {

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	@Override
	@Transactional
	public Entreprise saveOrUpdateEntreprise(Entreprise entreprise) {

		if (Objects.nonNull(entreprise)) {
			return entrepriseRepository.save(entreprise);
		}

		return null;
	}

	@Override
	public Optional<Entreprise> getEntrepriseById(Integer entrepriseId) {
		if (Objects.nonNull(entrepriseId)) {
			return entrepriseRepository.findById(entrepriseId);
		}
		return Optional.empty();
	}

	@Override
	public List<Entreprise> getAllEntreprise() {
		List<Entreprise> entrepriseList = new ArrayList<>();
		entrepriseList = entrepriseRepository.findAll();
		return entrepriseList;
	}

}
