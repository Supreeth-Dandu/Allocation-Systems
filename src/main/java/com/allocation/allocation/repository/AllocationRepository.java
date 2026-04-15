package com.allocation.allocation.repository;

import com.allocation.allocation.entity.Allocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {

    List<Allocation> findByTrade_Id(Long tradeId);

    Optional<Allocation> findByTrade_IdAndAccountId(Long tradeId, String accountId);

    boolean existsByTrade_IdAndAccountId(Long tradeId, String accountId);
}
