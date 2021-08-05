package be.test.genesis.service;

import java.util.List;
import java.util.Optional;

import be.test.genesis.domain.Contact;

public interface ContactService {

	public Optional<Contact> getContactById(Integer contactId);

	public List<Contact> getAllContact();

	public Optional<Contact> saveOrUpdateContact(Contact contact);

	public void deleteContact(Integer contactId);

}
