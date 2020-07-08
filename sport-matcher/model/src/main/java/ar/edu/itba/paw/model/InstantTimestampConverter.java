package ar.edu.itba.paw.model;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class InstantTimestampConverter implements AttributeConverter<Instant, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(Instant attribute) {
		return (attribute != null) ? Timestamp.from(attribute) : null;
	}

	@Override
	public Instant convertToEntityAttribute(Timestamp dbData) {
		return (dbData != null) ? dbData.toInstant() : null;
	}

}
