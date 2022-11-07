package org.art.entity;

import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "company")
@Builder
@Entity
@Table(name = "users", schema = "public") // схема тут не обязательно, тк эта по умолчанию
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    // название этого поля не имеет никакого значения
    @Embedded
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    /**
     * Аннотация ниже позволяет создать свой собственный кастомный тип. В данном случае String будет приводиться к формату JSON, однако
     * для корректной работы необходима зависимость для парсинга JSON (например библиотека Jackson)
     */
    @JdbcTypeCode(SqlTypes.JSON)
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // в данном случае не обязательно, т.к. по умолчанию возьмется название класса и его id. получится company_id
    @JoinColumn(name = "company_id")
    private Company company;
}
