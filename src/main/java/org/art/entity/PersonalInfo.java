package org.art.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    // не сработало
//    @NotNull // поставили для валидации перед пушем в БД
    private LocalDate birthDate;
}
