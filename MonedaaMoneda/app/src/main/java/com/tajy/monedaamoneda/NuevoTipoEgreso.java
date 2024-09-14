package com.tajy.monedaamoneda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NuevoTipoEgreso extends AppCompatActivity {

    EditText inputNombreTipo;
    Button botonConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establecer el diseño XML para esta actividad
        setContentView(R.layout.activity_nuevotipoegreso);

        // Referenciar los elementos del XML
        inputNombreTipo = findViewById(R.id.input_nombre_tipo);
        botonConfirmar = findViewById(R.id.boton_confirmar);

        // Agregar un listener al botón de confirmar
        botonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el nombre del tipo de egreso
                String nombreTipoEgreso = inputNombreTipo.getText().toString();

                // Validar que no esté vacío
                if (nombreTipoEgreso.isEmpty()) {
                    Toast.makeText(NuevoTipoEgreso.this, "Por favor, ingrese un nombre", Toast.LENGTH_SHORT).show();
                } else {
                    // Llamar al método para enviar los datos
                    agregarTipoEgreso(nombreTipoEgreso);
                }
            }
        });
    }

    private void agregarTipoEgreso(final String nombreTipoEgreso) {
        String url = "https://utic2025.com/lucas_rojas/monedaamoneda/nuevotipoegreso.php"; // URL de tu backend PHP

        // Crear una solicitud POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Mostrar el resultado que viene del servidor
                        if (response.equals("LOGRADO")) {
                            Toast.makeText(NuevoTipoEgreso.this, "Tipo de egreso añadido con éxito", Toast.LENGTH_SHORT).show();
                            finish(); // Esto devuelve a la ventana anterior
                        } else {
                            Toast.makeText(NuevoTipoEgreso.this, "Error al insertar el tipo de egreso", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NuevoTipoEgreso.this, "Error en la conexión: " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Obtener el idUsuario desde SharedPreferences
                SharedPreferences preferences = getSharedPreferences("datosusuario", Context.MODE_PRIVATE);
                int idUsuario = preferences.getInt("idUsuario", -1);

                // Crear los parámetros a enviar
                Map<String, String> params = new HashMap<>();
                params.put("nombretipoegreso", nombreTipoEgreso); // El nombre del tipo de egreso
                params.put("idUsuario", String.valueOf(idUsuario)); // El ID del usuario
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes de Volley
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
