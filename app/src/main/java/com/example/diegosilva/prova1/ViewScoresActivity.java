package com.example.diegosilva.prova1;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.text.InputFilter;
import android.text.Layout;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diegosilva.prova1.utils.InputFilterMinMax;
import com.example.diegosilva.prova1.utils.ViewScoresListAdapter;
import com.example.diegosilva.prova1.utils.db.dao.Prova;
import com.example.diegosilva.prova1.utils.db.model.MDisciplina;
import com.example.diegosilva.prova1.utils.db.model.MProva;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Diego Silva on 2015-05-20.
 */
public class ViewScoresActivity extends Activity {
    public Dialog dlg;
    public String whereStat;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(whereStat.length() == 0)
            return false;
        else {
            getMenuInflater().inflate(R.menu.mnu_limpafiltro_disciplina, menu);
            return true;
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(whereStat.length() > 0)
            return true;
        else {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.limpafiltro:
                whereStat = "";
                invalidateOptionsMenu();
                onResume();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_scores_activity);
        if(getIntent().hasExtra("disciplina"))
            whereStat = "disciplina = " + String.valueOf(getIntent().getIntExtra("disciplina", 0));
        else
            whereStat = "";
    }

    public void comecaeditanota(final MProva prova){
        dlg = new Dialog(ViewScoresActivity.this);
        LayoutInflater inflater = dlg.getLayoutInflater();
        View vinf = inflater.inflate(R.layout.update_scores_activity, null);
        dlg.setContentView(vinf);
        dlg.setTitle("Atualizar nota");
        dlg.setCancelable(false);

        Button btnCancela = (Button)dlg.findViewById(R.id.btnCancela);
        Button btnAtualizaNota = (Button)dlg.findViewById(R.id.btnAtualizaNota);
        EditText txtnota = (EditText)dlg.findViewById(R.id.txtnota);
        txtnota.setFilters(new InputFilter[]{new InputFilterMinMax(0,10)});

        btnCancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                dlg=null;
            }
        });
        btnAtualizaNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewScoresActivity.this.editanota(prova);
            }
        });
        dlg.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                EditText txtnota = (EditText)dlg.findViewById(R.id.txtnota);
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(txtnota, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        dlg.setCanceledOnTouchOutside(false);
        dlg.show();
    }



    public void compartilhar(MProva prova){
        String hashtag="";
        String provacompartilhada;
        String temporesposta = "";
        Calendar dt = Calendar.getInstance();
        Calendar odt = Calendar.getInstance();
        odt.setTimeInMillis(prova.getDatahora().getTime());

        if(odt.after(dt.getTime()))
            temporesposta = "vou responder";
        else
            temporesposta = "respondi";

        if(prova.getNota() == null) {
            provacompartilhada = "#MyTestsGrades: " + temporesposta + " em "+
                    DateFormat.getMediumDateFormat(ViewScoresActivity.this).format(prova.getDatahora())+
                    " às " +
                    DateFormat.getTimeFormat(ViewScoresActivity.this).format(prova.getDatahora())+
                    ", mas ainda não sei minha nota. #torçampormim. :|";
        } else {
            if(prova.getNota() == 10)
                hashtag = "#gabaritei";
            else if(prova.getNota() > 7)
                hashtag = "#quasegabaritei";
            else if(prova.getNota() > 5)
                hashtag = "#foisorte";
            else if(prova.getNota() > 2)
                hashtag = "#achoquemeferrei";
            else hashtag = "#meferrei";


            provacompartilhada = String.format("#MyTestGrades: "+temporesposta+" em "+
                    DateFormat.getMediumDateFormat(ViewScoresActivity.this).format(prova.getDatahora())+
                    " às " +
                    DateFormat.getTimeFormat(ViewScoresActivity.this).format(prova.getDatahora())+
                    " e tirei %02.1f na "+
                    prova.getDescricao()+
                    " do Prof. "+
                    prova.getDisciplina().getProfessor()+
                    " em "+
                    prova.getDisciplina().getNome()+
                    ". "+hashtag,  prova.getNota());
        }
        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_TEXT,
                provacompartilhada);
        i.setType("text/plain");

        startActivity(Intent.createChooser(i, "Escolher aplicativo"));
    }
    public void editanota(MProva prova){
        EditText txtnota = (EditText)dlg.findViewById(R.id.txtnota);
        if(txtnota.getText().toString().trim().length() == 0) {
            Toast.makeText(ViewScoresActivity.this, "Nota incorreta", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            prova.setNota(1.0 * Integer.parseInt(txtnota.getText().toString()));
        }
        catch (Exception e){
            Toast.makeText(ViewScoresActivity.this, "Nota incorreta", Toast.LENGTH_LONG).show();
            return;
        }
        Prova provadao = new Prova(ViewScoresActivity.this);
        provadao.atualizar(prova);
        dlg.dismiss();
        ViewScoresActivity.this.onResume();
    }
    public void deletaProva(final MProva prova){
        new AlertDialog.Builder(this).setTitle("Confirmar exclusão")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setMessage("A prova \"" + prova.getDescricao() + "\" " +
                        "para " + DateFormat.getMediumDateFormat(this).format(prova.getDatahora()) +
                        " às " + DateFormat.getTimeFormat(this).format(prova.getDatahora()) +
                        " será removida. Continuar?").setPositiveButton("Sim, remova.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Prova provadao = new Prova(ViewScoresActivity.this);
                if(provadao.remover(prova))
                    Toast.makeText(ViewScoresActivity.this, "Prova removida com sucesso", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ViewScoresActivity.this, "Houve um erro e sua prova não pode ser removida.", Toast.LENGTH_LONG).show();
                 onResume();
                 dialog.dismiss();
            }
        }).setNegativeButton("Não, espere!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();

    }
    @Override
    protected void onResume() {
        super.onResume();
        ListView lvNotas = (ListView)findViewById(R.id.lvNotas);
        Prova provadao = new Prova(this);
        List<MProva> provas = provadao.listar(whereStat);
        lvNotas.setAdapter(new ViewScoresListAdapter(provas, this));
        lvNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final MProva prova = (MProva)parent.getItemAtPosition(position);
                dlg = new Dialog(ViewScoresActivity.this);
                LayoutInflater inflater = dlg.getLayoutInflater();
                View vinf = inflater.inflate(R.layout.update_scores_activity, null);
                dlg.setContentView(vinf);
                dlg.setTitle("Atualizar nota");
                dlg.setCancelable(false);

                Button btnCancela = (Button)dlg.findViewById(R.id.btnCancela);
                Button btnAtualizaNota = (Button)dlg.findViewById(R.id.btnAtualizaNota);
                EditText txtnota = (EditText)dlg.findViewById(R.id.txtnota);
                txtnota.setFilters(new InputFilter[]{new InputFilterMinMax(0,10)});

                btnCancela.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                        dlg=null;
                    }
                });
                btnAtualizaNota.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       ViewScoresActivity.this.editanota(prova);
                    }
                });
                dlg.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        EditText txtnota = (EditText)dlg.findViewById(R.id.txtnota);
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.showSoftInput(txtnota, InputMethodManager.SHOW_IMPLICIT);
                    }
                });

                dlg.setCanceledOnTouchOutside(false);
                dlg.show();
            }
        });
        lvNotas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final MProva prova = (MProva)parent.getItemAtPosition(position);
                PopupMenu pm = new PopupMenu(ViewScoresActivity.this, view);
                pm.inflate(R.menu.mnu_prova);
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.deletar:
                                deletaProva(prova);
                                break;
                            case R.id.compartilha:
                                compartilhar(prova);
                                break;
                            case R.id.editadata:
                                editadata(prova);
                                break;
                            case R.id.editanota:
                                comecaeditanota(prova);
                                break;
                            default:
                        }
                        return true;
                    }
                });
                pm.show();
                return true;
            }
        });
        TextView tvIsEmpty = (TextView)findViewById(R.id.isEmpty);
        lvNotas.setEmptyView(tvIsEmpty);
    }

    public void editadata(final MProva prova) {

        dlg = new Dialog(this);

        LayoutInflater inflater = dlg.getLayoutInflater();
        View vinf = inflater.inflate(R.layout.dlg_datahora_layout, null);
        dlg.setContentView(vinf);
        dlg.setTitle("Data e hora da prova");
        Button btnok = (Button)dlg.findViewById(R.id.btnok);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewScoresActivity.this.btnok_click(v, prova);
            }
        });
        DatePicker dpData = (DatePicker)dlg.findViewById(R.id.dpData);
        TimePicker tpHora = (TimePicker)dlg.findViewById(R.id.tpHora);
        tpHora.setIs24HourView(DateFormat.is24HourFormat(this));
        Calendar data = Calendar.getInstance();
        data.setTimeInMillis(prova.getDatahora().getTime());

        //dpData.updateDate(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));
        tpHora.setCurrentHour(data.get(Calendar.HOUR_OF_DAY));
        tpHora.setCurrentMinute(data.get(Calendar.MINUTE));

        tpHora.setIs24HourView(DateFormat.is24HourFormat(this));

        dpData.init(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                TextView txthelperdata = (TextView)dlg.findViewById(R.id.txthelperdata);
                Calendar caldate = Calendar.getInstance();
                caldate.set(year, monthOfYear, dayOfMonth);
                txthelperdata.setText(DateFormatSymbols.getInstance(new Locale("pt", "BR")).getWeekdays()[caldate.get(Calendar.DAY_OF_WEEK)]);
            }
        });
        TextView txthelperdata = (TextView)dlg.findViewById(R.id.txthelperdata);
        txthelperdata.setText(DateFormatSymbols.getInstance(new Locale("pt","BR")).getWeekdays()[data.get(Calendar.DAY_OF_WEEK)]);
        dlg.show();
    }

    public  void btnok_click(View v, MProva prova) {
        DatePicker dpData = (DatePicker)dlg.findViewById(R.id.dpData);
        TimePicker tpHora = (TimePicker)dlg.findViewById(R.id.tpHora);

        int y, mo, d, h, mi, s;
        y = dpData.getYear();
        mo = dpData.getMonth();
        d = dpData.getDayOfMonth();
        h = tpHora.getCurrentHour();
        mi = tpHora.getCurrentMinute();
        Calendar data = Calendar.getInstance();
        data.set(y,mo,d,h,mi);
        prova.setDatahora(new Timestamp(data.getTimeInMillis()));
        Prova provadao = new Prova(this);
        if(provadao.atualizar(prova)){
            Toast.makeText(this, "Data da prova foi atualizada", Toast.LENGTH_LONG).show();
            onResume();
        }
        else {
            Toast.makeText(this, "Falha ao alterar data da prova.", Toast.LENGTH_LONG).show();
        }

        dlg.dismiss();
    }
}
