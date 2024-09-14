package com.tajy.monedaamoneda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText t1, t2;
    Button bt1;
    TextView tv1;
    String usu1, pass1;
    String dirip = "https://utic2025.com/lucas_rojas/monedaamoneda/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        t1 = findViewById(R.id.et_username);
        t2 = findViewById(R.id.et_password);
        bt1 = findViewById(R.id.btn_login);
        tv1 = findViewById(R.id.tv_create_account);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usu1 = t1.getText().toString();
                pass1 = t2.getText().toString();
                if (usu1.equals("") || pass1.equals("")) {
                    Toast.makeText(MainActivity.this, "Ingrese ambos datos para iniciar sesión.", Toast.LENGTH_SHORT).show();
                    t1.requestFocus();
                } else {
                    validarusuario(dirip + "login.php");
                }
            }
        });
        // Configurar el TextView para crear cuenta
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir el Activity para crear un nuevo usuario
                Intent intent = new Intent(MainActivity.this, nuevouser.class);
                startActivity(intent);
            }
        });
    }

    private void validarusuario(String URL) {
        StringRequest respuesta = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("ERROR1")) {
                            Toast.makeText(MainActivity.this, "Datos Incompletos", Toast.LENGTH_SHORT).show();
                            t1.requestFocus();
                        } else if (response.equalsIgnoreCase("ERROR2")) {
                            Toast.makeText(MainActivity.this, "Las credenciales no son correctas", Toast.LENGTH_SHORT).show();
                            t1.setText("");
                            t2.setText("");
                            t1.requestFocus();
                        }
                        else if (response.equalsIgnoreCase("ERROR3")) {
                            Toast.makeText(MainActivity.this, "Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
                            t1.setText("");
                            t2.setText("");
                            t1.requestFocus();
                        }else {
                            try {
                                // Parsear la respuesta como un JSON
                                JSONObject jsonResponse = new JSONObject(response);

                                // Obtener el estado y el ID del usuario del JSON
                                String status = jsonResponse.getString("status");
                                int idUsuario = jsonResponse.getInt("idUsuario");

                                // Almacenar el ID del usuario en SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("datosusuario", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("idUsuario", idUsuario);
                                editor.apply();

                                // Log para depurar
                                Log.d("SharedPreferences", "ID Usuario Recuperado Del Login: " + idUsuario);

                                // Manejar las respuestas según el estado
                                if (status.equalsIgnoreCase("ACCESOPRIMERAVEZ")) {
                                    // Iniciar la siguiente actividad
                                    Intent i1 = new Intent(getApplicationContext(), saldoinicial.class);
                                    startActivity(i1);
                                    finish();
                                } else if (status.equalsIgnoreCase("ACCESO")) {
                                    // Iniciar la siguiente actividad principal
                                    Intent i1 = new Intent(getApplicationContext(), egresos.class); // Reemplazar por tu Activity principal
                                    startActivity(i1);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "ERROR AL INICIAR SESIÓN"+error, Toast.LENGTH_LONG).show();
                t1.setText("");
                t2.setText("");
                t1.requestFocus();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new Hashtable<>();
                parametros.put("usuario1", t1.getText().toString());
                parametros.put("clave1", t2.getText().toString());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(respuesta);
    }
}
