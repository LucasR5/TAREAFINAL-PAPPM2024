package com.tajy.monedaamoneda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EgresosAdapter extends ArrayAdapter<JSONObject> {

    private final Context context;
    private final ArrayList<JSONObject> data;

    public EgresosAdapter(Context context, ArrayList<JSONObject> data) {
        super(context, R.layout.list_item, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView descripcionTextView = convertView.findViewById(R.id.descripciónTextView);
        TextView fechaTextView = convertView.findViewById(R.id.fechaTextView);
        TextView montoTextView = convertView.findViewById(R.id.montoTextView);
        TextView tipoTextView = convertView.findViewById(R.id.tipoTextView);

        JSONObject item = getItem(position);
        try {
            String descripcionEgreso = item.getString("descripcionEgreso");
            String fechaEgreso = item.getString("fechaEgreso");
            String montoIngreso = item.getString("montoIngreso");
            String tipoGasto = item.getString("descripcionTipoEgreso");

            descripcionTextView.setText(descripcionEgreso);
            fechaTextView.setText(formatDate(fechaEgreso)); // Usa el método para formatear la fecha
            montoTextView.setText(montoIngreso);
            tipoTextView.setText(tipoGasto);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }


    private String formatDate(String dateTime) {
        // Asumiendo que dateTime ya está en el formato "MM-dd HH:mm"
        return dateTime;
    }

}