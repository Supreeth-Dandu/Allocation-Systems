package com.allocation.trade.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allocation.trade.dto.TradeRequestDto;
import com.allocation.trade.dto.TradeResponseDto;
import com.allocation.trade.service.TradeService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/trades")
@Slf4j
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("/create")
    public ResponseEntity<TradeResponseDto> createTrade(@Valid @RequestBody TradeRequestDto dto) {
        TradeResponseDto created = tradeService.createTrade(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TradeResponseDto>> getAllTrades() {
        // log.info("All trades fetched successfully"); // This is a log message.
        // log.debug("All trades fetched successfully"); // This is a debug message.
        // log.error("All trades fetched successfully"); // This is an error message.
        // log.warn("All trades fetched successfully"); // This is a warning message.
        // log.trace("All trades fetched successfully"); // This is a trace message.
        return ResponseEntity.ok(tradeService.getAllTrades());

    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeResponseDto> getTradeById(@PathVariable Long id) {
        return ResponseEntity.ok(tradeService.getTradeById(id));
    }

    @GetMapping("/business/{tradeId}")
    public ResponseEntity<TradeResponseDto> getTradeByTradeId(@PathVariable String tradeId) {
        return ResponseEntity.ok(tradeService.getTradeByTradeId(tradeId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TradeResponseDto> updateTrade(
            @PathVariable Long id,
            @Valid @RequestBody TradeRequestDto dto) {
        return ResponseEntity.ok(tradeService.updateTrade(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTrade(@PathVariable Long id) {
        tradeService.deleteTrade(id);
        return ResponseEntity.noContent().build();
    }
}
