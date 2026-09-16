package com.example.rutasvivas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rutasvivas.datos.Destino
import java.io.File

class DestinoAdapter(
    private val items: MutableList<Destino>,
    private val onClick: (Destino) -> Unit,
    private val onLongClick: (Destino) -> Unit
) : RecyclerView.Adapter<DestinoAdapter.DestinoViewHolder>() {

    inner class DestinoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivDestino: ImageView = itemView.findViewById(R.id.ivDestino)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destino, parent, false)
        return DestinoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinoViewHolder, position: Int) {
        val destino = items[position]
        holder.tvNombre.text = destino.nombre
        holder.tvPrecio.text = "$${destino.precio} - ${destino.pais}"
        holder.tvDescripcion.text = destino.descripcion

        Glide.with(holder.itemView.context)
            .load(File(destino.imagenPath))
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.ivDestino)

        holder.itemView.setOnClickListener { onClick(destino) }
        holder.itemView.setOnLongClickListener { onLongClick(destino); true }
    }

    override fun getItemCount(): Int = items.size

    fun actualizarLista(nuevaLista: List<Destino>) {
        items.clear()
        items.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}