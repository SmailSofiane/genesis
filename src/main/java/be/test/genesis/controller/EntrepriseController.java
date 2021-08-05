package be.test.genesis.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import be.test.genesis.domain.Contact;
import be.test.genesis.domain.Entreprise;
import be.test.genesis.exception.ResourceNotFoundException;
import be.test.genesis.service.EntrepriseService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "EntrepriseController", description = "RestController for managing entrerprise")
@RestController
@RequestMapping("api/genesis/entreprises")
public class EntrepriseController {

	@Autowired
	private EntrepriseService entrepriseService;

	/*
	 * get Enterprise
	 * 
	 * @param :Entreprise Id
	 * 
	 * @return :ResponseEntity<Entreprise> HttpStatus 200 when success or Error
	 * detail{ internal error 500 | bad Request 400| 404 ResourceNotFoundException}
	 */
	@GetMapping("/{entrepriseId}")
	public ResponseEntity<Entreprise> getEntrepriseById(@Min(1) @PathVariable Integer entrepriseId) {

		Optional<Entreprise> fetchedEntreprise = entrepriseService.getEntrepriseById(entrepriseId);
		if (!fetchedEntreprise.isEmpty()) {
			updateEntrepriseResourceWithLinks(fetchedEntreprise.get());
			return new ResponseEntity<Entreprise>(fetchedEntreprise.get(), HttpStatus.OK);
		} else {
			throw new ResourceNotFoundException("Entreprise with id:" + entrepriseId + " not found");
		}

	}

	/*
	 * get all Enterprise
	 * 
	 * @param :void
	 * 
	 * @return :ResponseEntity<List<Entreprise>> HttpStatus 200 when success or
	 * Error detail{ internal error 500 | bad Request 400| default response
	 * no_content}
	 */
	@GetMapping
	public ResponseEntity<List<Entreprise>> getAllEntreprise() {

		List<Entreprise> entrepriseList = entrepriseService.getAllEntreprise();
		if (!entrepriseList.isEmpty()) {
			for (Entreprise entreprise : entrepriseList) {
				updateEntrepriseResourceWithLinks(entreprise);
			}

			return new ResponseEntity<List<Entreprise>>(entrepriseList, HttpStatus.OK);
		} else
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

	}

	/*
	 * add new Entreprise
	 * 
	 * @param :Entreprise Object
	 * 
	 * @return :ResponseEntity<new add Entreprise> HttpStatus 201 when success or
	 * Error detail{ internal error 500 | bad Request 400|
	 */
	@PostMapping
	public ResponseEntity<Entreprise> addEntreprise(@Valid @RequestBody Entreprise entreprise) {

		final Entreprise savedEntreprise = entrepriseService.saveOrUpdateEntreprise(entreprise);
		updateEntrepriseResourceWithLinks(savedEntreprise);
		return new ResponseEntity<>(savedEntreprise, HttpStatus.CREATED);

	}
	/*
	 * update Entreprise
	 * 
	 * @param :Entreprise Object
	 * 
	 * @return :ResponseEntity<nupdated Entreprise> HttpStatus 200 when success or
	 * Error detail{ internal error 500 | bad Request 400|
	 */

	@PutMapping
	public ResponseEntity<Entreprise> updateEntreprise(@Valid @RequestBody Entreprise entreprise) {

		final Entreprise updatedEntreprise = entrepriseService.saveOrUpdateEntreprise(entreprise);
		updateEntrepriseResourceWithLinks(updatedEntreprise);
		return new ResponseEntity<>(updatedEntreprise, HttpStatus.OK);

	}

	/*
	 * update Entreprise:add new contact
	 * 
	 * @param :Contact Object to add
	 * 
	 * @Pathvariable Enterprise id to update
	 * 
	 * @return :ResponseEntity<nupdated Enterprise> HttpStatus 200 when success or
	 * Error detail{ internal error 500 | bad Request 400| 404
	 * ResourceNotFoundException}
	 */
	@PatchMapping("/{entrepriseId}/contacts")
	public ResponseEntity<Entreprise> addContactToEntreprise(@Min(1) @PathVariable Integer entrepriseId,
			@Valid @RequestBody Contact contact) {

		final Optional<Entreprise> updatedEntrepriseOptional = entrepriseService.getEntrepriseById(entrepriseId);
		if (updatedEntrepriseOptional.isPresent()) {
			final Entreprise entreprise = updatedEntrepriseOptional.get();
			entreprise.addContact(contact);
			final Entreprise updatedEntreprise = entrepriseService.saveOrUpdateEntreprise(entreprise);
			updateEntrepriseResourceWithLinks(updatedEntreprise);
			return new ResponseEntity<>(updatedEntreprise, HttpStatus.OK);

		}
		throw new ResourceNotFoundException("Entreprise with id:" + entrepriseId + " not found");
	}

	/*
	 * private method to add hypermedia link to Enterprise Object responses
	 */

	private void updateEntrepriseResourceWithLinks(Entreprise entreprise) {
		Link linkToEntreprise = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EntrepriseController.class).getAllEntreprise())
				.slash(entreprise.getId()).withRel("entreprise");
		Link linkToPost = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EntrepriseController.class).getAllEntreprise()).withRel("POST");

		for (Contact contact : entreprise.getContacts()) {
			Link linkToContact = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(ContactController.class).getAllContact()).slash(contact.getId())
					.withRel("contact");
			entreprise.add(linkToContact);
		}

		entreprise.add(linkToEntreprise);
		entreprise.add(linkToPost);
	}
}
