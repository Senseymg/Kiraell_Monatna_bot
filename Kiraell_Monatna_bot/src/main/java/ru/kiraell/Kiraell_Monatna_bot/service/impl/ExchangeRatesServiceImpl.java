package ru.kiraell.Kiraell_Monatna_bot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;


import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import ru.kiraell.Kiraell_Monatna_bot.client.CbrClient;
import ru.kiraell.Kiraell_Monatna_bot.exception.ServiceException;
import ru.kiraell.Kiraell_Monatna_bot.service.ExchangeRatesService;


//реализация интерфейса для работы с валютой
@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {
    // так как известен формат XML  файла который получаем с катировками мы можем
    // достать значение по ключам ккоторые указаны ниже
    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value"; // ключи по которым будем
    // искать значение для евро и доллара
    private static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";

    @Autowired
    private CbrClient client;  // инжектим ЦБРКлиент чтобы можно было получать таблицу с котировками

    @Override
    public String getUSDExchangeRate() throws ServiceException {
        var xmlOptional= client.getCurrencyRatesXML();
        String xml = xmlOptional.orElseThrow( ()->new ServiceException(" Не удалось получить XML"));
        return extractCurrencyFromXML(xml,USD_XPATH);
    }

    @Override
    public String getEURExchangeRate() throws ServiceException {
        var xmlOptional= client.getCurrencyRatesXML();
        String xml = xmlOptional.orElseThrow( ()->new ServiceException(" Не удалось получить XML"));
        return extractCurrencyFromXML(xml,EUR_XPATH); // с помщью метода экстракт возвращаем значение из ноды
        // EUR PATH("/ValCurs//Valute[@ID='R01239']/Value";) из XML который был вытянут с сайта ЦБРФ с помощью CBRClienta
    }

    private static String extractCurrencyFromXML(String xml, String xpathExpression) throws ServiceException { // пишем логику
        // для извлечения котрировок при передачи таблицы и ключа . xpathExpression это будет некий адрес в нашем XML

        var source = new InputSource(new StringReader(xml));  // inputSource это входной источник дял XML поставляется SAX
        // и читаем это всё по строчно
        try {
            var xpath = XPathFactory.newInstance().newXPath();// https://ru.wikipedia.org/wiki/XPath по сути создаём формочку
            // для запрома на языке xpath
            var document =(Document)  xpath.evaluate("/", source, XPathConstants.NODE); // получчаем конкретную ноду по запросу
            // который вставим "/ValCurs//Valute[@ID='R01239']/Value" из  XML
            //
            return xpath.evaluate(xpathExpression, document);
        } catch (XPathExpressionException e) {
            throw new ServiceException(" Не удалось распарсить XML", e);
        }
    }
}
