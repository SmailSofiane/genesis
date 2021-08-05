package be.test.genesis.domain;

import java.util.HashMap;
import java.util.Map;


public enum CategorieContact {
	EMPLOYE("1"), FREELANCE("2");

	private final String id;

	private static final Map<String, CategorieContact> loockup = new HashMap<>();

	static {
		for (CategorieContact catContact : CategorieContact.values())
			loockup.put(catContact.getId(), catContact);
	}

	// Constructor
	private CategorieContact(String id) {
		this.id = id;
	}

	// getters
	public  String getId() {
		return id;
	}

	public static CategorieContact fromId(String dbData) {
		return loockup.get(dbData);
	}

}
