package app.savingbeasts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.savingbeasts.databinding.FragmentHomeBinding
import misClases.Ahorro
import misClases.adaptador.AhorroAdapterHome
import misClases.viewModel.AhorroViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class HomeFragment : Fragment() {

    var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!
    private val ahorroViewModel: AhorroViewModel by activityViewModels()
    private lateinit var adapter: AhorroAdapterHome
    private lateinit var items: MutableList<Ahorro>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment

        this.items = ahorroViewModel.ahorros.value ?: mutableListOf()
        var itemsFiltrados = mutableListOf<Ahorro>()
        for (i in items) {
            if (i.getRestante() > 0.0) {
                if (Math.abs(
                        ChronoUnit.DAYS.between(
                            i.getUltimoAhorro().toLocalDate(), LocalDate.now()
                        )
                    ) >= i.getPeriodo()
                ) {
                    itemsFiltrados.add(i)
                }
            }
        }
        // Inicializar RecyclerView
        val recyclerView = binding.recyclerViewHome

        recyclerView.layoutManager =
            LinearLayoutManager(this.context) // Disposición en lista vertical
        if (itemsFiltrados.size == 0) {

            binding.textViewHome.text = getString(R.string.noHayAhorrosPendientes)
        } else {
            binding.textViewHome.height = 0
            adapter = AhorroAdapterHome(this.requireContext(), itemsFiltrados)

            recyclerView.adapter = this.adapter

            ahorroViewModel.ahorros.observe(viewLifecycleOwner, Observer { ahorros ->
                // Actualiza el adaptador con la nueva lista
                adapter.updateAhorros(ahorros)
            })
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        // Cambiar el título en la Toolbar
        activity?.title = getString(R.string.home)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}