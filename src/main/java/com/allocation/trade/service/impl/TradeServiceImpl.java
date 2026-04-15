package com.allocation.trade.service.impl;

import com.allocation.trade.dto.TradeRequestDto;
import com.allocation.trade.dto.TradeResponseDto;
import com.allocation.trade.entity.Trade;
import com.allocation.trade.exception.DuplicateTradeException;
import com.allocation.trade.exception.InvalidTradeDataException;
import com.allocation.trade.exception.TradeNotFoundException;
import com.allocation.trade.mapper.TradeMapper;
import com.allocation.trade.repository.TradeRepository;
import com.allocation.trade.service.TradeService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j // This annotation is used to log the messages.
public class TradeServiceImpl implements TradeService {

    // @autowired is used to inject the repository into the service.
    private final TradeRepository tradeRepository; // This is the repository for the Trade entity.
    private final TradeMapper tradeMapper;

    public TradeServiceImpl(TradeRepository tradeRepository, TradeMapper tradeMapper) {
        this.tradeRepository = tradeRepository;
        this.tradeMapper = tradeMapper;
    }

    @Override
    @Transactional // Runs create steps in one DB transaction; rolls back if anything fails.
    public TradeResponseDto createTrade(TradeRequestDto dto) {
        validateBusinessRules(dto); // Reject invalid quantity, price, side, or status before touching the DB.
        if (tradeRepository.existsByTradeId(dto.getTradeId().trim())) { // trade_id is unique; trim matches stored values.
            throw new DuplicateTradeException("A trade with tradeId '" + dto.getTradeId().trim() + "' already exists"); // Maps to HTTP 409 via GlobalExceptionHandler.
        }
        Trade entity = tradeMapper.toEntity(dto); // Map request fields into a new Trade (id/timestamps not set yet).
        normalizeStrings(entity, dto); // Trim text and uppercase side/status so persistence is consistent.
        Trade saved = tradeRepository.save(entity); // INSERT row; Hibernate triggers @PrePersist for timestamps/version.
        log.info("Trade created successfully: {}", saved); // This is a log message.
        log.debug("Trade created successfully: {}", saved); // This is a debug message.
        log.error("Trade created successfully: {}", saved); // This is an error message.
        log.warn("Trade created successfully: {}", saved); // This is a warning message.
        log.trace("Trade created successfully: {}", saved); // This is a trace message.
        return tradeMapper.toResponseDto(saved); // Expose persisted state (id, createdAt, etc.) to the API layer.
    }

    @Override
    @Transactional(readOnly = true) // Read-only: no flush/commit of writes; can optimize the transaction.
    public TradeResponseDto getTradeById(Long id) {
        Trade trade = tradeRepository.findById(id) // Primary-key lookup on trades.id.
                .orElseThrow(() -> new TradeNotFoundException("Trade not found with id: " + id)); // Becomes HTTP 404.
        return tradeMapper.toResponseDto(trade); // Shape entity as JSON-friendly output.
    }

    @Override
    @Transactional(readOnly = true)
    public TradeResponseDto getTradeByTradeId(String tradeId) {
        Trade trade = tradeRepository.findByTradeId(tradeId.trim()) // Natural/business key lookup.
                .orElseThrow(() -> new TradeNotFoundException("Trade not found with tradeId: " + tradeId));
        return tradeMapper.toResponseDto(trade);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TradeResponseDto> getAllTrades() {
        return tradeRepository.findAll().stream() // Load every row (fine for small catalogs; paginate if it grows).
                .map(tradeMapper::toResponseDto) // One DTO per entity.
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // Update is a single write transaction; @PreUpdate refreshes updated_at.
    public TradeResponseDto updateTrade(Long id, TradeRequestDto dto) {
        validateBusinessRules(dto); // Same rules as create for a consistent domain model.
        Trade trade = tradeRepository.findById(id) // Must exist before partial overwrite.
                .orElseThrow(() -> new TradeNotFoundException("Trade not found with id: " + id));

        String newTradeId = dto.getTradeId().trim(); // Compare using normalized business id.
        if (!newTradeId.equals(trade.getTradeId()) && tradeRepository.existsByTradeId(newTradeId)) {
            throw new DuplicateTradeException("A trade with tradeId '" + newTradeId + "' already exists"); // Another row already owns this tradeId.
        }

        tradeMapper.updateEntityFromDto(dto, trade); // Copy mutable fields from the request onto the managed entity.
        normalizeStrings(trade, dto); // Align trims/casing with what we persist on create.
        if (trade.getVersionNumber() == null) {
            trade.setVersionNumber(1); // Defensive default if legacy data lacked a version.
        } else {
            trade.setVersionNumber(trade.getVersionNumber() + 1); // Bump optimistic-style version on each successful update.
        }

        Trade saved = tradeRepository.save(trade); // UPDATE row; dirty checking issues SQL as needed.
        return tradeMapper.toResponseDto(saved); // Return the post-update snapshot to the client.
    }

    @Override
    @Transactional
    public void deleteTrade(Long id) {
        if (!tradeRepository.existsById(id)) { // Avoid silent no-op deletes.
            throw new TradeNotFoundException("Trade not found with id: " + id);
        }
        tradeRepository.deleteById(id); // DELETE by primary key within the same transaction.
    }

    /** Service-layer checks beyond bean validation (defense in depth, clear domain errors). */
    private void validateBusinessRules(TradeRequestDto dto) {
        if (dto.getQuantity() == null || dto.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTradeDataException("Quantity must be greater than 0"); // HTTP 400.
        }
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTradeDataException("Price must be greater than 0");
        }
    }

    /** Keeps stored strings consistent (trim whitespace; canonical case for coded fields). */
    private void normalizeStrings(Trade entity, TradeRequestDto dto) {
        entity.setTradeId(dto.getTradeId().trim());
        entity.setSymbol(dto.getSymbol().trim());
        entity.setSide(dto.getSide());
        entity.setStatus(dto.getStatus());
        if (dto.getSourceSystem() != null) {
            entity.setSourceSystem(dto.getSourceSystem().trim());
        }
    }
}
