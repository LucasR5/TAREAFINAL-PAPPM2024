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

public class NuevoIngreso extends AppCompatActivity {

    private static final String TAG = "NuevoIngreso";
    Spinner spinnerTiposIngreso;
    ArrayList<String> tiposIngresoList;
    ArrayList<String> tiposIngresoIdList; // Nueva lista para almacenar los IDs
    RequestQueue requestQueue;
    EditText inputMotivo, inputMonto;
    Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevoingreso);

        // Inicializar vistas
        spinnerTiposIngreso = findViewById(R.id.spinner_tipos_ingreso);
        inputMotivo = findViewById(R.id.input_motivo);
        inputMonto = findViewById(R.id.input_monto);
        btnAceptar = findViewById(R.id.btn_aceptar);

        tiposIngresoList = new ArrayList<>();
        tiposIngresoIdList = new ArrayList<>(); // Inicializamos la lista de IDs
        requestQueue = Volley.newRequestQueue(this);

        // Cargar tipos de ingreso
        cargarTiposIngreso();

        // Acción del botón aceptar
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatosIngreso();
            }
        });
    }

    // Método para cargar los tipos de ingreso desde el backend
    private void cargarTiposIngreso() {
        String url = "https://utic2025.com/lucas_rojas/monedaamoneda/listadeTipoIngresos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Respuesta del servidor: " + response); // Verifica la respuesta

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            tiposIngresoList.clear(); // Limpiar la lista antes de agregar nuevos datos
                            tiposIngresoIdList.clear(); // Limpiar la lista de IDs antes de agregar nuevos datos

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String tipoIngreso = jsonObject.getString("descripcionTipoIngreso");
                                String idTipoIngreso = jsonObject.getString("idtipoIngreso"); // Corregido aquí

                                tiposIngresoList.add(tipoIngreso);
                                tiposIngresoIdList.add(idTipoIngreso);
                            }

                            // Asignar los datos al spinner
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(NuevoIngreso.this, android.R.layout.simple_spinner_item, tiposIngresoList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTiposIngreso.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.e(TAG, "Error al procesar JSON: " + e.getMessage()); // Mensaje de error más claro
                            Toast.makeText(NuevoIngreso.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error en la conexión: " + error.getMessage()); // Mensaje de error más claro
                        Toast.makeText(NuevoIngreso.this, "Error en la conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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


    // Método para enviar los datos del ingreso al backend
    private void enviarDatosIngreso() {
        String motivo = inputMotivo.getText().toString().trim();
        String monto = inputMonto.getText().toString().trim();
        int tipoIngresoSeleccionado = spinnerTiposIngreso.getSelectedItemPosition();  // Obtener la posición del Spinner
        String idTipoIngresoSeleccionado = tiposIngresoIdList.get(tipoIngresoSeleccionado); // Obtener el idTipoIngreso correcto

        Log.d(TAG, "Este es lo que envia como motivo: " + motivo);
        if (motivo.isEmpty() || monto.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://utic2025.com/lucas_rojas/monedaamoneda/nuevoIngreso.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Respuesta recibida: " + response);
                        if (response.trim().equals("LOGRADO")) {
                            Toast.makeText(NuevoIngreso.this, "Ingreso registrado con éxito", Toast.LENGTH_SHORT).show();
                            // Volver a la pantalla anterior
                            finish();
                        } else {
                            Toast.makeText(NuevoIngreso.this, "Error al registrar el ingreso", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NuevoIngreso.this, "Error en la conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences preferences = getSharedPreferences("datosusuario", Context.MODE_PRIVATE);
                int idUsuario = preferences.getInt("idUsuario", -1);

                Map<String, String> params = new HashMap<>();
                params.put("idTipoIngreso", idTipoIngresoSeleccionado); // Ahora se envía el idTipoIngreso correcto
                params.put("descripcionIngreso", motivo);
                params.put("montoIngreso", monto);
                params.put("idUsuarios", String.valueOf(idUsuario));

                return params;
            }
        };

        // Añadir la solicitud a la cola
        requestQueue.add(stringRequest);
    }
}
