package app.savingbeasts

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.savingbeasts.databinding.FragmentAllBinding
import misClases.Ahorro
import misClases.Fecha
import misClases.adaptador.AhorroAdapterAll
import misClases.viewModel.AhorroViewModel

class AllFragment : Fragment() {
    var _binding: FragmentAllBinding? = null
    val binding get() = _binding!!
    private val ahorroViewModel: AhorroViewModel by activityViewModels()
    private lateinit var adapter: AhorroAdapterAll
    private lateinit var items: MutableList<Ahorro>

    // Registrar el lanzador de actividad para seleccionar una imagen
    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                // Guardar la URI seleccionada para actualizar la imagen en el objeto
                selectedImageUri = uri
            }
        }

    // Variable para almacenar la URI seleccionada
    private var selectedImageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllBinding.inflate(inflater, container, false)
        val view = binding.root

        this.items = ahorroViewModel.ahorros.value ?: mutableListOf()

        // Inicializar RecyclerView
        val recyclerView = binding.recyclerViewAll

        recyclerView.layoutManager =
            LinearLayoutManager(this.context) // Disposición en lista vertical
        if (items.size == 0) {
            binding.textViewAll.text = getString(R.string.noHayAhorros)
        } else {
            binding.textViewAll.height = 0
            adapter = AhorroAdapterAll(this.requireContext(), items) { items, position ->
                //Cosa al hacer click
                showOptionsDialog(
                    this.items, position
                ) // Llamamos a la función para mostrar el diálogo
            }

            recyclerView.adapter = this.adapter

            ahorroViewModel.ahorros.observe(viewLifecycleOwner, Observer { ahorros ->
                // Actualiza el adaptador con la nueva lista
                adapter.updateAhorros(ahorros)
            })
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
                0 -> {
                    editItem(this.items, position)
                } // Llamamos a la función de editar
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
        Toast.makeText(
            context, this.requireContext().getString(R.string.ahorroEditado), Toast.LENGTH_SHORT
        ).show()
    }

    private fun deleteItem(position: Int) {
        ahorroViewModel.deleteAhorro(position)
        Toast.makeText(
            context, this.requireContext().getString(R.string.ahorroEliminado), Toast.LENGTH_SHORT
        ).show()
    }

    private fun showEditDialog(position: Int) {
        // Obtener el objeto actual
        val ahorro = ahorroViewModel.ahorros.value?.get(position)

        // Crear el diseño del diálogo
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit, null)
        val editNombre = dialogView.findViewById<EditText>(R.id.editNombre)
        val editFinal = dialogView.findViewById<EditText>(R.id.editCantidadFinal)
        val editActual = dialogView.findViewById<EditText>(R.id.editCantidadActual)
        val editFechaDia = dialogView.findViewById<EditText>(R.id.editFechaDia)
        val editFechaMes = dialogView.findViewById<EditText>(R.id.editFechaMes)
        val editFechaAno = dialogView.findViewById<EditText>(R.id.editFechaAno)
        val editPeriodo = dialogView.findViewById<EditText>(R.id.editPeriodo)

        val btnSelectImage = dialogView.findViewById<Button>(R.id.editarFoto)
        btnSelectImage.setOnClickListener {
            // Lanzar el selector de imágenes
            selectImageLauncher.launch("image/*")
        }

        // Llenar los campos con los valores actuales
        editNombre.setText(ahorro?.getNombre())
        editFinal.setText(ahorro?.getCantidad()?.toString())
        editActual.setText(ahorro?.getCantidadActual()?.toString())
        editFechaDia.setText(ahorro?.getFecha()?.getDia().toString())
        editFechaMes.setText(ahorro?.getFecha()?.getMes().toString())
        editFechaAno.setText(ahorro?.getFecha()?.getAnio().toString())
        editPeriodo.setText(ahorro?.getPeriodo()?.toString())

        AlertDialog.Builder(requireContext()).setTitle(getString(R.string.editar_ahorro))
            .setView(dialogView).setPositiveButton(getString(R.string.guardar)) { _, _ ->
                // Validar los campos
                val nombre = editNombre.text.toString().trim()
                val cantidadFinalText = editFinal.text.toString().trim()
                val cantidadActualText = editActual.text.toString().trim()
                val fechaDiaText = editFechaDia.text.toString().trim()
                val fechaMesText = editFechaMes.text.toString().trim()
                val fechaAnoText = editFechaAno.text.toString().trim()
                val periodoText = editPeriodo.text.toString().trim()
                if (ahorro != null) {
                    selectedImageUri = ahorro.getImagen()
                }
                // Validación de nombre
                if (nombre.isEmpty()) {
                    Toast.makeText(
                        this.context, getString(R.string.error_nombre_vacio), Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }

                // Validación de cantidad final
                val cantidadFinal = cantidadFinalText.toDoubleOrNull()
                if (cantidadFinal == null || cantidadFinal <= 0) {
                    Toast.makeText(
                        this.context,
                        getString(R.string.error_cantidad_invalida),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }

                // Validación de cantidad actual
                val cantidadActual = cantidadActualText.toDoubleOrNull()
                if (cantidadActual == null || cantidadActual < 0) {
                    Toast.makeText(
                        this.context,
                        getString(R.string.error_cantidad_actual_invalida),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }

                // Validación de fecha
                val dia = fechaDiaText.toIntOrNull()
                val mes = fechaMesText.toIntOrNull()
                val ano = fechaAnoText.toIntOrNull()
                if (dia == null || mes == null || ano == null || dia !in 1..31 || mes !in 1..12) {
                    Toast.makeText(
                        this.context, getString(R.string.error_fecha_invalida), Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }

                // Validación de periodo
                val periodo = periodoText.toIntOrNull()
                if (periodo == null || periodo <= 0) {
                    Toast.makeText(
                        this.context, getString(R.string.error_periodo_invalido), Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }
                //Validacion de la imagen
                if (selectedImageUri == null) {
                    Toast.makeText(
                        this.context, getString(R.string.error_imagen_vacia), Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }
                // Si todo es válido, guardar los datos
                val nuevoAhorro = ahorro?.copy(
                    nombre = nombre,
                    cantidad = cantidadFinal,
                    cantidadActual = cantidadActual,
                    fecha = Fecha(dia, mes, ano),
                    ultimoAhorro = ahorro.getUltimoAhorro(),
                    imagen = selectedImageUri ?: ahorro.getImagen(),
                    periodo = periodo
                )
                if (nuevoAhorro != null) {
                    ahorroViewModel.updateAhorro(position, nuevoAhorro)
                }
            }.setNegativeButton(getString(R.string.cancelar), null).show()
    }

    override fun onResume() {
        super.onResume()
        // Cambiar el título en la Toolbar
        activity?.title = getString(R.string.all)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar memory leaks
    }

}