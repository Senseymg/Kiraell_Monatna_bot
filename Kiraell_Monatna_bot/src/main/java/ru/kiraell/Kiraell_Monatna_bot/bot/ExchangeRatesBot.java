package ru.kiraell.Kiraell_Monatna_bot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ru.kiraell.Kiraell_Monatna_bot.bot.commands.ExcRatesBotCommands;
import ru.kiraell.Kiraell_Monatna_bot.exception.ServiceException;
import ru.kiraell.Kiraell_Monatna_bot.models.TelegramUser;
import ru.kiraell.Kiraell_Monatna_bot.service.ExchangeRatesService;
import ru.kiraell.Kiraell_Monatna_bot.service.FolderUrlCollector;
import ru.kiraell.Kiraell_Monatna_bot.service.Randomizer;
import ru.kiraell.Kiraell_Monatna_bot.service.TelegramUserService;

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
    private static final String PICTURE = "/picture";
    private static final String MUSIC = "/music";
    @Autowired
    private ExchangeRatesService exchangeRatesService;
    @Autowired
    private FolderUrlCollector folderUrlCollector;
    @Autowired
    private Randomizer randomizer;
    @Autowired
    ExcRatesBotCommands excRatesBotCommands;
    @Autowired
    TelegramUserService telegramUserService;

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
                telegramUserService.create(new TelegramUser(chatId, nameOfUser, 0.0F, 0.0F));
            }
            case MUSIC -> sendMusic(chatId, nameOfUser);
            case PICTURE -> sendPicture(chatId, nameOfUser);
            case USD -> usdCommand(chatId,nameOfUser); //usdCommand(chatId);
            case EUR -> eurCommand(chatId,nameOfUser);//eurCommand(chatId);
            case HELP -> {

                helpCommand(chatId,nameOfUser);
                telegramUserService.checker(chatId,nameOfUser);//helpCommand(chatId);
                System.out.println(nameOfUser);} //удали это
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
                          
                /picture случайная картинка из библиотеки       
                                
                /music случайная композиция
                                
                Дополнительные команды:
                /help - получение справки
                            
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    public void usdCommand(Long chatId,String userName) {//usdCommand(Long chatId)
        telegramUserService.checker(chatId,userName); //временна приблуда чтобы добавть тех кто был до базы
        String formattedText;
        try {
            var usd = exchangeRatesService.getUSDExchangeRate();
            float tmpUsdDiff = telegramUserService.updateUSD(telegramUserService.read(chatId), usd);
            var text = "Курс доллара на %s составляет %s рублей, разница с прошлым запросом %.2f";
            formattedText = String.format(text, LocalDate.now(), usd, tmpUsdDiff);
            System.out.println("is chat id changed for a day or static?" + chatId + " время " + new Timestamp(System.currentTimeMillis()) + " form USD");
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса доллара", e);
            formattedText = " Не удалось получить текущий курс доллара Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
    }

    public void eurCommand(Long chatId,String userName) { //eurCommand(Long chatId)
        telegramUserService.checker(chatId,userName); //временна приблуда чтобы добавть тех кто был до базы
        String formattedText;
        try {
            var eur = exchangeRatesService.getEURExchangeRate();
            float tmpEurDiff = telegramUserService.updateEUR(telegramUserService.read(chatId), eur);
            var text = "Курс евро на %s составляет %s рублей, разница с прошлым запросом %.2f";
            formattedText = String.format(text, LocalDate.now(),eur, tmpEurDiff);
            System.out.println("is chat id changed for a day or static?" + chatId + " время " + new Timestamp(System.currentTimeMillis()) + " form EUR");
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса евро", e);
            formattedText = " Не удалось получить текущий курс евро Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
    }

    public void helpCommand(Long chatId,String userName) {//helpCommand(Long chatId)
        var text = """
                справочная информация по боту
                               
                 для получения текущих курсов валют воспользуйтесь кодами:
                 
                 /usd - курс доллара
                 
                 /eur - курс евро
                 
                 /picture случайная картинка из библиотеки     
                 
                 /music случайная композиция
                    
                 """;
        sendMessage(chatId, text);

    }

    private void unknownCommand(Long chatId) {
        var text = "Комманда не распознана!";
        System.out.println("is chat id changed for a day or static?" + chatId + " время " + new Timestamp(System.currentTimeMillis()) + " form UNOWN");
        sendMessage(chatId, text);
    }

    private void sendPicture(Long chatId, String name) {
        telegramUserService.checker(chatId,name); //временна приблуда чтобы добавть тех кто был до базы
        System.out.println(name);
        System.out.println(imgPath);
        System.out.println(chatId);
        String secondPartOfUrl = randomizer.getRandomFileUrl(folderUrlCollector.getFolderUrls(picturesPath));
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                //.caption(secondPartOfUrl) просто название картинки не является необходимым
                .parseMode(ParseMode.HTML)
                .photo(new InputFile(new File(picturesPath + "//" + secondPartOfUrl))).build();// из рандомайзера берём часть которую добавляем к пути
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка вывода картинки", e);
            throw new RuntimeException(e);
        }
    }

    private void sendMusic(Long chatId, String name) {
        telegramUserService.checker(chatId,name); //временна приблуда чтобы добавть тех кто был до базы
        String secondPartOfUrl = randomizer.getRandomFileUrl(folderUrlCollector.getFolderUrls(musicPath));
        SendAudio sendAudio = new SendAudio();
        sendAudio.setAudio(new InputFile(new File(musicPath + "//" + secondPartOfUrl)));
        sendAudio.setChatId(chatId);
        sendAudio.setCaption(name + " Держи песенку");
        try {
            execute(sendAudio);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки Аудио", e);
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
            throw new RuntimeException(e);
        }
    }
}
