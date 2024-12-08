package app.savingbeasts

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import misClases.Ahorro
import misClases.Fecha
import misClases.viewModel.AhorroViewModel
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
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

    private val ahorroViewModel: AhorroViewModel by viewModels()
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
                showCreateDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showCreateDialog() {
        // Obtener el objeto actual

        // Crear el diseño del diálogo
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create, null)
        val editNombre = dialogView.findViewById<EditText>(R.id.editNombre)
        val editActual = dialogView.findViewById<EditText>(R.id.editCantidadActual)
        val editFechaDia = dialogView.findViewById<EditText>(R.id.editFechaDia)
        val editFechaMes = dialogView.findViewById<EditText>(R.id.editFechaMes)
        val editFechaAno = dialogView.findViewById<EditText>(R.id.editFechaAno)
        val editPeriodo = dialogView.findViewById<EditText>(R.id.editPeriodo)
        val btnSelectImage = dialogView.findViewById<Button>(R.id.editarFoto)


        // Manejar el clic en el botón de seleccionar imagen
        btnSelectImage.setOnClickListener {
            // Lanzar el selector de imágenes
            selectImageLauncher.launch("image/*")
        }

        AlertDialog.Builder(this).setTitle(getString(R.string.guardar_ahorro)).setView(dialogView)
            .setPositiveButton(getString(R.string.guardar)) { _, _ ->
                // Guardar los cambios
                val nuevoAhorro = Ahorro(
                    nombre = editNombre.text.toString(),
                    cantidadActual = 0.00,
                    fecha = Fecha(
                        editFechaDia.text.toString().toInt(),
                        editFechaMes.text.toString().toInt(),
                        editFechaAno.text.toString().toInt()
                    ),
                    ultimoAhorro = Fecha(LocalDate.now().minusDays(1)),
                    imagen = selectedImageUri!!, // Actualizar con la URI seleccionada
                    cantidad = editActual.text.toString().toDouble(),
                    periodo = editPeriodo.text.toString().toInt()
                )
                ahorroViewModel.addAhorro(nuevoAhorro)
            }.setNegativeButton(getString(R.string.cancelar), null).show()
    }
    fun getCurrentFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        return navHostFragment.childFragmentManager.fragments.firstOrNull()
    }
    fun reloadCurrentFragment() {
        val navController = (supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment).navController
        val currentDestinationId = navController.currentDestination?.id

        if (currentDestinationId != null) {
            navController.popBackStack(currentDestinationId, true) // Eliminar el fragmento actual
            navController.navigate(currentDestinationId)          // Navegar al mismo fragmento
        }
    }
}
