package ru.atomika.bank_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.atomika.bank_service.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
