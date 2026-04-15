package com.allocation.allocation.service.impl;

import com.allocation.allocation.dto.request.AllocationRequest;
import com.allocation.allocation.dto.response.AllocationResponse;
import com.allocation.allocation.entity.Allocation;
import com.allocation.allocation.exception.AllocationNotFoundException;
import com.allocation.allocation.exception.DuplicateAllocationException;
import com.allocation.allocation.exception.InvalidAllocationException;
import com.allocation.allocation.mapper.AllocationMapper;
import com.allocation.allocation.repository.AllocationRepository;
import com.allocation.allocation.service.AllocationService;
import com.allocation.trade.entity.Trade;
import com.allocation.trade.exception.TradeNotFoundException;
import com.allocation.trade.repository.TradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllocationServiceImpl implements AllocationService {

    private final AllocationRepository allocationRepository;
    private final TradeRepository tradeRepository;
    private final AllocationMapper allocationMapper;

    public AllocationServiceImpl(
            AllocationRepository allocationRepository,
            TradeRepository tradeRepository,
            AllocationMapper allocationMapper) {
        this.allocationRepository = allocationRepository;
        this.tradeRepository = tradeRepository;
        this.allocationMapper = allocationMapper;
    }

    @Override
    @Transactional
    public AllocationResponse createAllocation(AllocationRequest request) {
        validateBusinessRules(request);
        Trade trade = tradeRepository.findById(request.getTradeId())
                .orElseThrow(() -> new TradeNotFoundException("Trade not found with id: " + request.getTradeId()));

        String accountId = request.getAccountId().trim();
        if (allocationRepository.existsByTrade_IdAndAccountId(trade.getId(), accountId)) {
            throw new DuplicateAllocationException(
                    "An allocation already exists for trade id " + trade.getId() + " and account '" + accountId + "'");
        }

        Allocation entity = allocationMapper.toEntity(request, trade);
        normalizeStrings(entity, request, accountId);
        Allocation saved = allocationRepository.save(entity);
        return allocationMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AllocationResponse getAllocationById(Long id) {
        Allocation allocation = allocationRepository.findById(id)
                .orElseThrow(() -> new AllocationNotFoundException("Allocation not found with id: " + id));
        return allocationMapper.toResponse(allocation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllocationResponse> getAllocationsByTradeId(Long tradeId) {
        if (!tradeRepository.existsById(tradeId)) {
            throw new TradeNotFoundException("Trade not found with id: " + tradeId);
        }
        return allocationRepository.findByTrade_Id(tradeId).stream()
                .map(allocationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllocationResponse> getAllAllocations() {
        return allocationRepository.findAll().stream()
                .map(allocationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AllocationResponse updateAllocation(Long id, AllocationRequest request) {
        validateBusinessRules(request);
        Allocation allocation = allocationRepository.findById(id)
                .orElseThrow(() -> new AllocationNotFoundException("Allocation not found with id: " + id));

        Long existingTradePk = allocation.getTrade().getId();
        if (!request.getTradeId().equals(existingTradePk)) {
            throw new InvalidAllocationException(
                    "tradeId in request must match the allocation's trade; use delete and create to move between trades");
        }

        String accountId = request.getAccountId().trim();
        if (!accountId.equals(allocation.getAccountId())
                && allocationRepository.existsByTrade_IdAndAccountId(existingTradePk, accountId)) {
            throw new DuplicateAllocationException(
                    "An allocation already exists for this trade and account '" + accountId + "'");
        }

        allocationMapper.updateEntityFromRequest(request, allocation);
        normalizeStrings(allocation, request, accountId);
        if (allocation.getVersionNumber() == null) {
            allocation.setVersionNumber(1);
        } else {
            allocation.setVersionNumber(allocation.getVersionNumber() + 1);
        }

        return allocationMapper.toResponse(allocationRepository.save(allocation));
    }

    @Override
    @Transactional
    public void deleteAllocation(Long id) {
        if (!allocationRepository.existsById(id)) {
            throw new AllocationNotFoundException("Allocation not found with id: " + id);
        }
        allocationRepository.deleteById(id);
    }

    private void validateBusinessRules(AllocationRequest request) {
        if (request.getAllocatedQuantity() == null
                || request.getAllocatedQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAllocationException("Allocated quantity must be greater than 0");
        }
        if (request.getAllocatedPrice() == null
                || request.getAllocatedPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAllocationException("Allocated price must be greater than 0");
        }
    }

    private void normalizeStrings(Allocation entity, AllocationRequest request, String trimmedAccountId) {
        entity.setAccountId(trimmedAccountId);
        entity.setStatus(request.getStatus());
    }
}
