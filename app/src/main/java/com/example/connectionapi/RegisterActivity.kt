package com.example.connectionapi

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)

        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        buttonRegister.setOnClickListener {
            val editTextCedula = findViewById<EditText>(R.id.editTextCedula)
            val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
            val editTextApellidos = findViewById<EditText>(R.id.editTextApellidos)
            // Obtener referencias a otros EditText para los demás campos de registro
            // ...

            val cedula = editTextCedula.text.toString()
            val nombre = editTextNombre.text.toString()
            val apellidos = editTextApellidos.text.toString()
            // Obtener los valores de los demás campos de registro
            // ...

            if (cedula.isNotEmpty() && nombre.isNotEmpty() && apellidos.isNotEmpty()) {
                // Aquí puedes realizar validaciones adicionales si es necesario

                // Si todos los campos están completos, puedes proceder con el registro
                // Guarda los datos del usuario en una base de datos, SharedPreferences, etc.

                // Muestra un mensaje de registro exitoso
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()

                // Puedes redirigir al usuario a la pantalla de inicio de sesión u otra actividad
                // Por ejemplo:
                // val intent = Intent(this, LoginActivity::class.java)
                // startActivity(intent)

                // O puedes simplemente finalizar esta actividad
                finish()
            } else {
                // Muestra un mensaje de error indicando que todos los campos son obligatorios
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
