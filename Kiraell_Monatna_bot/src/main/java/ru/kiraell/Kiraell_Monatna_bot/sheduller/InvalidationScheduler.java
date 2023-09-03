package ru.kiraell.Kiraell_Monatna_bot.sheduller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kiraell.Kiraell_Monatna_bot.bot.ExchangeRatesBot;
import ru.kiraell.Kiraell_Monatna_bot.service.ExchangeRatesService;

@Component
public class InvalidationScheduler {
    @Autowired
    private ExchangeRatesService service;
    @Scheduled(cron = "* 0 0 * * ?")
    public void invalidateCache(){
        service.clearEURCache();
        service.clearUSDCache();
    }
}
