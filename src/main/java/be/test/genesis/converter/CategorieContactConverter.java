package be.test.genesis.converter;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import be.test.genesis.domain.CategorieContact;

@Converter(autoApply = true)
public class CategorieContactConverter implements AttributeConverter<CategorieContact, String> {

	@Override
	public String convertToDatabaseColumn(CategorieContact catContact) {
		if (Objects.nonNull(catContact)) {
			return catContact.getId();
		}
		return null;
	}

	@Override
	public CategorieContact convertToEntityAttribute(String dbData) {

		if (Objects.nonNull(dbData)) {
			return CategorieContact.fromId(dbData);

		}
		return null;
	}

}
