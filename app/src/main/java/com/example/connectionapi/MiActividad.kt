package com.example.connectionapi

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.connectionapi.UsuarioManager.iniciarSesion

object UsuarioManager {
    private val listaUsuarios: MutableList<Usuario> = mutableListOf()

    fun registrarUsuario(usuario: Usuario) {
        listaUsuarios.add(usuario)
    }

    fun obtenerUsuario(correo: String): Usuario? {
        return listaUsuarios.find { it.correo == correo }
    }

    fun existeUsuario(correo: String): Boolean {
        return obtenerUsuario(correo) != null
    }

    fun iniciarSesion(correo: String, contraseña: String): Boolean {
        val usuario = obtenerUsuario(correo)
        return if (correo == "admin" && contraseña == "admin") {
            // Credenciales de administrador
            true
        } else {
            usuario?.contraseña == contraseña
        }
    }
}


class MiActividad : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Llama al método para mostrar el diálogo de inicio de sesión
        mostrarDialogoInicioSesion()
    }

    fun mostrarDialogoInicioSesion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Inicio de sesión")

        // Inflar el layout del diálogo
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_login, null)
        builder.setView(dialogLayout)

        val editTextCorreo = dialogLayout.findViewById<EditText>(R.id.editTextCorreo)
        val editTextContraseña = dialogLayout.findViewById<EditText>(R.id.editTextContraseña)

        builder.setPositiveButton("Iniciar sesión") { dialog, which ->
            val correo = editTextCorreo.text.toString()
            val contraseña = editTextContraseña.text.toString()

            // Verifica si las credenciales son del administrador
            if (iniciarSesionAdmin(correo, contraseña)) {
                // Iniciar sesión como administrador
                val preferencias = getSharedPreferences("preferencias", Context.MODE_PRIVATE)
                val editor = preferencias.edit()
                editor.putBoolean("usuario_autenticado", true)
                editor.apply()
                Toast.makeText(this, "Inicio de sesión como administrador exitoso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                // Realiza las acciones de administrador aquí
            } else {
                // Verifica las credenciales del usuario normal
                val exitoso = UsuarioManager.iniciarSesion(correo, contraseña)
                if (exitoso) {
                    // Inicio de sesión exitoso, inicia MainActivity y finaliza MiActividad
                    val preferencias = getSharedPreferences("preferencias", Context.MODE_PRIVATE)
                    val editor = preferencias.edit()
                    editor.putBoolean("usuario_autenticado", true)
                    editor.apply()
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // Las credenciales son incorrectas, muestra un mensaje de error
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun iniciarSesionAdmin(correo: String, contraseña: String): Boolean {
        return correo == "admin" && contraseña == "admin"
    }
}



