package ru.kiraell.Kiraell_Monatna_bot.service;

import ru.kiraell.Kiraell_Monatna_bot.exception.ServiceException;

public interface ExchangeRatesService {
    String getUSDExchangeRate() throws ServiceException;

    String getEURExchangeRate() throws ServiceException;
}
