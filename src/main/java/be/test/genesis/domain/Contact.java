package be.test.genesis.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import be.test.genesis.validation.TvaConstraint;

@Entity
@TvaConstraint
public class Contact extends RepresentationModel<Contact> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Integer id;
	@Column
	@Size(min = 2, max = 60)
	@NotNull
	private String nom;
	@Column
	@Size(min = 2, max = 60)
	@NotNull
	private String preNom;
	@Column
	@Size(min = 2, max = 100)
	@NotNull
	private String adresse;

	@NotNull
	private CategorieContact categorieContact;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH,CascadeType.DETACH })
	@JoinTable(name = "entreprise_contact", joinColumns = { @JoinColumn(name = "contact_id") }, inverseJoinColumns = {
			@JoinColumn(name = "enterprise_id") })
	private Set<Entreprise> entreprises = new HashSet<>();

	@Column
	private String numeroTva;

	public Contact() {
		super();
	}

	public Contact(String nom, String preNom, String adresse, CategorieContact categorieContact, String numeroTva) {
		super();
		this.nom = nom;
		this.preNom = preNom;
		this.adresse = adresse;
		this.categorieContact = categorieContact;
		this.numeroTva = numeroTva;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPreNom() {
		return preNom;
	}

	public void setPreNom(String preNom) {
		this.preNom = preNom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public CategorieContact getCategorieContact() {
		return categorieContact;
	}

	public void setCategorieContact(CategorieContact categorieContact) {
		this.categorieContact = categorieContact;
	}

	public String getNumeroTva() {
		return numeroTva;
	}

	public void setNumeroTva(String numeroTva) {
		this.numeroTva = numeroTva;
	}

	public Set<Entreprise> getEntreprises() {
		return entreprises;
	}

	public void setEntreprises(Set<Entreprise> entreprises) {
		this.entreprises = entreprises;
	}

	public void addEntreprise(Entreprise entreprise) {
		this.entreprises.add(entreprise);
		entreprise.addContact(this);
	}

	public void removeEntreprise(Entreprise entreprise) {
		this.entreprises.remove(entreprise);
		entreprise.removeConatct(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Contact))
			return false;

		Contact contact = (Contact) o;

		return id != null && id.equals(contact.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", nom=" + nom + ", preNom=" + preNom + ", adresse=" + adresse
				+ ", categorieContact=" + categorieContact + ", entreprises=" + entreprises + ", numeroTva=" + numeroTva
				+ "]";
	}

}
