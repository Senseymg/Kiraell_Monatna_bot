package ru.kiraell.Kiraell_Monatna_bot.service;

import ru.kiraell.Kiraell_Monatna_bot.exception.ServiceException;
    // интерфейс который декларирует виды операций с валютой
public interface ExchangeRatesService {
    String getUSDExchangeRate() throws ServiceException;

    String getEURExchangeRate() throws ServiceException;
}
