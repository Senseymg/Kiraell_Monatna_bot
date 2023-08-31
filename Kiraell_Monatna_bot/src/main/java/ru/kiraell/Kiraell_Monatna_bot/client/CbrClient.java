package ru.kiraell.Kiraell_Monatna_bot.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kiraell.Kiraell_Monatna_bot.exception.ServiceException;
import java.io.IOException;
import java.util.Optional;
//идём по url который записан в application properties
@Component
public class CbrClient {
    @Autowired
    private OkHttpClient client;

    @Value("${cbr.currency.rates.xml.url}")
    String url;

    public Optional<String> getCurrencyRatesXML() throws ServiceException{
        var request = new Request.Builder()  // формириуем реквест  на URL с помощью HTTP3
                .url(url)
                .build();

       try(var response = client.newCall(request).execute()) { //отправялем наш реквест
            var body = response.body();

            return body==null? Optional.empty(): Optional.of(body.string()) ;
       }catch (IOException e){
           throw  new ServiceException(" Ошибка получения курсов валют",e);
       }

    }
}
