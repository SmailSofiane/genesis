package be.test.genesis.service;

import java.util.List;
import java.util.Optional;
 
import be.test.genesis.domain.Entreprise;

public interface EntrepriseService {
	
	public Optional<Entreprise> getEntrepriseById(Integer entrepriseId);
	
	public List<Entreprise> getAllEntreprise();
	
	public Entreprise saveOrUpdateEntreprise(Entreprise entreprise);
	

}
