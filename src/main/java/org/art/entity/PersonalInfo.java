package org.art.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo {

    private String firstname;
    private String lastname;

    // перепишем в  configuration.addAttributeConverter(new BirthdayConverter());
//    @Convert(converter = BirthdayConverter.class)
    private Birthday birthDate;
}
