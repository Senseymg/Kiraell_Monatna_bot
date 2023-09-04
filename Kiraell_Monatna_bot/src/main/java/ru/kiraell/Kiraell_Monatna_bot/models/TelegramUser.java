package ru.kiraell.Kiraell_Monatna_bot.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "telegramUsers")

public class TelegramUser {
    @Id
    @Column(name = "id")
    long id;
    @Column(name = "userName")
    String userName;
    @Column(name = "dollarLastReq")
    float dollarLastReq;
    @Column(name = "eurLastReq")
    float eurLastReq;

    public TelegramUser(long id, String userName, float dollarLastReq, float eurLastReq) {
        this.id = id;
        this.userName = userName;
        this.dollarLastReq = dollarLastReq;
        this.eurLastReq = eurLastReq;
    }

    public TelegramUser() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getDollarLastReq() {
        return dollarLastReq;
    }

    public void setDollarLastReq(float dollarLastReq) {
        this.dollarLastReq = dollarLastReq;
    }

    public float getEurLastReq() {
        return eurLastReq;
    }

    public void setEurLastReq(float eurLastReq) {
        this.eurLastReq = eurLastReq;
    }
}
