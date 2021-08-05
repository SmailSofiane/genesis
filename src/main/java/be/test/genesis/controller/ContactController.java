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
import org.springframework.web.bind.annotation.DeleteMapping;
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
import be.test.genesis.service.ContactService;
import be.test.genesis.service.EntrepriseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ContactController", description = "RestController for managing contacts")
@RestController
@RequestMapping("api/genesis/contacts")
public class ContactController {

	@Autowired
	private ContactService contactService;
	@Autowired
	private EntrepriseService entrepriseService;

	/*
	 * get Contact
	 * 
	 * @param :contact Id
	 * 
	 * @return :ResponseEntity<Contact> HttpStatus 200 when success or Error detail{
	 * internal error 500 | bad Request 400|404 ResourceNotFoundException}
	 */
	@GetMapping("/{contactId}")
	public ResponseEntity<Contact> getContactById(@PathVariable Integer contactId) {

		final Optional<Contact> contactOptional = contactService.getContactById(contactId);
		if (contactOptional.isPresent()) {
			final Contact contact = contactOptional.get();
			updateContactResourceWithLinks(contact);
			return new ResponseEntity<Contact>(contact, HttpStatus.OK);
		}
		throw new ResourceNotFoundException("Contact with id:" + contactId + " not found");
	}

	/*
	 * get all Contacts
	 * 
	 * @param :void
	 * 
	 * @return :ResponseEntity<List<Contact>> HttpStatus 200 when success or Error
	 * detail{ internal error 500 | bad Request 400| default response no_content}
	 */
	@GetMapping()
	public ResponseEntity<List<Contact>> getAllContact() {

		final List<Contact> fetchedListContact = contactService.getAllContact();
		if (!fetchedListContact.isEmpty()) {
			for (Contact contact : fetchedListContact) {
				updateContactResourceWithLinks(contact);
			}
			return new ResponseEntity<List<Contact>>(fetchedListContact, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
	/*
	 * add new Contact
	 * 
	 * @param :Contact Object
	 * 
	 * @return :ResponseEntity<new add Contact> HttpStatus 201 when success or Error
	 * detail{ internal error 500 | bad Request 400| default response no_content}
	 */

	@PostMapping
	public ResponseEntity<Contact> addContact(@Valid @RequestBody Contact contact) {

		final Optional<Contact> savedContactOptional = contactService.saveOrUpdateContact(contact);
		if (savedContactOptional.isPresent()) {
			final Contact savedContact = savedContactOptional.get();
			updateContactResourceWithLinks(savedContact);
			return new ResponseEntity<Contact>(savedContact, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	/*
	 *   update Contact
	 * 
	 * @param :Contact Object
	 * 
	 * @return :ResponseEntity<new add Contact> HttpStatus 200 when success or Error
	 * detail{ internal error 500 | bad Request 400 | default response no_content}
	 */
	@PutMapping
	public ResponseEntity<Contact> updateContact(@Valid @RequestBody Contact contact) {

		final Optional<Contact> updatedContactOptional = contactService.saveOrUpdateContact(contact);
		if (updatedContactOptional.isPresent()) {
			final Contact updatedContact = updatedContactOptional.get();
			updateContactResourceWithLinks(updatedContact);
			return new ResponseEntity<Contact>(updatedContact, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	/*
	 * add existing Contact to an existing enterprise
	 * 
	 * @param :contactId |enterpriseId
	 * 
	 * @return :ResponseEntity<new add Contact> HttpStatus 200 when success or Error
	 * detail{ internal error 500 | bad Request 400 | response no_content|404
	 * resourceNotFound}
	 */
	@PatchMapping("/{contactid}/entreprises/{entrepriseId}")
	public ResponseEntity<Contact> addContactToEntreprise(@Min(1) @PathVariable Integer entrepriseId,
			@Min(1) @PathVariable Integer contactid) {

		final Optional<Entreprise> entrepriseToUpdatOptional = entrepriseService.getEntrepriseById(entrepriseId);
		if (entrepriseToUpdatOptional.isPresent()) {

			final Optional<Contact> contactToUpdateOptional = contactService.getContactById(contactid);
			if (contactToUpdateOptional.isPresent()) {
				final Contact contactToUpdate = contactToUpdateOptional.get();
				contactToUpdate.addEntreprise(entrepriseToUpdatOptional.get());
				final Optional<Contact> updatedContactOptional = contactService.saveOrUpdateContact(contactToUpdate);
				if (!updatedContactOptional.isEmpty()) {
					return new ResponseEntity<>(updatedContactOptional.get(), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
				}
			} else
				throw new ResourceNotFoundException("Contact with id:" + contactid + " not found");

		} else {
			throw new ResourceNotFoundException("Entreprise with id:" + entrepriseId + " not found");
		}
	}
	/*
	 * add delete Contact
	 * 
	 * @param :Contact Id
	 * 
	 * @return :void when success or Error detail{ internal error 500 | bad Request
	 * 400 | default response no_content}
	 */

	@DeleteMapping("/{contactId}")
	public ResponseEntity<Void> deleteContact(@PathVariable Integer contactId) {

		contactService.deleteContact(contactId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

	/*
	 * private method to add hypermedia link to Contact Object responses
	 */
	private void updateContactResourceWithLinks(Contact contact) {
		Link linkToPost = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ContactController.class).getAllContact())
				.withRel("POST");
		Link linkToContact = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(ContactController.class).getAllContact()).slash(contact.getId())
				.withRel("contact");

		for (Entreprise en : contact.getEntreprises()) {
			Link linkToEntreprise = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EntrepriseController.class).getAllEntreprise()).slash(en.getId())
					.withRel("entreprise");
			contact.add(linkToEntreprise);
		}
		contact.add(linkToPost);
		contact.add(linkToContact);
	}
}
