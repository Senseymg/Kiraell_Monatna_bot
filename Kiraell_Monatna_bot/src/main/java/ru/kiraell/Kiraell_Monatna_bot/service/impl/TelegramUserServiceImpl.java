package ru.kiraell.Kiraell_Monatna_bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kiraell.Kiraell_Monatna_bot.models.TelegramUser;
import ru.kiraell.Kiraell_Monatna_bot.repository.TelegramUsersRepository;
import ru.kiraell.Kiraell_Monatna_bot.service.TelegramUserService;

import java.util.List;
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
}
