package app.savingbeasts

import android.app.AlertDialog
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
        val ahorro3 = Ahorro(
            "Isaac",
            120.0,
            0.0,
            Fecha(1, 4, 2025),
            Fecha(LocalDate.now()),
            R.drawable.isaac,
            Periodo("Diario", 1)
        )
        val ahorro4 = Ahorro(
            "La PC",
            1000.0,
            23.45,
            Fecha(20, 2, 2025),
            Fecha(LocalDate.now()),
            R.drawable.pc,
            Periodo("Semanal", 7)
        )
        // Inicializar RecyclerView
        val items = listOf(ahorro1, ahorro2, ahorro3, ahorro4)

        val recyclerView = binding.recyclerViewAll
        recyclerView.layoutManager =
            LinearLayoutManager(this.context) // Disposición en lista vertical
        recyclerView.adapter = AhorroAdapter(this.requireContext(), items) { items, position ->
            //Cosa al hacer click
            showOptionsDialog(items, position) // Llamamos a la función para mostrar el diálogo
        }


        return view
    }

    private fun showOptionsDialog(items: List<Ahorro>, position: Int) {
        val options = arrayOf(
            this.requireContext().getString(R.string.editar),
            this.requireContext().getString(R.string.borrar)
        )
        val title = this.requireContext()
            .getString(R.string.itemElegido) + " " + items[position].getNombre()
        AlertDialog.Builder(this.context).setTitle(title).setItems(options) { _, which ->
            when (which) {
                0 -> editItem(items, position) // Llamamos a la función de editar
                1 -> deleteItem(items, position)    // Llamamos a la función de borrar
            }
        }.show()
    }

    private fun editItem(items: List<Ahorro>, position: Int) {
        // Código para editar un elemento
    }

    private fun deleteItem(items: List<Ahorro>, position: Int) {
        // Código para borrar un elemento
        items.drop(position)
    }
}