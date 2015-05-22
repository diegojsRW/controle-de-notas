package com.example.diegosilva.prova1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diegosilva.prova1.utils.db.ViewDisciplinesListAdapter;
import com.example.diegosilva.prova1.utils.db.dao.Disciplina;
import com.example.diegosilva.prova1.utils.db.dao.Prova;
import com.example.diegosilva.prova1.utils.db.model.MDisciplina;
import com.example.diegosilva.prova1.utils.db.model.MProva;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * Created by Diego Silva on 2015-05-20.
 */
public class ViewDisciplinesActivity extends Activity {
    ListView lvDisciplinas;
    Dialog d;
    public void compartilhar(MDisciplina disciplina){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, "#MyTestsGrade:  " + new DateFormatSymbols(new Locale("pt", "BR")).getWeekdays()[disciplina.getDiaSemana()+1] + " tenho aula de " + disciplina.getNome() + " com o Prof. " + disciplina.getProfessor() + ". #Valeu, prof.!");
        startActivity(Intent.createChooser(i, "Escolher aplicativo"));
    }
    public void confirmadeleta(MDisciplina disciplina){
        Prova provadao = new Prova(this);
        List<MProva> provas = provadao.listar("disciplina = " + disciplina.getId().toString());
        for(MProva prova : provas){
            if(!provadao.remover(prova)) {
                Toast.makeText(this, "Erro ao excluir prova " + prova.getDescricao() +". A disciplina não foi excluida.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        Disciplina discdao = new Disciplina(this);
        if(discdao.remover(disciplina))
            Toast.makeText(this, "Disciplina removida com sucesso. ", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Erro ao excluir disciplina", Toast.LENGTH_LONG).show();
        onResume();
    }
    public void deleta(final MDisciplina disciplina){
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_menu_delete)
                .setTitle("Confirmar exclusão")
                .setMessage("A disciplina " + disciplina.getNome() + " será excluída. \r\nTODAS AS PROVAS DESTA DISCIPLINA SERÃO REMOVIDAS. \r\nDeseja continuar?")
                .setPositiveButton("Sim, entendi, remova", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmadeleta(disciplina);
                    }
                })
                .setNegativeButton("Não, espere!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

    }
    public void editar(final MDisciplina disciplina){
        //Toast.makeText(this, "Aqui edita", Toast.LENGTH_LONG).show();
        d = new Dialog(this);
        LayoutInflater li = d.getLayoutInflater();
        View vw = li.inflate(R.layout.new_discipline_activity, null);

        d.setContentView(vw);
        EditText txtDisciplinaNome = (EditText)d.findViewById(R.id.txtDisciplinaNome);
        EditText txtDisciplinaNomeAbreviado = (EditText)d.findViewById(R.id.txtDisciplinaNomeAbreviado);
        EditText txtDisciplinaProfessor = (EditText)d.findViewById(R.id.txtDisciplinaProfessor);
        Spinner spnDiaSemanal = (Spinner)d.findViewById(R.id.spnDiaSemanal);

        txtDisciplinaNome.setText(disciplina.getNome());
        txtDisciplinaNomeAbreviado.setText(disciplina.getNomeAbreviado());
        txtDisciplinaProfessor.setText(disciplina.getProfessor());
        spnDiaSemanal.setSelection(disciplina.getDiaSemana());

        Button btnNovaDisciplinaCadastra = (Button)d.findViewById(R.id.btnNovaDisciplinaCadastra);

        btnNovaDisciplinaCadastra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editaconfirma(disciplina);
            }
        });
        btnNovaDisciplinaCadastra.setText("Concluir");
        Button btnNovaDisciplinaCancela = (Button)d.findViewById(R.id.btnNovaDisciplinaCancela);
        btnNovaDisciplinaCancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });
        d.setTitle("Editar disciplina...");
        d.show();

    }

    private void editaconfirma(MDisciplina disciplina) {
        EditText txtDisciplinaNome = (EditText)d.findViewById(R.id.txtDisciplinaNome);
        EditText txtDisciplinaNomeAbreviado = (EditText)d.findViewById(R.id.txtDisciplinaNomeAbreviado);
        EditText txtDisciplinaProfessor = (EditText)d.findViewById(R.id.txtDisciplinaProfessor);
        Spinner spnDiaSemanal = (Spinner)d.findViewById(R.id.spnDiaSemanal);

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

        disciplina.setNome(txtDisciplinaNome.getText().toString().trim());
        disciplina.setProfessor(txtDisciplinaProfessor.getText().toString().trim());
        disciplina.setNomeAbreviado(txtDisciplinaNomeAbreviado.getText().toString().trim());
        disciplina.setDiaSemana(spnDiaSemanal.getSelectedItemPosition());

        Disciplina discdao = new Disciplina(this);
        if(discdao.atualizar(disciplina)) {
            Toast.makeText(this, "Disciplina atualizada", Toast.LENGTH_LONG).show();
            d.dismiss();
            onResume();
        } else {
            Toast.makeText(this, "Houve um erro ao atualizar a disciplina", Toast.LENGTH_LONG).show();
        }

    }

    public void listaprovas(MDisciplina disciplina){
        //Toast.makeText(this, "Aqui lista provas", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, ViewScoresActivity.class);
        i.putExtra("disciplina", disciplina.getId());
        startActivity(i);
    }
    public void abremenu(AdapterView<?> parent, View view, final MDisciplina disciplina){
        PopupMenu pm = new PopupMenu(this, view);
        pm.inflate(R.menu.mnu_disciplinas);
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.compartilha:
                        compartilhar(disciplina);
                        break;
                    case R.id.deletar:
                        deleta(disciplina);
                        break;
                    case R.id.listarprovas:
                        listaprovas(disciplina);
                        break;
                    case R.id.editar:
                        editar(disciplina);
                        break;
                    default:
                }
                return true;
            }
        });
        pm.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_disciplines_activity);
        lvDisciplinas = (ListView)findViewById(R.id.lvdisciplinas);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Disciplina discdao = new Disciplina(this);
        List<MDisciplina> disciplinas = discdao.listarTodos();
        lvDisciplinas.setAdapter(new ViewDisciplinesListAdapter(disciplinas, this));
        lvDisciplinas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                abremenu(parent, view, (MDisciplina)parent.getItemAtPosition(position));
                return true;
            }
        });
        TextView tvIsEmpty = (TextView)findViewById(R.id.isEmpty);
        lvDisciplinas.setEmptyView(tvIsEmpty);
    }
}
