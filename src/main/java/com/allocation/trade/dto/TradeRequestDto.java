package com.allocation.trade.dto;

import com.allocation.trade.enums.Side;
import com.allocation.trade.enums.TradeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class TradeRequestDto {

    @NotBlank(message = "Trade ID is required and must not be blank")
    private String tradeId;

    @NotBlank(message = "Symbol is required and must not be blank")
    private String symbol;

    @NotNull(message = "Side is required")
    private Side side;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Status is required")
    private TradeStatus status;

    @NotNull(message = "Event timestamp is required")
    private LocalDateTime eventTimestamp;

    private String sourceSystem;
}
