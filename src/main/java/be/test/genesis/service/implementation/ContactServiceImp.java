package be.test.genesis.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.test.genesis.dao.ContactRepository;
import be.test.genesis.domain.Contact;
import be.test.genesis.service.ContactService;

@Service
public class ContactServiceImp implements ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Override
	@Transactional
	public Optional<Contact> saveOrUpdateContact(Contact contact) {
		if (Objects.nonNull(contact)) {
			return Optional.ofNullable(contactRepository.save(contact));
		}
		return Optional.empty();
	}

	@Override
	@Transactional
	public void deleteContact(Integer contactId) {
		if (Objects.nonNull(contactId)) {
			Contact contactToRemove=contactRepository.getById(contactId);
	///		contactToRemove.getEntreprises().stream().map(entreprise-> entreprise.getContacts().stream().map(con)).count();
			contactRepository.deleteById(contactId);
		}

	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Contact> getContactById(Integer contactId) {
		if (Objects.nonNull(contactId)) {
			return contactRepository.findById(contactId);
		}
		return Optional.empty();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Contact> getAllContact() {
		List<Contact> contactList = new ArrayList();
		contactList = contactRepository.findAll();

		return contactList;
	}

}
