package com.example.rutasvivas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.example.rutasvivas.datos.Destino

class CatalogoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvVacio: TextView
    private lateinit var adapter: DestinoAdapter
    private val listaDestinos = mutableListOf<Destino>()

    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refDestinos: DatabaseReference = database.getReference("destinos")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        recyclerView = findViewById(R.id.recyclerView)
        tvVacio = findViewById(R.id.tvVacio)
        val fabAgregar = findViewById<FloatingActionButton>(R.id.fabAgregar)

        adapter = DestinoAdapter(
            listaDestinos,
            onClick = { destino -> abrirEditar(destino) },
            onLongClick = { destino -> confirmarEliminar(destino) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fabAgregar.setOnClickListener {
            startActivity(Intent(this, AddEditDestinoActivity::class.java))
        }

        escucharCambios()
    }

    private fun escucharCambios() {
        refDestinos.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaDestinos.clear()
                for (hijo in snapshot.children) {
                    try {
                        val nombre = hijo.child("nombre").getValue(String::class.java) ?: ""
                        val pais = hijo.child("pais").getValue(String::class.java) ?: ""
                        val descripcion = hijo.child("descripcion").getValue(String::class.java) ?: ""
                        val imagenPath = hijo.child("imagenPath").getValue(String::class.java) ?: ""
                        val precio = hijo.child("precio").getValue(Double::class.java)
                            ?: hijo.child("precio").getValue(String::class.java)?.toDoubleOrNull()
                            ?: 0.0

                        val destino = Destino(
                            key = hijo.key ?: "",
                            nombre = nombre,
                            pais = pais,
                            precio = precio,
                            descripcion = descripcion,
                            imagenPath = imagenPath
                        )
                        listaDestinos.add(destino)
                    } catch (e: Exception) {
                        Toast.makeText(this@CatalogoActivity, "Error leyendo: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
                Toast.makeText(this@CatalogoActivity, "Lista final tiene: ${listaDestinos.size} items", Toast.LENGTH_LONG).show()
                adapter.actualizarLista(listaDestinos)
                tvVacio.visibility = if (listaDestinos.isEmpty()) View.VISIBLE else View.GONE
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun abrirEditar(destino: Destino) {
        val intent = Intent(this, AddEditDestinoActivity::class.java)
        intent.putExtra("key", destino.key)
        intent.putExtra("nombre", destino.nombre)
        intent.putExtra("pais", destino.pais)
        intent.putExtra("precio", destino.precio)
        intent.putExtra("descripcion", destino.descripcion)
        intent.putExtra("imagenPath", destino.imagenPath)
        startActivity(intent)
    }

    private fun confirmarEliminar(destino: Destino) {
        AlertDialog.Builder(this)
            .setTitle(R.string.confirmacion_eliminar_titulo)
            .setMessage(R.string.confirmacion_eliminar_mensaje)
            .setPositiveButton("Sí") { _, _ ->
                refDestinos.child(destino.key).removeValue()
            }
            .setNegativeButton("No", null)
            .show()
    }
}