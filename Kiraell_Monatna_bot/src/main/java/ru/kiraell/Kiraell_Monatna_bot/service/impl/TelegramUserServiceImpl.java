package ru.kiraell.Kiraell_Monatna_bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kiraell.Kiraell_Monatna_bot.models.TelegramUser;
import ru.kiraell.Kiraell_Monatna_bot.repository.TelegramUsersRepository;
import ru.kiraell.Kiraell_Monatna_bot.service.TelegramUserService;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {
    @Autowired
    TelegramUsersRepository telegramUsersRepository;

    @Override
    public void create(TelegramUser telegramUser) {
        telegramUsersRepository.save(telegramUser);
    }

    @Override
    public List<TelegramUser> readAll() {
        return telegramUsersRepository.findAll();
    }

    @Override
    public TelegramUser read(Long id) {
        return telegramUsersRepository.getOne(id);
    }

    @Override
    public boolean update(TelegramUser telegramUser, Long id) {
        if (telegramUsersRepository.existsById(id)) {
            telegramUser.setId(id);
            telegramUsersRepository.save(telegramUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        if (telegramUsersRepository.existsById(id)) {
            telegramUsersRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public float updateUSD(TelegramUser telegramUser, String usdValue) {
        float floatUsdNewValue = Float.parseFloat(usdValue.replace(',', '.'));
        float floatUsdOldValue = telegramUser.getDollarLastReq();
        if (floatUsdOldValue != floatUsdNewValue) {
            telegramUser.setDollarLastReq(floatUsdNewValue);
            telegramUsersRepository.save(telegramUser);
            return floatUsdNewValue-floatUsdOldValue  ;
        }
            return 0.0F;
    }

    @Override
    public float updateEUR(TelegramUser telegramUser, String eurValue) {
        float floatEurNewValue = Float.parseFloat(eurValue.replace(',', '.'));
        float floatEurOldValue = telegramUser.getEurLastReq();
        if (floatEurOldValue != floatEurNewValue) {
            telegramUser.setEurLastReq(floatEurNewValue);
            telegramUsersRepository.save(telegramUser);
            return floatEurNewValue-floatEurOldValue  ;
        }
        return 0.0F;
    }
    //потом удалить
    @Override
    public void checker(Long id,String userName){
        var tmp=telegramUsersRepository.findById(id);
        if(tmp.isEmpty()) {
            telegramUsersRepository.save(new TelegramUser(id,userName,0,0));
        }

    }
}
