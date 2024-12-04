package app.savingbeasts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.savingbeasts.databinding.FragmentAllBinding
import clasesModelo.Ahorro
import clasesModelo.Fecha
import clasesModelo.Periodo
import clasesModelo.adaptador.AhorroAdapter
import java.time.LocalDate

class AllFragment : Fragment() {
    var _binding: FragmentAllBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment

        val ahorro1 = Ahorro(
            "Bicicleta",
            300.0,
            0.0,
            Fecha(1, 1, 2025),
            Fecha(LocalDate.now()),
            R.drawable.bicicleta,
            Periodo("Diario", 1)
        )
        val ahorro2 = Ahorro(
            "Coche",
            5000.0,
            1500.0,
            Fecha(12, 9, 2026),
            Fecha(LocalDate.now()),
            R.drawable.coche,
            Periodo("Diario", 1)
        )
        val ahorro3=Ahorro(
            "Isaac",
            120.0,
            0.0,
            Fecha(1, 4, 2025),
            Fecha(LocalDate.now()),
            R.drawable.isaac,
            Periodo("Diario", 1)
        )
        val ahorro4=Ahorro(
            "La PC",
            1000.0,
            23.45,
            Fecha(20, 2, 2025),
            Fecha(LocalDate.now()),
            R.drawable.pc,
            Periodo("Semanal", 7)
        )
        // Inicializar RecyclerView
        val items = listOf(ahorro1,ahorro2,ahorro3,ahorro4)

        val recyclerView = binding.recyclerViewAll
        recyclerView.layoutManager =
            LinearLayoutManager(this.context) // Disposición en lista vertical
        recyclerView.adapter = AhorroAdapter(this.requireContext(), items) {
            // Manejar clic en el elemento
        }


        return view
    }

}