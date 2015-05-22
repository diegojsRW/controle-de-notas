package com.example.diegosilva.prova1.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.diegosilva.prova1.HomeActivity;
import com.example.diegosilva.prova1.R;
import com.example.diegosilva.prova1.utils.db.model.MProva;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Silva on 2015-05-19.
 */
public class NextTestsListAdapter extends BaseAdapter {
    private List<MProva> provas;
    private Activity a;


    public NextTestsListAdapter(List<MProva> provas, Activity a){
        this.provas = provas;
        this.a = a;
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
        View row = li.inflate(R.layout.listview_tests_row_activity, parent, false);
        TextView txtDiscipline, txtDescription, txtDatetime;
        txtDiscipline = (TextView)row.findViewById(R.id.txtdiscipline);
        txtDatetime = (TextView)row.findViewById(R.id.txtdatetime);
        txtDescription = (TextView)row.findViewById(R.id.txtdescription);
        txtDiscipline.setText(provas.get(position).getDisciplina().getNomeAbreviado());
        txtDescription.setText(provas.get(position).getDescricao());
        txtDatetime.setText(android.text.format.DateFormat.getDateFormat(
                a.getApplicationContext()).format(provas.get(position).getDatahora()) + " " +
                            android.text.format.DateFormat.getTimeFormat(
                a.getApplicationContext()).format(provas.get(position).getDatahora())
        );

        return row;
    }
}
