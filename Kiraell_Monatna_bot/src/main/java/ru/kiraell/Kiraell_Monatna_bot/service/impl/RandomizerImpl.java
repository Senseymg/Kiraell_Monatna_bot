package ru.kiraell.Kiraell_Monatna_bot.service.impl;

import org.springframework.stereotype.Service;
import ru.kiraell.Kiraell_Monatna_bot.service.Randomizer;

import java.util.List;
@Service
public class RandomizerImpl implements Randomizer {
    @Override
    public String getRandomFileUrl(List<String> urlsList) {
        return urlsList.get((int)(Math.random()*(urlsList.size())));
    }
}
//нижняя 0 верхняя urlsList.length()