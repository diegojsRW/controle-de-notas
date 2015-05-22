package com.example.diegosilva.prova1.utils.db;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.diegosilva.prova1.R;
import com.example.diegosilva.prova1.utils.db.model.MDisciplina;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Diego Silva on 2015-05-20.
 */
public class ViewDisciplinesListAdapter extends BaseAdapter {
    List<MDisciplina> disciplinas;
    Activity a;

    public ViewDisciplinesListAdapter(List<MDisciplina> disciplinas, Activity a) {
        this.disciplinas = disciplinas;
        this.a = a;
    }

    @Override
    public int getCount() {
        return disciplinas.size();
    }

    @Override
    public Object getItem(int position) {
        return disciplinas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MDisciplina disc = disciplinas.get(position);
        LayoutInflater li = a.getLayoutInflater();
        View vw = li.inflate(R.layout.listview_disc_row_activity, parent, false);
        TextView txtDisciplinaNome = (TextView)vw.findViewById(R.id.txtDisciplinaNome);
        TextView txtProfessor = (TextView)vw.findViewById(R.id.txtProfessor);
        TextView txtDisciplinaDia = (TextView)vw.findViewById(R.id.txtDisciplinaDia);

        txtDisciplinaNome.setText(disc.getNome() + " (" + disc.getNomeAbreviado() + ")");
        txtProfessor.setText("Prof. " + disc.getProfessor());
        txtDisciplinaDia.setText("Aulas de " + new DateFormatSymbols(new Locale("pt", "BR")).getWeekdays()[disc.getDiaSemana()+1]);
        return vw;

    }
}
