package com.example.connectionapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.connectionapi.ui.theme.ConnectionAPITheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConnectionAPITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llama al API y maneja la respuesta
                    CallApi(ApiConfig.apiService.getMensaje())

                }
            }
        }
    }
}

@Composable
fun CallApi(call: Call<String>) {
    var mensaje by remember { mutableStateOf("Cargando...") }

    LaunchedEffect(Unit) {
        try {
            val response = withContext(Dispatchers.IO) {
                call.execute()
            }
            if (response.isSuccessful) {
                mensaje = response.body() ?: "Empty response"
            } else {
                mensaje = "Error1: ${response.message()}"
            }
        } catch (e: Exception) {
            mensaje = "Error2: ${e.message}"
        }
    }

    Text(
        text = mensaje,
        modifier = Modifier.fillMaxSize()
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ConnectionAPITheme {
        CallApi(ApiConfig.apiService.getMensaje())
    }
}

