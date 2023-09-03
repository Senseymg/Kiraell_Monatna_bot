package ru.kiraell.Kiraell_Monatna_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kiraell.Kiraell_Monatna_bot.models.TelegramUser;

import java.util.Optional;

@Repository
public interface TelegramUsersRepository extends JpaRepository<TelegramUser,Long> {

}
