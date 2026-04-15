package com.allocation.allocation.controller;

import com.allocation.allocation.dto.request.AllocationRequest;
import com.allocation.allocation.dto.response.AllocationResponse;
import com.allocation.allocation.service.AllocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/allocations")
public class AllocationController {

    /** Required on POST /api/allocations; clients must send a non-blank value (e.g. shared secret or caller id). */
    public static final String ALLOCATION_AUTH_HEADER = "X-Allocation-Token";

    private final AllocationService allocationService;

    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping
    public ResponseEntity<AllocationResponse> create(
            @RequestHeader(value = ALLOCATION_AUTH_HEADER, required = false) String allocationToken,
            @Valid @RequestBody AllocationRequest allocationRequest) {
        if (allocationToken == null || allocationToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(allocationService.createAllocation(allocationRequest));
    }

    @GetMapping
    public ResponseEntity<List<AllocationResponse>> getAll() {
        return ResponseEntity.ok(allocationService.getAllAllocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AllocationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(allocationService.getAllocationById(id));
    }

    @GetMapping("/trade/{tradeId}")
    public ResponseEntity<List<AllocationResponse>> getByTradeId(@PathVariable Long tradeId) {
        return ResponseEntity.ok(allocationService.getAllocationsByTradeId(tradeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AllocationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AllocationRequest allocationRequest) {
        return ResponseEntity.ok(allocationService.updateAllocation(id, allocationRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        allocationService.deleteAllocation(id);
        return ResponseEntity.noContent().build();
    }
}
