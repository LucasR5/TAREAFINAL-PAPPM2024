package com.tajy.monedaamoneda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tajy.monedaamoneda.EgresosAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class egresos extends AppCompatActivity {
    ListView listaGastos;
    EgresosAdapter adapter;
    ArrayList<JSONObject> gastosList;
    Button btnNuevoTipoEgreso, btnAgregarGasto, btnVerIngresos, btnBalanceGeneral;
    TextView tvTotalGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_egresos);

        listaGastos = findViewById(R.id.lista_gastos);
        btnNuevoTipoEgreso = findViewById(R.id.btn_nuevo_tipo_egreso);
        btnAgregarGasto = findViewById(R.id.btn_agregar_gasto);
        btnBalanceGeneral = findViewById(R.id.btn_ver_balance_general);
        tvTotalGastos = findViewById(R.id.tv_total_gastos);
        gastosList = new ArrayList<>();
        btnVerIngresos = findViewById(R.id.btn_verIngresos);

        // Cargar los datos de la base de datos
        cargarDatos();
        cargarTotalGastos();

        // Configurar el botón para abrir la nueva actividad para añadir tipo de egreso
        btnNuevoTipoEgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(egresos.this, NuevoTipoEgreso.class);
                startActivity(intent);
            }
        });

        // Configurar el botón para abrir la nueva actividad para añadir un nuevo gasto
        btnAgregarGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(egresos.this, nuevoEgreso.class);
                startActivity(intent);
            }
        });

        // Configurar el botón para abrir la actividad de ingresos
        btnVerIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(egresos.this, Ingresos.class);
                startActivity(intent);
            }
        });

        // Configurar el botón para abrir la pantalla de balance general
        btnBalanceGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(egresos.this, balancegeneral.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
        cargarTotalGastos();
    }

    private void cargarDatos() {
        String url = "https://utic2025.com/lucas_rojas/monedaamoneda/listaegresos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            // Limpiar la lista
                            gastosList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                gastosList.add(jsonArray.getJSONObject(i));
                            }

                            // Crear el adaptador y asignarlo al ListView
                            adapter = new EgresosAdapter(egresos.this, gastosList);
                            listaGastos.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(egresos.this, "Error al parsear los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(egresos.this, "Error en la conexión: " + error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences preferences = getSharedPreferences("datosusuario", Context.MODE_PRIVATE);
                int idUsuario = preferences.getInt("idUsuario", -1);

                Map<String, String> params = new HashMap<>();
                params.put("idUsuario", String.valueOf(idUsuario));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void cargarTotalGastos() {
        String url = "https://utic2025.com/lucas_rojas/monedaamoneda/totalegresos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int totalGastos = jsonObject.getInt("total_gastos");
                            tvTotalGastos.setText("Total Gastos: $" + totalGastos);
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(egresos.this, "Error en la conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences preferences = getSharedPreferences("datosusuario", Context.MODE_PRIVATE);
                int idUsuario = preferences.getInt("idUsuario", -1);

                Map<String, String> params = new HashMap<>();
                params.put("idUsuario", String.valueOf(idUsuario));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}