package ru.kiraell.Kiraell_Monatna_bot.dao;


import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class TelegramuserDao {

    private static final String URL= "jdbc:postgresql://localhost:5432/TelegramBotDB";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "8756441067";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
//execute("CREATE TABLE IF NOT EXISTS myTelegramBotUsers
// (name varchar(100), id integer primary key auto_increment, nickname varchar(100));");
/*
CREATE TABLE test."TestTGusers"
        (
        id bigint NOT NULL,
        name character varying(100),
        "picturesSeen" integer[],
        "musicTrackRecived" integer[],
        "dollarLastReq" money,
        "eurLastReq" money,
        PRIMARY KEY (id)
        );

        ALTER TABLE IF EXISTS test."TestTGusers"
        OWNER to postgres;*/
