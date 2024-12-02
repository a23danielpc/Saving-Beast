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
        val cantidadActual: TextView = itemView.findViewById(R.id.tvCantidadActual)
        val restante: TextView = itemView.findViewById(R.id.tvRestante)
        val imagen: ImageView = itemView.findViewById(R.id.ivImagenAhorro)
    }

    // Infla el diseño para cada elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AhorroViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ahorro, parent, false)
        return AhorroViewHolder(view)
    }

    // Vincula los datos del objeto Ahorro a las vistas
    override fun onBindViewHolder(holder: AhorroViewHolder, position: Int) {
        val ahorro = listaAhorros[position]

        // Formatear cantidades a dos decimales
        val formatter = DecimalFormat("#,###.00")

        holder.nombre.text = ahorro.getNombre()
        holder.cantidadActual.text = "Actual: ${formatter.format(ahorro.getCantidadActual())}"
        holder.restante.text = "Restante: ${formatter.format(ahorro.getRestante())}"

        // Carga la imagen (por ejemplo, desde un archivo local)
        holder.imagen.setImageURI(ahorro.getImagen().toURI())

        // Maneja el clic en el elemento
        holder.itemView.setOnClickListener {
            onItemClick(ahorro)
        }
    }

    // Devuelve el tamaño de la lista
    override fun getItemCount(): Int = listaAhorros.size
}
