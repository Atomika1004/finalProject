package ru.atomika.bank_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    BigDecimal balance;
}
