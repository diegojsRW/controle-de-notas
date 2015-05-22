package com.example.diegosilva.prova1;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.*;

import com.example.diegosilva.prova1.utils.NextTestsListAdapter;
import com.example.diegosilva.prova1.utils.db.dao.Disciplina;
import com.example.diegosilva.prova1.utils.db.dao.Prova;
import com.example.diegosilva.prova1.utils.db.model.MProva;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.crypto.spec.GCMParameterSpec;

public class HomeActivity extends Activity {
    private ListView lvProximasProvas;
    private boolean prevProva;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        prevProva = false;
        lvProximasProvas = (ListView)findViewById(R.id.lvProximasProvas);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar saída")
                .setMessage("Tem certeza de que deseja sair do aplicativo?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton){
                        Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("QUITFLAG", true);
                        startActivity(i);
                        finish();
                    }
                }).setNegativeButton("Voltar ao aplicativo", null).show();
    }
    public void novaDisciplina(View v){
        Intent i = new Intent(this, NewDisciplineActivity.class);
        startActivity(i);
    }
    public void novaProva(final View v){
        Disciplina discdao = new Disciplina(this);
        if(discdao.listarTodos().size() == 0)
            new AlertDialog.Builder(this)
                    .setTitle("Nenhuma disciplina cadastrada")
                    .setIcon(android.R.drawable.ic_menu_help)
                    .setMessage("No momento, ainda não existe nenhuma disciplina cadastrada. \r\nDeseja cadastrar uma agora?")
                    .setNegativeButton("Não, obrigado.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).setPositiveButton("Sim, por favor", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    prevProva = true;
                    novaDisciplina(v);
                }
            }).show();
        else {
            Intent i = new Intent(this, NewTestActivity.class);
            startActivity(i);
        }
    }
    public void verDisciplinas(View v){
        Intent i = new Intent(this, ViewDisciplinesActivity.class);
        startActivity(i);
    }
    public void verNotas(View v){
        Intent i = new Intent(this, ViewScoresActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Prova provadao = new Prova(this);

        Calendar caldate = Calendar.getInstance();

        List<MProva> provas = provadao.listar("datahora >= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(caldate.getTime())+"'");
        lvProximasProvas.setAdapter(new NextTestsListAdapter(provas, this));

        if(prevProva){
            prevProva = false;
            Disciplina discdao = new Disciplina(this);
            if(!discdao.listarTodos().isEmpty()) {
                Intent i = new Intent(this, NewTestActivity.class);
                startActivity(i);
            }
        }

    }
}
