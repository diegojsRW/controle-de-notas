package com.example.diegosilva.prova1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diegosilva.prova1.utils.InputFilterMinMax;
import com.example.diegosilva.prova1.utils.db.dao.Disciplina;
import com.example.diegosilva.prova1.utils.db.dao.Prova;
import com.example.diegosilva.prova1.utils.db.model.MDisciplina;
import com.example.diegosilva.prova1.utils.db.model.MProva;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewTestActivity extends Activity {
    private Dialog dlg;
    //private Date data;
    private Calendar data;
    private boolean timeset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_test_activity);
        data = Calendar.getInstance();
        EditText txtNota = (EditText)findViewById(R.id.txtnota);
        txtNota.setFilters(new InputFilter[]{new InputFilterMinMax(0,10)});
        limpaCampos();
    }
    public void btnok_click(View v){
        DatePicker dpData = (DatePicker)dlg.findViewById(R.id.dpData);
        TimePicker tpHora = (TimePicker)dlg.findViewById(R.id.tpHora);

        int y, mo, d, h, mi, s;
        y = dpData.getYear();
        mo = dpData.getMonth();
        d = dpData.getDayOfMonth();
        h = tpHora.getCurrentHour();
        mi = tpHora.getCurrentMinute();

        Button btnDefineData = (Button)findViewById(R.id.btnDefineData);
        data.set(y, mo, d, h, mi);
        String st = android.text.format.DateFormat.format("dd/MM/yy 'às' HH:mm", data).toString();
        btnDefineData.setText(st);

        timeset = true;
        dlg.dismiss();
    }
    public void cancelaNovaProva(View v){
        finish();
    }
    public void cadastraProva(View v){
        Spinner spnDisciplina = (Spinner)findViewById(R.id.spnDisciplina);
        EditText txtProvaDescricao = (EditText)findViewById(R.id.txtProvaDescricao);
        Button btnDefineData = (Button)findViewById(R.id.btnDefineData);
        EditText txtNota = (EditText)findViewById(R.id.txtnota);
        if(spnDisciplina.getSelectedItemPosition() == AdapterView.INVALID_POSITION)
            Toast.makeText(this, "Selecione uma disciplina.", Toast.LENGTH_LONG).show();
        else if(txtProvaDescricao.getText().toString().trim().length() == 0)
            Toast.makeText(this, "Digite uma descrição para esta prova", Toast.LENGTH_LONG).show();
        else if(!timeset)
            Toast.makeText(this, "Defina uma data para esta prova", Toast.LENGTH_LONG).show();
        else {
            ArrayAdapter<MDisciplina> adp = (ArrayAdapter<MDisciplina>)spnDisciplina.getAdapter();
            MDisciplina disci = adp.getItem(spnDisciplina.getSelectedItemPosition());
            MProva prova = new MProva();
            prova.setDisciplina(disci);
            prova.setDescricao(txtProvaDescricao.getText().toString().trim());
            prova.setDatahora(new Timestamp(data.getTimeInMillis()));
            if(txtNota.getText().toString().trim().length() == 0)
                prova.setNota(null);
            else try {
                prova.setNota(1.0*Integer.parseInt(txtNota.getText().toString().trim()));
            } catch(Exception e){
                prova.setNota(null);
            }

            Prova provadao = new Prova(this);

            if(!provadao.inserir(prova))
                Toast.makeText(this, "Erro ao cadastrar prova. Tente novamente mais tarde", Toast.LENGTH_LONG).show();
            else
                new AlertDialog.Builder(this).setTitle("Cadastro efetuado").setMessage("A prova de \""+disci.getNome()+"\" para " + btnDefineData.getText() + " foi cadastrada com sucesso. \r\n"+
                "Deseja cadastrar mais provas?").setPositiveButton("Cadastrar mais", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewTestActivity.this.limpaCampos();
                    }
                }).setNegativeButton("Voltar ao início", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setIcon(android.R.drawable.ic_menu_save).show();
        }
    }

    private void limpaCampos() {
        EditText txtProvaDescricao = (EditText)findViewById(R.id.txtProvaDescricao);
        Spinner spnDisciplina = (Spinner)findViewById(R.id.spnDisciplina);
        Button btnDefineData = (Button)findViewById(R.id.btnDefineData);
        txtProvaDescricao.setText("");
        spnDisciplina.setSelection(0);

        timeset = false;
        btnDefineData.setText("Toque para definir data");
    }

    public void defineData(View v){

        dlg = new Dialog(this);

        LayoutInflater inflater = dlg.getLayoutInflater();
        View vinf = inflater.inflate(R.layout.dlg_datahora_layout, null);
        dlg.setContentView(vinf);
        dlg.setTitle("Data e hora da prova");
        Button btnok = (Button)dlg.findViewById(R.id.btnok);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTestActivity.this.btnok_click(v);
            }
        });
        DatePicker dpData = (DatePicker)dlg.findViewById(R.id.dpData);
        TimePicker tpHora = (TimePicker)dlg.findViewById(R.id.tpHora);
        tpHora.setIs24HourView(DateFormat.is24HourFormat(this));
        Calendar dat = Calendar.getInstance();

        if(timeset) {
        //    dpData.updateDate(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));
            dat.set(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));
            tpHora.setCurrentHour(data.get(Calendar.HOUR_OF_DAY));
            tpHora.setCurrentMinute(data.get(Calendar.MINUTE));

        }
        else {
            //Obtém o dia da semana que se tem aula com essa disciplina
            Spinner spnDisciplina = (Spinner)findViewById(R.id.spnDisciplina);
            MDisciplina disc = (MDisciplina)spnDisciplina.getSelectedItem();
            int diasemana = disc.getDiaSemana()+1; //de 0 a 6; somar mais um.
            int hojediasemana = dat.get(Calendar.DAY_OF_WEEK);
            /*

            DOMINGO = 1
            SEGUNDA = 2
            TER = 3
            QUA =4
            QUI=5
            SEX=6
            SÁB=7
            Se hoje é segunda, e a disciplina é /terça/q
             */
            if(diasemana >= hojediasemana)
                dat.add(Calendar.DATE, diasemana-hojediasemana);
            else /* tipo, hoje é quarta (4) e a disciplina é de segunda (2). Adiciono 5 dias até segunda*/
                dat.add(Calendar.DATE, 7 - (hojediasemana - diasemana));
        }

        dpData.init(dat.get(Calendar.YEAR), dat.get(Calendar.MONTH), dat.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                TextView txthelperdata = (TextView)dlg.findViewById(R.id.txthelperdata);
                Calendar caldate = Calendar.getInstance();
                caldate.set(year, monthOfYear, dayOfMonth);
                txthelperdata.setText(DateFormatSymbols.getInstance(new Locale("pt","BR")).getWeekdays()[caldate.get(Calendar.DAY_OF_WEEK)]);
            }
        });
        TextView txthelperdata = (TextView)dlg.findViewById(R.id.txthelperdata);
        txthelperdata.setText(DateFormatSymbols.getInstance(new Locale("pt","BR")).getWeekdays()[dat.get(Calendar.DAY_OF_WEEK)]);
        dlg.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Spinner spnDisciplina = (Spinner)findViewById(R.id.spnDisciplina);
        Disciplina disc = new Disciplina(this);
        List<MDisciplina> itens = new ArrayList<MDisciplina>();
        for(MDisciplina item : disc.listarTodos()) {
            itens.add(item);
        }
        MDisciplina[] itenss = new MDisciplina[itens.size()];
        itens.toArray(itenss);

        ArrayAdapter<MDisciplina> adp = new ArrayAdapter<MDisciplina>(this, android.R.layout.simple_spinner_item, itenss);
        spnDisciplina.setAdapter(adp);
    }
}
