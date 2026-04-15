package com.allocation.allocation.mapper;

import com.allocation.allocation.dto.request.AllocationRequest;
import com.allocation.allocation.dto.response.AllocationResponse;
import com.allocation.allocation.entity.Allocation;
import com.allocation.trade.entity.Trade;
import org.springframework.stereotype.Component;

@Component
public class AllocationMapper {

    public Allocation toEntity(AllocationRequest request, Trade trade) {
        if (request == null || trade == null) {
            return null;
        }
        return Allocation.builder()
                .trade(trade)
                .accountId(request.getAccountId())
                .allocatedQuantity(request.getAllocatedQuantity())
                .allocatedPrice(request.getAllocatedPrice())
                .status(request.getStatus())
                .versionNumber(1)
                .build();
    }

    public AllocationResponse toResponse(Allocation allocation) {
        if (allocation == null) {
            return null;
        }
        Long tradePk = allocation.getTrade() != null ? allocation.getTrade().getId() : null;
        return AllocationResponse.builder()
                .id(allocation.getId())
                .tradeId(tradePk)
                .accountId(allocation.getAccountId())
                .allocatedQuantity(allocation.getAllocatedQuantity())
                .allocatedPrice(allocation.getAllocatedPrice())
                .status(allocation.getStatus())
                .versionNumber(allocation.getVersionNumber())
                .createdAt(allocation.getCreatedAt())
                .updatedAt(allocation.getUpdatedAt())
                .build();
    }

    public void updateEntityFromRequest(AllocationRequest request, Allocation allocation) {
        if (request == null || allocation == null) {
            return;
        }
        allocation.setAccountId(request.getAccountId());
        allocation.setAllocatedQuantity(request.getAllocatedQuantity());
        allocation.setAllocatedPrice(request.getAllocatedPrice());
        allocation.setStatus(request.getStatus());
    }
}
