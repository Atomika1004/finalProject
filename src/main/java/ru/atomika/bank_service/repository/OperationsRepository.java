package ru.atomika.bank_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.atomika.bank_service.entity.Operations;

import java.time.LocalDate;
import java.util.List;

public interface OperationsRepository extends JpaRepository<Operations, Integer> {

    @Query("""
            SELECT op from Operations op
            LEFT JOIN FETCH op.account as account
            where op.date >= :from and op.date <= :to
            """)
    List<Operations> findByDateBetween(LocalDate from, LocalDate to);

    @Query("""
            SELECT op from Operations op
            LEFT JOIN FETCH op.account as account
            """)
    List<Operations> findAll();

    @Query("""
            SELECT op FROM Operations op
            LEFT JOIN FETCH op.account as account
            WHERE op.date >= :from
    """)
    List<Operations> findAllFromDate(LocalDate from);

    @Query("""
            SELECT op FROM Operations op
            LEFT JOIN FETCH op.account as account
            WHERE op.date <= :to
    """)
    List<Operations> findAllToDate(LocalDate to);
}
