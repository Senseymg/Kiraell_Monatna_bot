/*bot.token=6573975242:AAFahNqwORuF1THAUr2Y9YSgJYoxQ0lIXqU
        cbr.currency.rates.xml.url=http://www.cbr.ru/scripts/XML_daily.asp
        img.path=F://Java//JavaIdeaWs//Kiraell_Monatna_bot//Kiraell_Monatna_bot//src//main//resources//photo.jpg*/


package ru.kiraell.Kiraell_Monatna_bot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.kiraell.Kiraell_Monatna_bot.exception.ServiceException;
import ru.kiraell.Kiraell_Monatna_bot.service.ExchangeRatesService;
import ru.kiraell.Kiraell_Monatna_bot.service.FolderUrlCollector;
import ru.kiraell.Kiraell_Monatna_bot.service.Randomizer;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;

@Component
public class ExchangeRatesBot extends TelegramLongPollingBot {
    @Value("${img.path}")
    String imgPath;
    @Value("${music_Folder.path}")
    String musicPath;
    @Value("${img_folder.path}")
    String picturesPath;

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesBot.class);
    private static final String START = "/start";
    private static final String USD = "/usd";
    private static final String EUR = "/eur";
    private static final String HELP = "/help";
    private static final String PICTURE = "/photo";
    @Autowired
    private ExchangeRatesService exchangeRatesService;
    @Autowired
    private FolderUrlCollector folderUrlCollector;
    @Autowired
    private Randomizer randomizer;

    public ExchangeRatesBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        var nameOfUser = update.getMessage().getChat().getUserName();
        switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
            case PICTURE -> sendPicture(chatId, nameOfUser);
            case USD -> usdCommand(chatId);
            case EUR -> eurCommand(chatId);
            case HELP -> helpCommand(chatId);
            default -> unknownCommand(chatId);
        }
    }

    @Override
    public String getBotUsername() {
        return "Kiraell_Monatna_bot";
    }

    private void startCommand(Long chatId, String userName) {
        var text = """
                Добро пожаловать в бот, %s!
                                
                Здесь Вы сможете узнать официальные курсы валют на сегодня установленные ЦБ РФ.
                                
                для этого воспользуйтесь кодами:
                /usd - курс доллара
                                
                /eur - курс евро
                          
                /picture картинка=)                
                                
                Дополнительные команды:
                /help - получение справки
                            
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    public void usdCommand(Long chatId) {
        String formattedText;
        try {
            var usd = exchangeRatesService.getUSDExchangeRate();
            var text = "Курс доллара на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);
            System.out.println("is chat id changed for a day or static?" + chatId + " время " + new Timestamp(System.currentTimeMillis()) + " form USD");
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса доллара", e);
            formattedText = " Не удалось получить текущий курс доллара Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
    }

    public void eurCommand(Long chatId) {
        String formattedText;

        try {


            var eur = exchangeRatesService.getEURExchangeRate();
            var text = "Курс доллара на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), eur);
            System.out.println("is chat id changed for a day or static?" + chatId + " время " + new Timestamp(System.currentTimeMillis()) + " form EUR");
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса евро", e);
            formattedText = " Не удалось получить текущий курс евро Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
    }

    public void helpCommand(Long chatId) {
        var text = """
                справочная информация по боту
                               
                 для получения текущих курсов валют воспользуйтесь кодами:
                 
                 /usd - курс доллара
                 
                 /eur - курс евро
                 
                 /photo картинка=)    
                 """;
        sendMessage(chatId, text);
    }

    private void unknownCommand(Long chatId) {
        var text = "Комманда не распознана!";
        System.out.println("is chat id changed for a day or static?" + chatId + " время " + new Timestamp(System.currentTimeMillis()) + " form UNOWN");
        sendMessage(chatId, text);
    }

    private void sendPicture(Long chatId, String name) {

        randomizer.getRandomFileUrl(folderUrlCollector.getFolderUrls(picturesPath));
        System.out.println(name);
        System.out.println(imgPath);
        String caption = "Dzirrt";
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode(ParseMode.HTML)
                .photo(new InputFile(new File(imgPath))).build();
        try {

            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщенияь", e);
        }
    }
}
