package com.allocation.trade.service;

import com.allocation.trade.dto.TradeRequestDto;
import com.allocation.trade.dto.TradeResponseDto;

import java.util.List;
// This interface is used to define the methods for the TradeService.
public interface TradeService {

    TradeResponseDto createTrade(TradeRequestDto dto); //   create a new Trade.

    TradeResponseDto getTradeById(Long id); //   get a Trade by its id.

    TradeResponseDto getTradeByTradeId(String tradeId); //   get a Trade by its tradeId.

    List<TradeResponseDto> getAllTrades(); //   get all Trades.    

    TradeResponseDto updateTrade(Long id, TradeRequestDto dto); //   update a Trade.   

    void deleteTrade(Long id); //   delete a Trade by its id.
}
