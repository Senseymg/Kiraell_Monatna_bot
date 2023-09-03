package ru.kiraell.Kiraell_Monatna_bot.models;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Table;


public class TelegramUser {

    long id;

    String name;

    int[] picturesSeen;

    int[] musicTrackRecived;

    float dollarLastReq;

    float eurLastReq;
}
