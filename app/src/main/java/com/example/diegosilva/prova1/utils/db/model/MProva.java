package com.example.diegosilva.prova1.utils.db.model;

import android.content.ContentValues;

import java.sql.*;
import java.text.SimpleDateFormat;

public class MProva {
    public static String TABLENAME = "prova";
    private Integer id;
    private MDisciplina disciplina;
    private String descricao;
    private Timestamp datahora;
    private Double nota;

    public MProva() {
        this.id = null;
        this.disciplina = null;
        this.descricao = "";
        this.datahora = null;
        this.nota = null;
    }

    public ContentValues getContentValues() {
        ContentValues vals = new ContentValues();
        if((this.getId() != null)&&(this.getId()==0))
            vals.putNull("id");
        else
            vals.put("id", this.getId());
        vals.put("descricao", this.getDescricao());
        if(this.getDatahora() != null)
            vals.put("datahora", android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", this.getDatahora()).toString());
        else
            vals.putNull("datahora");

        if(this.getDisciplina() != null)
            vals.put("disciplina", this.getDisciplina().getId());
        else
            vals.putNull("disciplina");
        if(this.getNota() == null)
            vals.putNull("nota");
        else
            vals.put("nota", this.getNota());
        return vals;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public MDisciplina getDisciplina() {
        if(this.disciplina == null)
            this.setDisciplina(new MDisciplina());
        return disciplina;
    }

    public void setDisciplina(MDisciplina disciplina) {
        this.disciplina = disciplina;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if(descricao.length() > 32)
            this.descricao = descricao.substring(0, 32);
        else
            this.descricao = descricao;
    }

    public Timestamp getDatahora() {
        return datahora;
    }

    public void setDatahora(Timestamp datahora) {
        this.datahora = datahora;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        if(nota == null)
            this.nota = null;
        else if(nota > 10.0)
            this.nota = 10.0;
        else if(nota < 0.0)
            this.nota = 0.0;
        else
            this.nota = nota;
    }
}
