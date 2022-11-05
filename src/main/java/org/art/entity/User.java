package org.art.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public") // схема тут не обязательно, тк эта по умолчанию
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(generator = "user_gen", strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name = "user_gen", sequenceName = "users_id_seq", allocationSize = 1)
//    @TableGenerator(name = "user_gen", table = "all_sequence",
//            pkColumnName = "table_name", valueColumnName = "pk_value",
//            allocationSize = 1)
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
}
