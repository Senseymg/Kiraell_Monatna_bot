package ru.kiraell.Kiraell_Monatna_bot.configuration;


import okhttp3.OkHttpClient;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.kiraell.Kiraell_Monatna_bot.bot.ExchangeRatesBot;

@Configuration
@EnableCaching
@EnableScheduling
public class ExchangeRatesBotConfiguration {
    @Bean
    public TelegramBotsApi telegramBotApi(ExchangeRatesBot exchangeRatesBot) throws TelegramApiException {
            var api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(exchangeRatesBot);
            return  api;
        }
        @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient();
        }
}
