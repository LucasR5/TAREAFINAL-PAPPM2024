package com.tajy.monedaamoneda;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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


public class nuevouser extends AppCompatActivity {
    EditText t1, t2, t3;
    Button bt1;
    String usu1, pass1, pass2;
    String dirip = "https://utic2025.com/lucas_rojas/monedaamoneda/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevouser);
        t1 = findViewById(R.id.et_username);
        t2 = findViewById(R.id.et_password);
        t3 = findViewById(R.id.et_confirm_password);
        bt1 = findViewById(R.id.btn_register);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usu1 = t1.getText().toString();
                pass1 = t2.getText().toString();
                pass2 = t3.getText().toString();
                if (usu1.equals("") || pass1.equals("") || pass2.equals("")) {
                    Toast.makeText(nuevouser.this, "Complete todos los campos para registrarse.", Toast.LENGTH_SHORT).show();
                    t1.requestFocus();
                } else {
                    registrarUsuario(dirip + "crearUsuario.php", usu1, pass1, pass2);
                }
            }
        });
    }
    public void registrarUsuario(String URL, String usuario,String clave1, String clave2 ){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Respuesta del servidor", response);

                        if (response.trim().equalsIgnoreCase("LOGRADO")) {
                            Toast.makeText(nuevouser.this, "Usuario registrado con éxito", Toast.LENGTH_LONG).show();
                            // Redirigir al login
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(nuevouser.this, "Error: " + response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(nuevouser.this, "Error en la conexión: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new Hashtable<>();
                parametros.put("usuario1", usuario);
                parametros.put("clave1", clave1);
                parametros.put("clave2", clave2);
                return parametros;
            }
        };

        // Añadir la solicitud a la cola
        RequestQueue requestQueue = Volley.newRequestQueue(nuevouser.this);
        requestQueue.add(stringRequest);

    }
}
