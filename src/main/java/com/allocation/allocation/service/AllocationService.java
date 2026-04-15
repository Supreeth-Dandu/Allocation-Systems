package com.allocation.allocation.service;

import com.allocation.allocation.dto.request.AllocationRequest;
import com.allocation.allocation.dto.response.AllocationResponse;

import java.util.List;

public interface AllocationService {

    AllocationResponse createAllocation(AllocationRequest request);

    AllocationResponse getAllocationById(Long id);

    List<AllocationResponse> getAllocationsByTradeId(Long tradeId);

    List<AllocationResponse> getAllAllocations();

    AllocationResponse updateAllocation(Long id, AllocationRequest request);

    void deleteAllocation(Long id);
}
