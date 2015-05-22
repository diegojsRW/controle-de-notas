package com.example.diegosilva.prova1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.diegosilva.prova1.utils.db.dao.Disciplina;
import com.example.diegosilva.prova1.utils.db.model.MDisciplina;

public class NewDisciplineActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_discipline_activity);

        //Spinner spnDiaSemanal = (Spinner)findViewById(R.id.spnDiaSemanal);
        //String[] arrSpin = new String[]{"Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado"};
        //ArrayAdapter spnAdapt = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrSpin);
        //spnDiaSemanal.setAdapter(spnAdapt);
    }

    public void limpaCampos(){
        EditText txtDisciplinaNome = (EditText)findViewById(R.id.txtDisciplinaNome);
        EditText txtDisciplinaNomeAbreviado = (EditText)findViewById(R.id.txtDisciplinaNomeAbreviado);
        EditText txtDisciplinaProfessor = (EditText)findViewById(R.id.txtDisciplinaProfessor);
        Spinner spnDiaSemanal = (Spinner)findViewById(R.id.spnDiaSemanal);

        txtDisciplinaNome.setText("");
        txtDisciplinaNomeAbreviado.setText("");
        txtDisciplinaProfessor.setText("");
        spnDiaSemanal.setSelection(0);
    }
    public void cancelaNovaDisciplina(View v){
        finish();
    }

    public void cadastraDisciplina(View v){
        EditText txtDisciplinaNome = (EditText)findViewById(R.id.txtDisciplinaNome);
        EditText txtDisciplinaNomeAbreviado = (EditText)findViewById(R.id.txtDisciplinaNomeAbreviado);
        EditText txtDisciplinaProfessor = (EditText)findViewById(R.id.txtDisciplinaProfessor);
        Spinner spnDiaSemanal = (Spinner)findViewById(R.id.spnDiaSemanal);

        if(txtDisciplinaNome.getText().length() == 0){
            Toast.makeText(this, "Nome da disciplina necessário", Toast.LENGTH_SHORT).show();
            txtDisciplinaNome.requestFocus();
            return;
        }

        if(txtDisciplinaNomeAbreviado.getText().length() == 0){
            Toast.makeText(this, "Nome abreviado da disciplina necessário", Toast.LENGTH_SHORT).show();
            txtDisciplinaNomeAbreviado.requestFocus();
            return;
        }


        if(txtDisciplinaProfessor.getText().length() == 0){
            Toast.makeText(this, "Nome do professor necessário", Toast.LENGTH_SHORT).show();
            txtDisciplinaProfessor.requestFocus();
            return;
        }

        MDisciplina disc = new MDisciplina();
        Disciplina disdao = new Disciplina(this);
        disc.setNome(txtDisciplinaNome.getText().toString());
        disc.setNomeAbreviado(txtDisciplinaNomeAbreviado.getText().toString());
        disc.setProfessor(txtDisciplinaProfessor.getText().toString());
        disc.setDiaSemana(spnDiaSemanal.getSelectedItemPosition());
        boolean insercao = disdao.inserir(disc);

        if(!insercao)
            Toast.makeText(this, "Erro ao cadastrar disciplina. Tente novamente mais tarde.", Toast.LENGTH_LONG);
        else
            new AlertDialog.Builder(this).setTitle("Cadastro efetuado").setMessage("A disciplina \"" + disc.getNome() + "\" foi cadastrada com sucesso. \r\n"+
                    "Deseja cadastrar mais disciplinas?").setPositiveButton("Cadastrar mais", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            NewDisciplineActivity.this.limpaCampos();
                        }
                    }

                    ).setNegativeButton("Voltar ao Início", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setIcon(android.R.drawable.ic_menu_save).show();

    }
}
