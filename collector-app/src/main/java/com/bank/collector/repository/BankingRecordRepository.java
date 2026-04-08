package com.bank.collector.repository;

import com.bank.collector.model.BankingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankingRecordRepository extends JpaRepository<BankingRecord, Long> {
}
