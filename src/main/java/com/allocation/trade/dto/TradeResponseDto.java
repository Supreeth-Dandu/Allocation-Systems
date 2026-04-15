package com.allocation.trade.dto;

import com.allocation.trade.enums.Side;
import com.allocation.trade.enums.TradeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeResponseDto {

    private Long id;
    private String tradeId;
    private String symbol;
    private Side side;
    private BigDecimal quantity;
    private BigDecimal price;
    private TradeStatus status;
    private LocalDateTime eventTimestamp;
    private String sourceSystem;
    private Integer versionNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
