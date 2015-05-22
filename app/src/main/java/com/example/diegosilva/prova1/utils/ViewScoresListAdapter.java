package com.example.diegosilva.prova1.utils;

import android.app.Activity;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.diegosilva.prova1.R;
import com.example.diegosilva.prova1.utils.db.model.MProva;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Diego Silva on 2015-05-20.
 */
public class ViewScoresListAdapter extends BaseAdapter {
    List<MProva> provas;
    Activity a;

    public ViewScoresListAdapter(List<MProva> provas, Activity a) {
        this.a = a;
        this.provas = provas;
    }

    @Override
    public int getCount() {
        return provas.size();
    }

    @Override
    public Object getItem(int position) {
        return provas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = a.getLayoutInflater();
        View vw = li.inflate(R.layout.listview_scores_row_activity, parent, false);
        TextView txtnota = (TextView)vw.findViewById(R.id.txtnota);
        TextView txtProvaDescricao = (TextView)vw.findViewById(R.id.txtProvaDescricao);
        TextView txtDisciplina = (TextView)vw.findViewById(R.id.txtDisciplina);
        TextView txtProfessor = (TextView)vw.findViewById(R.id.txtProfessor);
        TextView txtData = (TextView)vw.findViewById(R.id.txtData);
        TextView txtHora = (TextView)vw.findViewById(R.id.txtHora);

        MProva prova = provas.get(position);
        if(prova.getNota() == null) {
            txtnota.setText("N/D");
            txtnota.setTextColor(Color.rgb(255, 255, 255));
        } else {
            txtnota.setText(prova.getNota().toString());
            if(prova.getNota() > 8)
                txtnota.setTextColor(Color.rgb(0, 255, 0));
            else if(prova.getNota() > 5)
                txtnota.setTextColor(Color.rgb(255, 255, 0));
            else if(prova.getNota() > 2)
                txtnota.setTextColor(Color.rgb(255, 128, 0));
            else
                txtnota.setTextColor(Color.rgb(255, 0, 0));
        }
        txtProvaDescricao.setText(prova.getDescricao());
        txtDisciplina.setText(prova.getDisciplina().getNome());
        txtProfessor.setText("Prof." + prova.getDisciplina().getProfessor());
        txtData.setText(DateFormat.getMediumDateFormat(a).format(prova.getDatahora()));
        txtHora.setText(DateFormat.getTimeFormat(a).format(prova.getDatahora()));

        return vw;
    }
}
