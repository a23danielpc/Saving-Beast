package app.savingbeasts

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.util.copy
import app.savingbeasts.databinding.FragmentAllBinding
import clasesModelo.Ahorro
import clasesModelo.Fecha
import clasesModelo.Periodo
import clasesModelo.adaptador.AhorroAdapter
import clasesModelo.viewModel.AhorroViewModel
import java.time.LocalDate

class AllFragment : Fragment() {
    var _binding: FragmentAllBinding? = null
    val binding get() = _binding!!
    private val ahorroViewModel: AhorroViewModel by activityViewModels()
    private lateinit var adapter: AhorroAdapter
    companion object {
        private const val GALLERY_REQUEST_CODE = 1001
    }
   var items = ahorroViewModel.ahorros.value ?: mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment


        // Inicializar RecyclerView
        val recyclerView = binding.recyclerViewAll

        recyclerView.layoutManager =
            LinearLayoutManager(this.context) // Disposición en lista vertical
        adapter = AhorroAdapter(this.requireContext(), items) { items, position ->
            //Cosa al hacer click
            showOptionsDialog(this.items, position) // Llamamos a la función para mostrar el diálogo
        }

        recyclerView.adapter = this.adapter

        ahorroViewModel.ahorros.observe(viewLifecycleOwner, Observer { ahorros ->
            // Actualiza el adaptador con la nueva lista
            adapter.updateAhorros(ahorros)
        })

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
                0 -> editItem(this.items, position) // Llamamos a la función de editar
                1 -> {
                    // Llamamos a la función de borrar
                    deleteItem(position)
                }
            }
        }.show()
    }

    private fun editItem(items: MutableList<Ahorro>, position: Int) {
        // Código para editar un elemento
        showEditDialog(position)
    }

    private fun deleteItem(position: Int) {
        ahorroViewModel.deleteAhorro(position)
    }

    private fun showEditDialog(position: Int) {
        // Obtener el objeto actual
        val ahorro = ahorroViewModel.ahorros.value?.get(position)

        // Crear el diseño del diálogo
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit, null)
        val editNombre = dialogView.findViewById<EditText>(R.id.editNombre)
        val editFinal = dialogView.findViewById<EditText>(R.id.editCantidadFinal)
        val editActual = dialogView.findViewById<EditText>(R.id.editCantidadActual)
        val editFechaDia = dialogView.findViewById<EditText>(R.id.editFechaDia)
        val editFechaMes = dialogView.findViewById<EditText>(R.id.editFechaMes)
        val editFechaAno = dialogView.findViewById<EditText>(R.id.editFechaAno)
        val editPeriodo = dialogView.findViewById<EditText>(R.id.editPeriodo)


        val btnSelectImage = dialogView.findViewById<Button>(R.id.editarFoto)
        btnSelectImage.setOnClickListener {
            openGallery() // Abre la galería al pulsar el botón
        }

        // Llenar los campos con los valores actuales
        editNombre.setText(ahorro?.getNombre())
        editFinal.setText(ahorro?.getCantidad()?.toString())
        editActual.setText(ahorro?.getCantidadActual()?.toString())

        AlertDialog.Builder(requireContext()).setTitle(getString(R.string.editar_ahorro))
            .setView(dialogView).setPositiveButton(getString(R.string.guardar)) { _, _ ->
                // Guardar los cambios
                val nuevoAhorro = ahorro?.copy(
                    nombre = editNombre.text.toString() ?: ahorro.getNombre(),
                    cantidad = editFinal.text.toString().toDoubleOrNull() ?: ahorro.getCantidad(),
                    cantidadActual = editActual.text.toString().toDoubleOrNull() ?: ahorro.getCantidadActual(),
                    fecha = Fecha(
                        editFechaDia.text.toString().toIntOrNull() ?: ahorro.getFecha().getDia(),
                        editFechaMes.text.toString().toIntOrNull() ?: ahorro.getFecha().getMes(),
                        editFechaAno.text.toString().toIntOrNull() ?: ahorro.getFecha().getAnio()
                    ),
                    ultimoAhorro = ahorro.getUltimoAhorro(),
                    imagen = ahorro.getImagen(),
                    periodo = Periodo(editPeriodo.text.toString())
                )
                if (nuevoAhorro != null) {
                    ahorroViewModel.updateAhorro(position, nuevoAhorro)
                }
            }.setNegativeButton(getString(R.string.cancelar), null).show()
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK) // Acción para seleccionar contenido
        intent.type = "image/*" // Filtra solo imágenes
        startActivityForResult(intent, GALLERY_REQUEST_CODE) // Inicia la actividad para recibir un resultado
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar memory leaks
    }
}