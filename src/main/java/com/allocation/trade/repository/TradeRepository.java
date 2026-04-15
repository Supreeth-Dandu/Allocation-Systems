package com.allocation.trade.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allocation.trade.entity.Trade;

@Repository // This annotation is used to mark the repository as a Spring bean.
public interface TradeRepository extends JpaRepository<Trade, Long> {

    Optional<Trade> findByTradeId(String tradeId); // This method is used to find a Trade by its tradeId.

    boolean existsByTradeId(String tradeId);  // This method is used to check if a Trade with the given tradeId exists.

    // @Query(value = "SELECT * from allocation.trades WHERE trade_id = :tradeId", nativeQuery = true) 
    // Optional<Trade> findByTradeId(String tradeId);

}
