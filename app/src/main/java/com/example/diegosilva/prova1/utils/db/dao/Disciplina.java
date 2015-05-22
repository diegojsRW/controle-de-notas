package com.example.diegosilva.prova1.utils.db.dao;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import android.widget.Toast;

import com.example.diegosilva.prova1.utils.db.DAO;
import com.example.diegosilva.prova1.utils.db.model.*;
import com.example.diegosilva.prova1.utils.*;

import java.util.*;

public class Disciplina extends DAO {

    public Disciplina(Context ctx){ this.ctx = ctx; }

    public boolean inserir(MDisciplina disciplina){
        SQLiteDatabase db = getReadableDB();
        long rwid = db.insert(MDisciplina.TABLENAME, null, disciplina.getContentValues());
        db.close();
        return (rwid > 0);
    }
    public boolean atualizar(MDisciplina disciplina){
        SQLiteDatabase db = getReadableDB();
        SQLiteStatement stm = db.compileStatement("UPDATE disciplina SET nome = ?, nomeAbreviado = ?, professor = ?, diaSemana = ? WHERE id = ?");
        stm.bindString(1, disciplina.getNome()); //Mais seguro utilizar Statements.
        stm.bindString(2, disciplina.getNomeAbreviado());
        stm.bindString(3, disciplina.getProfessor());
        stm.bindLong(4, disciplina.getDiaSemana());
        stm.bindLong(5, disciplina.getId());
        int rows = stm.executeUpdateDelete();
        db.close();
        return (rows > 0);
    }
    public boolean remover(MDisciplina disciplina){
        SQLiteDatabase db = getReadableDB();
        SQLiteStatement stm = db.compileStatement("DELETE FROM disciplina WHERE id = ?");
        stm.bindLong(1, disciplina.getId());
        int rows = stm.executeUpdateDelete();
        db.close();
        return (rows > 0);
    }
    public List<MDisciplina> listarTodos(){
        return this.listar("");
    }
    public List<MDisciplina> listar(String WhereStatement){
        SQLiteDatabase db = null;
        try {
            db = getReadableDB();
        }
        catch(Exception e){
            Toast.makeText(ctx, "Erro ao obter banco de dados", Toast.LENGTH_LONG);
            return new ArrayList<MDisciplina>();
        }
        List<MDisciplina> resu = new ArrayList<MDisciplina>();
        MDisciplina disc;
        WhereStatement = WhereStatement.trim();
        if(WhereStatement.length() > 0) WhereStatement = "WHERE "+WhereStatement;

        Cursor cur = db.rawQuery("SELECT id, nome, nomeAbreviado, professor, diaSemana FROM disciplina" + WhereStatement, null);
        if(cur.moveToFirst())
            do {
                disc = new MDisciplina();
                disc.setId(cur.getInt(0));
                disc.setNome(cur.getString(1));
                disc.setNomeAbreviado(cur.getString(2));
                disc.setProfessor(cur.getString(3));
                disc.setDiaSemana(cur.getInt(4));

                resu.add(disc);
            } while(cur.moveToNext());
        return resu;
    }
}
