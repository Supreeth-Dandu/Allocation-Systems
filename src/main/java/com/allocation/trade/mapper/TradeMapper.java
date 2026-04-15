package com.allocation.trade.mapper;

import com.allocation.trade.dto.TradeRequestDto;
import com.allocation.trade.dto.TradeResponseDto;
import com.allocation.trade.entity.Trade;
import org.springframework.stereotype.Component;

@Component // This annotation is used to mark the mapper as a Spring bean.
public class TradeMapper {
    // This method is used to convert a TradeRequestDto to a Trade entity.
    public Trade toEntity(TradeRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return Trade.builder()
                .tradeId(dto.getTradeId())
                .symbol(dto.getSymbol())
                .side(dto.getSide())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .eventTimestamp(dto.getEventTimestamp())
                .sourceSystem(dto.getSourceSystem())
                .versionNumber(1)
                .build();
    }

    // This method is used to convert a Trade entity to a TradeResponseDto.
    public TradeResponseDto toResponseDto(Trade trade) {
        if (trade == null) {
            return null;
        }
        return TradeResponseDto.builder()
                .id(trade.getId())
                .tradeId(trade.getTradeId())
                .symbol(trade.getSymbol())
                .side(trade.getSide())
                .quantity(trade.getQuantity())
                .price(trade.getPrice())
                .status(trade.getStatus())
                .eventTimestamp(trade.getEventTimestamp())
                .sourceSystem(trade.getSourceSystem())
                .versionNumber(trade.getVersionNumber())
                .createdAt(trade.getCreatedAt())
                .updatedAt(trade.getUpdatedAt())
                .build();
    }

    // @author 
    // This method is used to update a Trade entity from a TradeRequestDto.
    public void updateEntityFromDto(TradeRequestDto dto, Trade trade) {
        if (dto == null || trade == null) {
            return;
        }
        trade.setTradeId(dto.getTradeId());
        trade.setSymbol(dto.getSymbol());
        trade.setSide(dto.getSide());
        trade.setQuantity(dto.getQuantity());
        trade.setPrice(dto.getPrice());
        trade.setStatus(dto.getStatus());
        trade.setEventTimestamp(dto.getEventTimestamp());
        trade.setSourceSystem(dto.getSourceSystem());
    }
}
