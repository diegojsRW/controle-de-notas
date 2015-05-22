package com.example.diegosilva.prova1.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.diegosilva.prova1.utils.SQLiteMyAdapter;

public class DAO {

    protected Context ctx;
    protected SQLiteMyAdapter adapter;

    protected boolean isOpen(){ return (adapter != null); }
    protected void openAdapter(){ if(!isOpen()) this.adapter = new SQLiteMyAdapter(ctx); }
    protected SQLiteDatabase getReadableDB(){
        openAdapter();
        return adapter.getReadableDatabase();
    }
}
