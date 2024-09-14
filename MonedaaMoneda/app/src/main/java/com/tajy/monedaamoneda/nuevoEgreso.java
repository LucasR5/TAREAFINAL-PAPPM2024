package com.tajy.monedaamoneda;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class nuevoEgreso extends AppCompatActivity {

    private static final String TAG = "nuevoEgreso";
    Spinner spinnerTiposEgreso;
    ArrayList<String> tiposEgresoList;
    ArrayList<String> tiposEgresoIdList; // Nueva lista para almacenar los ids
    RequestQueue requestQueue;
    EditText inputMotivo, inputMonto;
    Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevoegreso);

        // Inicializar vistas
        spinnerTiposEgreso = findViewById(R.id.spinner_tipos_egreso);
        inputMotivo = findViewById(R.id.input_motivo);
        inputMonto = findViewById(R.id.input_monto);
        btnAceptar = findViewById(R.id.btn_aceptar);

        tiposEgresoList = new ArrayList<>();
        tiposEgresoIdList = new ArrayList<>(); // Inicializamos la lista de ids
        requestQueue = Volley.newRequestQueue(this);

        // Cargar tipos de egreso
        cargarTiposEgreso();

        // Acción del botón aceptar
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatosEgreso();
            }
        });
    }

    // Método para cargar los tipos de egreso desde el backend
    private void cargarTiposEgreso() {
        String url = "https://utic2025.com/lucas_rojas/monedaamoneda/listadeTipoEgresos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            // Log para verificar la respuesta del servidor
                            Log.d("BalanceGeneral", "Respuesta del servidor: " + response);
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String tipoEgreso = jsonObject.getString("descripcionTipoEgreso");
                                String idTipoEgreso = jsonObject.getString("idTipoEgreso"); // Obtener idTipoEgreso

                                tiposEgresoList.add(tipoEgreso);
                                tiposEgresoIdList.add(idTipoEgreso); // Guardar el idTipoEgreso
                            }

                            // Asignar los datos al spinner
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(nuevoEgreso.this, android.R.layout.simple_spinner_item, tiposEgresoList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTiposEgreso.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(nuevoEgreso.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(nuevoEgreso.this, "Error en la conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences preferences = getSharedPreferences("datosusuario", Context.MODE_PRIVATE);
                int idUsuario = preferences.getInt("idUsuario", -1);

                Map<String, String> params = new HashMap<>();
                params.put("idUsuario", String.valueOf(idUsuario));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    // Método para enviar los datos del egreso al backend
    private void enviarDatosEgreso() {
        String motivo = inputMotivo.getText().toString().trim();
        String monto = inputMonto.getText().toString().trim();
        int tipoEgresoSeleccionado = spinnerTiposEgreso.getSelectedItemPosition();  // Obtener la posición del Spinner
        String idTipoEgresoSeleccionado = tiposEgresoIdList.get(tipoEgresoSeleccionado); // Obtener el idTipoEgreso correcto

        Log.d(TAG, "Este es lo que envia como motivo: " + motivo);
        if (motivo.isEmpty() || monto.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://utic2025.com/lucas_rojas/monedaamoneda/nuevoEgreso.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Respuesta recibida2: " + response);
                        if (response.trim().equals("LOGRADO")) {
                            Toast.makeText(nuevoEgreso.this, "Egreso registrado con éxito", Toast.LENGTH_SHORT).show();
                            // Volver a la pantalla anterior
                            finish();
                        } else {
                            Toast.makeText(nuevoEgreso.this, "Error al registrar el egreso", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(nuevoEgreso.this, "Error en la conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences preferences = getSharedPreferences("datosusuario", Context.MODE_PRIVATE);
                int idUsuario = preferences.getInt("idUsuario", -1);

                Map<String, String> params = new HashMap<>();
                params.put("idtipoEgreso", idTipoEgresoSeleccionado); // Ahora se envía el idTipoEgreso correcto
                params.put("descripcionEgreso", motivo);
                params.put("montoIngreso", monto);
                params.put("idUsuarios", String.valueOf(idUsuario));

                return params;
            }
        };

        // Añadir la solicitud a la cola
        requestQueue.add(stringRequest);
    }
}
