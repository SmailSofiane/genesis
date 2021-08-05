package be.test.genesis.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.hateoas.RepresentationModel;
 
@Entity
public class Entreprise extends RepresentationModel<Entreprise> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Integer id;

	@Size(min = 2, max = 100)
	@NotNull
	@Column
	private String adresse;

	@Column
	@NotNull
	private String numeroTva;
	   
	     
	    
	@ManyToMany( fetch = FetchType.EAGER,mappedBy = "entreprises")
	private Set<Contact> contacts = new HashSet();

	public Entreprise() {
		super();
	}

	public Entreprise(@Size(min = 2, max = 100) @NotNull String adresse, @NotNull String numeroTva) {
		super();
		this.adresse = adresse;
		this.numeroTva = numeroTva;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getNumeroTva() {
		return numeroTva;
	}

	public void setNumeroTva(String numeroTva) {
		this.numeroTva = numeroTva;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public void addContact(Contact contact) {
		this.contacts.add(contact);
		contact.getEntreprises().add(this);
	}

	public void removeConatct(Contact contact) {
		this.contacts.remove(contact);
		contact.getEntreprises().remove(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Entreprise))
			return false;
		Entreprise entreprise = (Entreprise) o;
		return Objects.equals(getNumeroTva(), entreprise.getNumeroTva());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getNumeroTva());
	}

	@Override
	public String toString() {
		return "Entreprise [id=" + id + ", adresse=" + adresse + ", numeroTva=" + numeroTva + ", contacts=" + contacts
				+ "]";
	}
	

}
