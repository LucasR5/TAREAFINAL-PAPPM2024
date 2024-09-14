package com.tajy.monedaamoneda;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class balancegeneral extends AppCompatActivity {

    private TextView tvSaldoFinal;
    private BarChart barChart;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancegeneral);

        tvSaldoFinal = findViewById(R.id.tvSaldoFinal);
        barChart = findViewById(R.id.barChart);
        btnVolver = findViewById(R.id.btnVolver);

        // Llamar a la función para obtener los datos y mostrar el gráfico
        obtenerDatosBalance();

        // Configurar el botón de "Volver"
        btnVolver.setOnClickListener(v -> {
            // Llamar a finish() para volver a la actividad anterior
            finish();
        });
    }

    private void obtenerDatosBalance() {
        // Obtener el idUsuario desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("datosusuario", Context.MODE_PRIVATE);
        int idUsuario = preferences.getInt("idUsuario", -1);  // -1 es el valor por defecto si no se encuentra

        if (idUsuario == -1) {
            // Manejar el caso en que no se encuentra el idUsuario en SharedPreferences
            Toast.makeText(this, "Error: No se encontró el id del usuario", Toast.LENGTH_SHORT).show();
            return;  // Salir de la función si no hay idUsuario
        }

        // Configurar las fechas (puedes ajustar estas fechas según tu lógica)
        String fechaDesde = "2024-01-01";
        String fechaHasta = "2024-12-31";

        // URL del backend
        String url = "https://utic2025.com/lucas_rojas/monedaamoneda/balanceConGrafico.php";

        // Hacer la solicitud POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        // Log para verificar la respuesta del servidor
                        Log.d("BalanceGeneral", "Respuesta del servidor: " + response);

                        // Procesar la respuesta JSON
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            JSONObject data = jsonArray.getJSONObject(0);

                            // Manejar valores nulos y convertir a double
                            double totalEgresos = data.isNull("total_egresos") ? 0.0 : data.getDouble("total_egresos");
                            double totalIngresos = data.isNull("total_ingresos") ? 0.0 : data.getDouble("total_ingresos");
                            double saldoFinal = data.isNull("saldo_final") ? 0.0 : data.getDouble("saldo_final");

                            // Mostrar el saldo final en el TextView
                            tvSaldoFinal.setText("Saldo Final: " + saldoFinal);

                            // Configurar el gráfico de barras
                            ArrayList<BarEntry> barEntries = new ArrayList<>();
                            barEntries.add(new BarEntry(1f, (float) totalIngresos));
                            barEntries.add(new BarEntry(2f, (float) totalEgresos));

                            BarDataSet barDataSet = new BarDataSet(barEntries, "Ingresos vs Egresos");
                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                            BarData barData = new BarData(barDataSet);
                            barChart.setData(barData);
                            barChart.invalidate();  // Actualizar el gráfico

                            // Log de éxito
                            Log.d("BalanceGeneral", "Datos procesados correctamente.");
                        } else {
                            Toast.makeText(balancegeneral.this, "No se encontraron datos.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // Log del error en JSON
                        Log.e("BalanceGeneral", "Error al procesar los datos JSON: " + e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(balancegeneral.this, "Error al procesar los datos.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Log del error en la conexión
                    Log.e("BalanceGeneral", "Error en la conexión: " + error.getMessage());
                    Toast.makeText(balancegeneral.this, "Error en la conexión.", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Enviar los parámetros en la solicitud POST
                Map<String, String> params = new HashMap<>();
                params.put("fechaDesde", fechaDesde);
                params.put("fechaHasta", fechaHasta);
                params.put("idUsuario", String.valueOf(idUsuario));  // Enviar el idUsuario desde SharedPreferences
                return params;
            }
        };

        // Añadir la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
