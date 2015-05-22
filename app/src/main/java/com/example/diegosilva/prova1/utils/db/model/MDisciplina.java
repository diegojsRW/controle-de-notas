package com.example.diegosilva.prova1.utils.db.model;

import android.content.ContentValues;
import android.support.v4.app.NotificationCompatSideChannelService;

import java.sql.*;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class MDisciplina {
    public static String TABLENAME = "disciplina";
    private Integer id;
    private String nome;
    private String nomeAbreviado;
    private String professor;
    private Integer diaSemana;

    public MDisciplina() {
        this.id = null;
        this.nome = "";
        this.nomeAbreviado = "";
        this.professor = "";
        this.diaSemana = null;
    }

    public ContentValues getContentValues() {
        ContentValues vals = new ContentValues();
        if((this.getId() != null)&&(this.getId()==0))
            vals.putNull("id");
        else
            vals.put("id", this.getId());
        vals.put("nome", this.getNome());
        vals.put("nomeAbreviado", this.getNomeAbreviado());
        vals.put("professor", this.getProfessor());
        vals.put("diaSemana", this.getDiaSemana());
        return vals;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = new Integer(id);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if(nome.length() > 32)
            this.nome = nome.substring(0, 32);
        else
            this.nome = nome;
    }

    public String getNomeAbreviado() {
        return nomeAbreviado;
    }

    public void setNomeAbreviado(String nomeAbreviado) {
        if(nomeAbreviado.length() > 15)
            this.nomeAbreviado = nomeAbreviado.substring(0, 15);
        else
            this.nomeAbreviado = nomeAbreviado;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        if(professor.length() > 32)
            this.professor = professor.substring(0, 32);
        else
            this.professor = professor;
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        if(diaSemana > 6)
            this.diaSemana = new Integer(6);
        else if(diaSemana < 0)
            this.diaSemana = new Integer(0);
        else
            this.diaSemana = new Integer(diaSemana);
    }
    @Override
    public String toString(){
        return this.getNome();
    }
}
