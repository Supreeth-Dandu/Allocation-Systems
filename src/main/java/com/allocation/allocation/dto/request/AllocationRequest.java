package com.allocation.allocation.dto.request;

import com.allocation.allocation.enums.AllocationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllocationRequest {

    @NotNull(message = "Trade id (database id) is required")
    private Long tradeId;

    @NotBlank(message = "Account id is required and must not be blank")
    private String accountId;

    @NotNull(message = "Allocated quantity is required")
    @Positive(message = "Allocated quantity must be greater than 0")
    private BigDecimal allocatedQuantity;

    @NotNull(message = "Allocated price is required")
    @Positive(message = "Allocated price must be greater than 0")
    private BigDecimal allocatedPrice;

    @NotNull(message = "Status is required")
    private AllocationStatus status;
}
