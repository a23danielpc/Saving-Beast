package app.savingbeasts

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import misClases.Fecha
import misClases.Periodo
import misClases.viewModel.AhorroViewModel

class MainActivity : AppCompatActivity() {
    companion object {
        private const val GALLERY_REQUEST_CODE = 1001
    }

    private val ahorroViewModel: AhorroViewModel = TODO()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbarSuperior = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbarSuperior)

        val toolbar = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val navController =
            (supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment).navController


        toolbar.setupWithNavController(navController)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_superior, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_button -> {
                // Acción para el botón
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun showEditDialog(position: Int) {
        // Obtener el objeto actual
        val ahorro = ahorroViewModel.ahorros.value?.get(position)

        // Crear el diseño del diálogo
        val dialogView = LayoutInflater.from(this.baseContext).inflate(R.layout.dialog_create, null)
        val editNombre = dialogView.findViewById<EditText>(R.id.editNombre)
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

        editActual.setText(ahorro?.getCantidadActual()?.toString())

        AlertDialog.Builder(this.baseContext).setTitle(getString(R.string.editar_ahorro))
            .setView(dialogView).setPositiveButton(getString(R.string.guardar)) { _, _ ->
                // Guardar los cambios
                val nuevoAhorro = ahorro?.copy(
                    nombre = editNombre.text.toString() ?: ahorro.getNombre(),
                    cantidadActual = editActual.text.toString().toDoubleOrNull()
                        ?: ahorro.getCantidadActual(),
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
        startActivityForResult(
            intent, GALLERY_REQUEST_CODE
        ) // Inicia la actividad para recibir un resultado
    }
}
