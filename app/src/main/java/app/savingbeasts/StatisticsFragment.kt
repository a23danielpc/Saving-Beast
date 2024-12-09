package app.savingbeasts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import app.savingbeasts.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import misClases.Ahorro
import misClases.viewModel.AhorroViewModel


class StatisticsFragment : Fragment() {
    var _binding: FragmentStatisticsBinding? = null
    val binding get() = _binding!!
    private val ahorroViewModel: AhorroViewModel by activityViewModels()
    private lateinit var items: MutableList<Ahorro>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment

        items = ahorroViewModel.ahorros.value ?: mutableListOf()
        var cantidadRestanteTotal = 0.0
        var cantidadTotalAhorrada = 0.0
        var cantidadTotalTerminada = 0.0
        for (i in items) {
            if (i.getRestante() == 0.0) {
                cantidadTotalTerminada += i.getCantidad()
            } else {
                cantidadRestanteTotal += i.getRestante()
                cantidadTotalAhorrada += i.getCantidadActual()
            }
        }
        binding.dineroTotalTerminadoCalc.text =
            "" + cantidadTotalTerminada + this.requireContext().getString(R.string.euro)
        binding.dineroAhorradoCalc.text =
            "" + cantidadTotalAhorrada + this.requireContext().getString(R.string.euro)
        binding.dineroRestanteCalc.text =
            "" + cantidadRestanteTotal + this.requireContext().getString(R.string.euro)


        val pieChart = binding.pieChartStatistics

        val pieEntries = mutableListOf<PieEntry>()

        if (cantidadTotalTerminada == 0.0 || cantidadTotalAhorrada == 0.0 || cantidadRestanteTotal == 0.0) {
            pieEntries.add(PieEntry(1f, getString(R.string.noData)))
        } else {
            pieEntries.add(PieEntry(cantidadTotalTerminada.toFloat(), getString(R.string.finished)))
            pieEntries.add(PieEntry(cantidadTotalAhorrada.toFloat(), getString(R.string.saved)))
            pieEntries.add(PieEntry(cantidadRestanteTotal.toFloat(), getString(R.string.remaining)))
        }

        val dataSet = PieDataSet(pieEntries, "")
        dataSet.colors = listOf(
            Color.CYAN, // Color para la parte alcanzada
            Color.GREEN,  // Color para la parte faltante
            Color.GRAY    // Color para la parte restante
        )
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 20f

        val pieData = PieData(dataSet)
        pieChart.data = pieData

        // Configuración visual del gráfico
        pieChart.description.isEnabled = false // Oculta la descripción
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.setCenterTextColor(Color.BLACK)
        pieChart.legend.isEnabled = true // Oculta la leyenda
        pieChart.setEntryLabelColor(Color.BLACK) // Oculta las etiquetas de las entradas
        pieChart.animateY(1000)
        pieChart.invalidate()

        return view
    }

    override fun onResume() {
        super.onResume()
        // Cambiar el título en la Toolbar
        activity?.title = getString(R.string.statistics)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}