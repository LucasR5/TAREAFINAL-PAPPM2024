package com.tajy.monedaamoneda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ingresos extends AppCompatActivity {
    ListView listaIngresos;
    IngresosAdapter adapter;
    ArrayList<JSONObject> ingresosList;
    Button btnNuevoTipoIngreso, btnAgregarIngreso, btnVerBalanceGeneral, btnVerEgresos;
    TextView tvTotalIngresos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ingresos);

        listaIngresos = findViewById(R.id.lista_ganancias);
        btnNuevoTipoIngreso = findViewById(R.id.btn_nuevo_tipo_ingreso);
        btnAgregarIngreso = findViewById(R.id.btn_agregar_ganancia);
        btnVerBalanceGeneral = findViewById(R.id.btn_ver_balance_general);
        tvTotalIngresos = findViewById(R.id.tv_total_ingresos);
        ingresosList = new ArrayList<>();
        btnVerEgresos = findViewById(R.id.btn_volverAEgresos);

        // Cargar los datos de la base de datos
        cargarDatos();
        cargarTotalIngresos();

        // Configurar el botón para abrir la nueva actividad para añadir tipo de ingreso
        btnNuevoTipoIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ingresos.this, NuevoTipoIngreso.class);
                startActivity(intent);
            }
        });

        // Configurar el botón para abrir la nueva actividad para añadir un nuevo ingreso
        btnAgregarIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ingresos.this, NuevoIngreso.class);
                startActivity(intent);
            }
        });

        // Configurar el botón para abrir la actividad de egresos
        btnVerEgresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ingresos.this, egresos.class);
                startActivity(intent);
            }
        });

        // Configurar el botón para abrir la actividad de balance general
        btnVerBalanceGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ingresos.this, balancegeneral.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
        cargarTotalIngresos();
    }

    private void cargarDatos() {
        String url = "https://utic2025.com/lucas_rojas/monedaamoneda/listaingresos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Imprimir la respuesta para depuración
                            Log.d("Respuesta Ingresos", response);

                            JSONArray jsonArray = new JSONArray(response);

                            // Limpiar la lista
                            ingresosList.clear();

                            // Agregar datos a la lista
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ingresosList.add(jsonArray.getJSONObject(i));
                            }

                            // Crear el adaptador y asignarlo al ListView
                            adapter = new IngresosAdapter(Ingresos.this, ingresosList);
                            listaIngresos.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Ingresos.this, "Error al parsear los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Imprimir el error para depuración
                        Log.e("Error Ingresos", error.getMessage());

                        Toast.makeText(Ingresos.this, "Error en la conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

        // Añadir la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void cargarTotalIngresos() {
        String url = "https://utic2025.com/lucas_rojas/monedaamoneda/totalingresos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int totalIngresos = jsonObject.getInt("total_ganancias");
                            tvTotalIngresos.setText("Total Ingresos: $" + totalIngresos);
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Ingresos.this, "Error en la conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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