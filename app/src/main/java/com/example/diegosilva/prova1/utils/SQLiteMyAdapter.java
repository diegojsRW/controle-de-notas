package com.example.diegosilva.prova1.utils;

import android.content.*;
import android.database.sqlite.*;
import android.util.Log;

public class SQLiteMyAdapter extends SQLiteOpenHelper {
    private static final int DBVERSION = 1;
    private static final String DBNAME = "mytests.db";

    public SQLiteMyAdapter(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE disciplina (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, nomeAbreviado TEXT, professor TEXT, diaSemana INTEGER);");
        Log.d("SQLiteMyAdapter", "Criou tabela disciplina");
        db.execSQL("CREATE TABLE prova (id INTEGER PRIMARY KEY AUTOINCREMENT, descricao TEXT, datahora TIMESTAMP, nota DOUBLE, disciplina INTEGER);");
        Log.d("SQLiteMyAdapter", "Criou tabela prova");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
