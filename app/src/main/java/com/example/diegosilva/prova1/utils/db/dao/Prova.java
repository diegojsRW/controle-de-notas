package com.example.diegosilva.prova1.utils.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import com.example.diegosilva.prova1.utils.db.DAO;
import com.example.diegosilva.prova1.utils.db.model.MDisciplina;
import com.example.diegosilva.prova1.utils.db.model.MProva;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Prova extends DAO {

    public Prova(Context ctx){ this.ctx = ctx; }

    public boolean inserir(MProva prova){
        SQLiteDatabase db = getReadableDB();
        long rwid = db.insert(MProva.TABLENAME, null, prova.getContentValues());
        db.close();
        return (rwid > 0);
    }
    public boolean atualizar(MProva prova){
        SQLiteDatabase db = getReadableDB();
        SQLiteStatement stm = db.compileStatement("UPDATE prova SET descricao = ?, datahora  = ?, disciplina = ?, nota = ? WHERE id = ?");
        stm.bindString(1, prova.getDescricao()); //Mais seguro utilizar Statements.
        stm.bindString(2, android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", prova.getDatahora()).toString());
        stm.bindLong(3, prova.getDisciplina().getId());
        stm.bindDouble(4, prova.getNota());
        stm.bindLong(5, prova.getId());
        int rows = stm.executeUpdateDelete();
        db.close();
        return (rows > 0);
    }
    public boolean remover(MProva prova){
        SQLiteDatabase db = getReadableDB();
        SQLiteStatement stm = db.compileStatement("DELETE FROM prova WHERE id = ?");
        stm.bindLong(1, prova.getId());
        int rows = stm.executeUpdateDelete();
        db.close();
        return (rows > 0);
    }
    public List<MProva> listarTodos() {
        return this.listar("");
    }


    public List<MProva> listar(String WhereStatement) {
        SQLiteDatabase db = null;
        try {
            db = getReadableDB();
        } catch(Exception e){
            Toast.makeText(ctx, "Erro ao obter banco de dados", Toast.LENGTH_LONG).show();
            return new ArrayList<MProva>();
        }
        List<MProva> resu = new ArrayList<MProva>();
        MDisciplina disc;
        MProva prova;
        WhereStatement = WhereStatement.trim();
        if(WhereStatement.length() > 0) WhereStatement = "WHERE "+WhereStatement;

        Cursor cur = db.rawQuery("SELECT p.id, p.descricao, p.datahora, p.nota, p.disciplina, d.nome, d.nomeAbreviado, d.professor, d.diaSemana FROM prova p inner join disciplina d on d.id = p.disciplina " + WhereStatement, new String[]{});
        if(cur.moveToFirst())
            do {
                disc = new MDisciplina();
                prova = new MProva();
                prova.setId(cur.getInt(0));
                prova.setDescricao(cur.getString(1));
                try {
                    prova.setDatahora(new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cur.getString(2)).getTime()));
                } catch (ParseException e) {
                    prova.setDatahora(new Timestamp(0));
                }
                prova.setNota(cur.getDouble(3));
                prova.setDisciplina(disc);
                disc.setId(cur.getInt(4));
                disc.setNome(cur.getString(5));
                disc.setNomeAbreviado(cur.getString(6));
                disc.setProfessor(cur.getString(7));
                disc.setDiaSemana(cur.getInt(8));

                resu.add(prova);
            } while(cur.moveToNext());
        return resu;
    }

}
