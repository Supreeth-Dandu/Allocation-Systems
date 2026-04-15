package com.allocation.allocation.dto.response;

import com.allocation.allocation.enums.AllocationStatus;
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
public class AllocationResponse {

    private Long id;
    private Long tradeId;
    private String accountId;
    private BigDecimal allocatedQuantity;
    private BigDecimal allocatedPrice;
    private AllocationStatus status;
    private Integer versionNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
