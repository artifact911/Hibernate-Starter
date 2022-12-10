package org.art.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Serializable entityId;

    // что за класс сущности
    private String entityName;

    // что изменилось в сущности
    private String entityContent;

    // чтоб енам именами в таблицу писался
    @Enumerated(EnumType.STRING)
    private Operation operation;

    public enum Operation {
      SAVE, UPDATE, DELETE, INSERT
    }
}
