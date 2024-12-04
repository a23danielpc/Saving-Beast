package clasesModelo.adaptador


import clasesModelo.Ahorro
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.savingbeasts.R
import java.text.DecimalFormat

// Adaptador de RecyclerView para la clase Ahorro
class AhorroAdapter(
    private val context: Context,
    private val listaAhorros: List<Ahorro>,
    private val onItemClick: (Ahorro) -> Unit // Callback para manejar clics en los elementos
) : RecyclerView.Adapter<AhorroAdapter.AhorroViewHolder>() {

    // ViewHolder que mantiene las referencias de las vistas para cada elemento
    class AhorroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.titulo_All)
        val cantidadActual: TextView = itemView.findViewById(R.id.cantidadActual_All)
        val cantidadRestante: TextView = itemView.findViewById(R.id.cantidadRestante_All)
        val cantidadFinal: TextView = itemView.findViewById(R.id.cantidadFinal_All)
        val fecha: TextView = itemView.findViewById(R.id.tiempo_All)
        val imagen: ImageView = itemView.findViewById(R.id.imagen_All)
        val diasRestantes: TextView = itemView.findViewById(R.id.diasRestantes_All)
    }

    // Infla el diseño para cada elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AhorroViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.all_item_view, parent, false)
        return AhorroViewHolder(view)
    }

    // Vincula los datos del objeto Ahorro a las vistas
    override fun onBindViewHolder(holder: AhorroViewHolder, position: Int) {
        val ahorro = listaAhorros[position]

        // Formatear cantidades a dos decimales
        val formatter = DecimalFormat("0.00")

        holder.nombre.text = ahorro.getNombre()

        val cantidadActual =
            context.getString(R.string.actualAll) + " " + formatter.format(ahorro.getCantidadActual())+"€"


        holder.cantidadActual.text = cantidadActual

        val cantidadRestante =
            context.getString(R.string.restanteAll) + " " + formatter.format(ahorro.getRestante())+"€"
        holder.cantidadRestante.text = cantidadRestante

        val cantidadTotal =
            context.getString(R.string.finalAll) + " " + formatter.format(ahorro.getCantidad())+"€"
        holder.cantidadFinal.text = cantidadTotal

        holder.fecha.text = ahorro.getFecha().toString()

        val diasRestantes= context.getString(R.string.diasRestantesAll) + " " + ahorro.getDiasRestantes().toString()
        holder.diasRestantes.text = diasRestantes

        // Carga la imagen (por ejemplo, desde un archivo local)
        holder.imagen.setImageResource(ahorro.getImagen())

        // Maneja el clic en el elemento
        holder.itemView.setOnClickListener {
            onItemClick(ahorro)
        }
    }

    // Devuelve el tamaño de la lista
    override fun getItemCount(): Int = listaAhorros.size
}
