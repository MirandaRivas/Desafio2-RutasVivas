package com.example.rutasvivas

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.rutasvivas.datos.Destino
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class AddEditDestinoActivity : AppCompatActivity() {

    private lateinit var ivPreview: ImageView
    private lateinit var etNombre: EditText
    private lateinit var spinnerPais: Spinner
    private lateinit var etPrecio: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var tvError: TextView
    private lateinit var btnEliminar: Button

    private var keyExistente: String = ""
    private var rutaImagenSeleccionada: String = ""

    // Selector de imagen moderno: NO necesita pedir permisos (Foto Picker del sistema)
    private val seleccionarImagenLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val rutaGuardada = copiarImagenAlAlmacenamientoInterno(uri)
                if (rutaGuardada != null) {
                    rutaImagenSeleccionada = rutaGuardada
                    ivPreview.setImageURI(Uri.fromFile(File(rutaGuardada)))
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_destino)

        ivPreview = findViewById(R.id.ivPreview)
        etNombre = findViewById(R.id.etNombre)
        spinnerPais = findViewById(R.id.spinnerPais)
        etPrecio = findViewById(R.id.etPrecio)
        etDescripcion = findViewById(R.id.etDescripcion)
        tvError = findViewById(R.id.tvError)
        btnEliminar = findViewById(R.id.btnEliminar)

        val btnSeleccionarImagen = findViewById<Button>(R.id.btnSeleccionarImagen)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        cargarDatosSiEsEdicion()

        btnSeleccionarImagen.setOnClickListener {
            seleccionarImagenLauncher.launch("image/*")
        }

        btnGuardar.setOnClickListener { validarYGuardar() }
        btnCancelar.setOnClickListener { finish() }

        btnEliminar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.confirmacion_eliminar_titulo)
                .setMessage(R.string.confirmacion_eliminar_mensaje)
                .setPositiveButton("Sí") { _, _ ->
                    CatalogoActivity.refDestinos.child(keyExistente).removeValue()
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    /** Si venimos desde "editar", llenamos los campos con lo que ya existía */
    private fun cargarDatosSiEsEdicion() {
        val key = intent.getStringExtra("key")
        if (!key.isNullOrEmpty()) {
            keyExistente = key
            etNombre.setText(intent.getStringExtra("nombre"))
            etPrecio.setText(intent.getDoubleExtra("precio", 0.0).toString())
            etDescripcion.setText(intent.getStringExtra("descripcion"))
            rutaImagenSeleccionada = intent.getStringExtra("imagenPath") ?: ""

            val pais = intent.getStringExtra("pais") ?: ""
            val adapter = spinnerPais.adapter
            for (i in 0 until adapter.count) {
                if (adapter.getItem(i).toString() == pais) {
                    spinnerPais.setSelection(i)
                    break
                }
            }

            if (rutaImagenSeleccionada.isNotEmpty()) {
                ivPreview.setImageURI(Uri.fromFile(File(rutaImagenSeleccionada)))
            }
            btnEliminar.visibility = android.view.View.VISIBLE
        }
    }

    /** Copia la imagen elegida de la galería a la carpeta interna de la app */
    private fun copiarImagenAlAlmacenamientoInterno(uriOrigen: Uri): String? {
        return try {
            val nombreArchivo = "destino_${UUID.randomUUID()}.jpg"
            val archivoDestino = File(filesDir, nombreArchivo)
            contentResolver.openInputStream(uriOrigen)?.use { input ->
                FileOutputStream(archivoDestino).use { output ->
                    input.copyTo(output)
                }
            }
            archivoDestino.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    /** Aquí van las 4 validaciones obligatorias del desafío */
    private fun validarYGuardar() {
        tvError.text = ""

        val nombre = etNombre.text.toString().trim()
        val pais = spinnerPais.selectedItem?.toString() ?: ""
        val precioTexto = etPrecio.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()

        // 1) Campos nulos o vacíos
        if (nombre.isEmpty() || pais.isEmpty() || precioTexto.isEmpty() || descripcion.isEmpty()) {
            tvError.text = getString(R.string.error_campo_vacio)
            return
        }

        // 2) Precio numérico y mayor a 0
        val precio = precioTexto.toDoubleOrNull()
        if (precio == null || precio <= 0.0) {
            tvError.text = getString(R.string.error_precio_invalido)
            return
        }

        // 3) Descripción mínimo 20 caracteres
        if (descripcion.length < 20) {
            tvError.text = getString(R.string.error_descripcion_corta)
            return
        }

        // 4) Imagen obligatoria
        if (rutaImagenSeleccionada.isEmpty()) {
            tvError.text = getString(R.string.error_imagen_requerida)
            return
        }

        val destino = Destino(
            key = keyExistente,
            nombre = nombre,
            pais = pais,
            precio = precio,
            descripcion = descripcion,
            imagenPath = rutaImagenSeleccionada
        )

        if (keyExistente.isEmpty()) {
            // Crear (Create)
            val nuevaRef = CatalogoActivity.refDestinos.push()
            destino.key = nuevaRef.key ?: ""
            nuevaRef.setValue(destino.toMap())
        } else {
            // Actualizar (Update)
            CatalogoActivity.refDestinos.child(keyExistente).setValue(destino.toMap())
        }

        Toast.makeText(this, "Destino guardado con éxito", Toast.LENGTH_SHORT).show()
        finish()
    }
}