package be.test.genesis.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import be.test.genesis.domain.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

}
