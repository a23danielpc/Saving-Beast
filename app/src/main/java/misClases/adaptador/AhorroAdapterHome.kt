package misClases.adaptador


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.savingbeasts.R
import misClases.Ahorro
import misClases.Fecha
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit

// Adaptador de RecyclerView para la clase Ahorro
class AhorroAdapterHome(
    private val context: Context, private var listaAhorros: MutableList<Ahorro>
) : RecyclerView.Adapter<AhorroAdapterHome.AhorroViewHolder>() {

    // ViewHolder que mantiene las referencias de las vistas para cada elemento
    class AhorroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.titulo_Home)
        val cantidadRestante: TextView = itemView.findViewById(R.id.cantidad_Restante_Home)
        val cantidadFinal: TextView = itemView.findViewById(R.id.cantidad_Home)
        val fecha: TextView = itemView.findViewById(R.id.tiempo_Home)
        val imagen: ImageView = itemView.findViewById(R.id.imagen_Home)
        val boton: Button = itemView.findViewById(R.id.itemButton)
    }

    // Infla el diseño para cada elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AhorroViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_item_view, parent, false)
        return AhorroViewHolder(view)
    }

    // Vincula los datos del objeto Ahorro a las vistas
    override fun onBindViewHolder(holder: AhorroViewHolder, position: Int) {

        val ahorro = listaAhorros[position]

        // Formatear cantidades a dos decimales
        val formatter = DecimalFormat("0.00")


        holder.nombre.text = ahorro.getNombre()


        val cantidadActual =
            context.getString(R.string.actualAll) + " " + formatter.format(ahorro.getCantidadActual()) + this.context.getString(
                R.string.euro
            )
        holder.cantidadRestante.text = cantidadActual

        val cantidadRestante =
            context.getString(R.string.restanteAll) + " " + formatter.format(ahorro.getRestante()) + this.context.getString(
                R.string.euro
            )
        holder.cantidadRestante.text = cantidadRestante

        val cantidadTotal =
            context.getString(R.string.finalAll) + " " + formatter.format(ahorro.getCantidad()) + this.context.getString(
                R.string.euro
            )
        holder.cantidadFinal.text = cantidadTotal

        holder.fecha.text = ahorro.getFecha().toString()

        val diasRestantes =
            context.getString(R.string.diasRestantesAll) + " " + ahorro.getDiasRestantes()
                .toString()


        holder.boton.setOnClickListener {
            ahorrarDia(listaAhorros, position)
            Toast.makeText(context, this.context.getString(R.string.ahorroRealizado), Toast.LENGTH_SHORT).show()
        }

        // Carga la imagen (por ejemplo, desde un archivo local)
        holder.imagen.setImageURI(android.net.Uri.parse(ahorro.getImagen().toString()))
    }


    // Devuelve el tamaño de la lista
    override fun getItemCount(): Int = listaAhorros.size

    fun updateAhorros(ahorros: MutableList<Ahorro>?) {
        if (ahorros != null) {
            this.listaAhorros = ahorros.toMutableList()
        } // Reemplaza la lista antigua con la nueva
        notifyDataSetChanged() // Notifica al adaptador que la lista ha cambiado

    }

    fun ahorrarDia(listaAhorros: MutableList<Ahorro>, position: Int) {
        val ahorro = listaAhorros[position]
        listaAhorros[position].setCantidadActual(ahorro.getCantidadActual() + ahorro.getAhorroDiario())
        listaAhorros[position].setUltimoAhorro(Fecha(LocalDate.now()))
        if (listaAhorros[position].getRestante() <= 0) {
            listaAhorros[position].setCantidad(listaAhorros[position].getCantidad())
            listaAhorros[position].setCantidadActual(listaAhorros[position].getCantidad())
        }

        notifyDataSetChanged()
    }
}
