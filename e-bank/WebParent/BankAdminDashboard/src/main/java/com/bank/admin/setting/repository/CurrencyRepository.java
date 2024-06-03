package com.bank.admin.setting.repository;

import com.bank.common.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
    public List<Currency> findAllByOrderByNameAsc();
}
