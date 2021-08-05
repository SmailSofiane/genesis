package be.test.genesis.validation;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import be.test.genesis.domain.CategorieContact;
import be.test.genesis.domain.Contact;

public class TvaValidator implements ConstraintValidator<TvaConstraint, Contact> {

	@Override
	public boolean isValid(Contact contact, ConstraintValidatorContext context) {

		final CategorieContact contactCategory = contact.getCategorieContact();
		final String numeroTva = contact.getNumeroTva();
		if (CategorieContact.FREELANCE.equals(contactCategory)) {
			if (Objects.nonNull(numeroTva) && (!numeroTva.isEmpty())) {
				return true;
			} else
				return false;
		} else if (Objects.isNull(numeroTva) || numeroTva.isEmpty()) {
			return true;
		}

		return false;
	}

}
