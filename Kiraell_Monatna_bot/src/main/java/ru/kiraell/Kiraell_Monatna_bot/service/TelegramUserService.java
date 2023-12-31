package ru.kiraell.Kiraell_Monatna_bot.service;

import ru.kiraell.Kiraell_Monatna_bot.models.TelegramUser;

import java.util.List;

public interface TelegramUserService {
    void create(TelegramUser telegramUser);
    List<TelegramUser> readAll();
    TelegramUser read(Long id);
    boolean update(TelegramUser telegramUser,Long id);
    boolean delete(Long id);
    float updateUSD(TelegramUser telegramUser,String usdValue);
    float updateEUR(TelegramUser telegramUser,String eurValue);
    void checker(Long id,String userName);

}
