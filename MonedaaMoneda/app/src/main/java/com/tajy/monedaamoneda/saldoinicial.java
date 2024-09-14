package com.tajy.monedaamoneda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Hashtable;
import java.util.Map;

public class saldoinicial extends AppCompatActivity {

    EditText t1;
    Button bt1;
    String dirip = "https://utic2025.com/lucas_rojas/monedaamoneda/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldoinicial);

        t1 = findViewById(R.id.et_monto_inicial);
        bt1 = findViewById(R.id.btn_iniciar);

        // Ahora solo actualiza el saldo cuando el botón es presionado
        bt1.setOnClickListener(v -> {
            actualizarSaldo(dirip + "actualizarSaldo.php");
        });
    }

    public void actualizarSaldo(String URL) {
        // Obtener el idUsuario desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("datosusuario", Context.MODE_PRIVATE);
        int idUsuario = preferences.getInt("idUsuario", -1); // -1 es el valor predeterminado si no se encuentra la clave

        // Log para depurar
        Log.d("SharedPreferences", "ID Usuario Recuperado: " + idUsuario);




        StringRequest respuesta = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ServerResponse", "Respuesta del Servidor: " + response);
                        if (response.trim().equalsIgnoreCase("LOGRADO")) {
                            Toast.makeText(saldoinicial.this, "Saldo Inicial Definido", Toast.LENGTH_SHORT).show();
                            Intent i1 = new Intent(getApplicationContext(), egresos.class);
                            startActivity(i1);
                            finish();
                        } else {
                            Toast.makeText(saldoinicial.this, "Error al definir saldo", Toast.LENGTH_SHORT).show();
                            t1.setText("");
                            t1.requestFocus();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(saldoinicial.this, "ERROR AL INSERTAR " + error, Toast.LENGTH_LONG).show();
                t1.setText("");
                t1.requestFocus();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new Hashtable<>();
                parametros.put("et_monto_inicial", t1.getText().toString());
                parametros.put("idUsuario", String.valueOf(idUsuario)); // Enviar el ID de usuario con el saldo
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(saldoinicial.this);
        requestQueue.add(respuesta);
    }
}
