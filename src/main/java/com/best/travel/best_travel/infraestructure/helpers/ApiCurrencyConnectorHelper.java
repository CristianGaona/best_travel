package com.best.travel.best_travel.infraestructure.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.best.travel.best_travel.infraestructure.dtos.CurrencyDto;

@Component
public class ApiCurrencyConnectorHelper {
    private final WebClient currencyWebClient;

    @Value("${api.base.currency}")
    private String baseCurrency;

    public ApiCurrencyConnectorHelper(@Qualifier("currency") WebClient currencyWebClient) {
        this.currencyWebClient = currencyWebClient;
    }

    private static final String BASE_CURRENCY_QUERY_PARAM = "?base={base}";
    private static final String SYMBOL_CURRENCY_QUERY_PARAM = "&symbols={symbol}";
    private static final String CURRENCY_PATH = "exchangerates_data/"+LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //2025-08-07

    public CurrencyDto getExchangeRate(Currency currency) {
        {
            return this.currencyWebClient.get()
                    .uri(uri -> uri.path(CURRENCY_PATH)
                            .query(BASE_CURRENCY_QUERY_PARAM)
                            .query(SYMBOL_CURRENCY_QUERY_PARAM)
                            .build(baseCurrency, currency.getCurrencyCode()))
                    .retrieve()
                    .bodyToMono(CurrencyDto.class)
                    .block();
        }
    }
}
