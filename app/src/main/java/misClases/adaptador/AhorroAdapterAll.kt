package misClases.adaptador


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.savingbeasts.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import misClases.Ahorro
import java.text.DecimalFormat

// Adaptador de RecyclerView para la clase Ahorro
class AhorroAdapterAll(
    private val context: Context,
    private var listaAhorros: MutableList<Ahorro>,
    private val onItemClick: (List<Ahorro>,Int) -> Unit // Callback para manejar clics en los elementos
) : RecyclerView.Adapter<AhorroAdapterAll.AhorroViewHolder>() {

    // ViewHolder que mantiene las referencias de las vistas para cada elemento
    class AhorroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.titulo_All)
        val cantidadActual: TextView = itemView.findViewById(R.id.cantidadActual_All)
        val cantidadRestante: TextView = itemView.findViewById(R.id.cantidadRestante_All)
        val cantidadFinal: TextView = itemView.findViewById(R.id.cantidadFinal_All)
        val fecha: TextView = itemView.findViewById(R.id.tiempo_All)
        val imagen: ImageView = itemView.findViewById(R.id.imagen_All)
        val diasRestantes: TextView = itemView.findViewById(R.id.diasRestantes_All)
        val pieChart: PieChart = itemView.findViewById(R.id.pieChart)
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
            context.getString(R.string.actualAll) + " " + formatter.format(ahorro.getCantidadActual()) + "€"


        holder.cantidadActual.text = cantidadActual

        val cantidadRestante =
            context.getString(R.string.restanteAll) + " " + formatter.format(ahorro.getRestante()) + "€"
        holder.cantidadRestante.text = cantidadRestante

        val cantidadTotal =
            context.getString(R.string.finalAll) + " " + formatter.format(ahorro.getCantidad()) + "€"
        holder.cantidadFinal.text = cantidadTotal

        holder.fecha.text = ahorro.getFecha().toString()

        val diasRestantes =
            context.getString(R.string.diasRestantesAll) + " " + ahorro.getDiasRestantes()
                .toString()
        holder.diasRestantes.text = diasRestantes

        // Carga la imagen (por ejemplo, desde un archivo local)
        holder.imagen.setImageURI(android.net.Uri.parse(ahorro.getImagen().toString()))

        //Grafico de pastel
        val pieEntries = listOf(
            PieEntry(ahorro.getCantidadActual().toFloat(), ""),
            PieEntry(ahorro.getRestante().toFloat(), "")
        )
        val dataSet = PieDataSet(pieEntries, "")
        dataSet.colors = listOf(
            Color.GREEN, // Color para la parte alcanzada
            Color.GRAY  // Color para la parte faltante
        )
        dataSet.valueTextColor = Color.TRANSPARENT
        dataSet.valueTextSize = 0f
        val pieData = PieData(dataSet)
        holder.pieChart.data = pieData

        // Configuración visual del gráfico
        holder.pieChart.description.isEnabled = false // Oculta la descripción
        holder.pieChart.isDrawHoleEnabled = true
        holder.pieChart.setHoleColor(Color.TRANSPARENT)
        holder.pieChart.centerText =
            "${(ahorro.getCantidadActual() / ahorro.getCantidad() * 100).toInt()}%" // Muestra solo el porcentaje
        holder.pieChart.setCenterTextColor(Color.BLACK)
        holder.pieChart.legend.isEnabled = false // Oculta la leyenda
        holder.pieChart.setEntryLabelColor(Color.TRANSPARENT) // Oculta las etiquetas de las entradas
        holder.pieChart.animateY(1000)

        // Maneja el clic en el elemento
        holder.itemView.setOnClickListener {
            onItemClick(listaAhorros,position)
        }
    }

    // Devuelve el tamaño de la lista
    override fun getItemCount(): Int = listaAhorros.size
    fun updateAhorros(ahorros: MutableList<Ahorro>?) {

        if (ahorros != null) {
            this.listaAhorros = ahorros.toMutableList()
        } // Reemplaza la lista antigua con la nueva
        notifyDataSetChanged() // Notifica al adaptador que la lista ha cambiado

    }
}
