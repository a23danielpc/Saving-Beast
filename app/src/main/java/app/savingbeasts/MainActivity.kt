package app.savingbeasts

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
                // Validar los campos
                val nombre = editNombre.text.toString().trim()
                val cantidadActualText = editActual.text.toString().trim()
                val fechaDiaText = editFechaDia.text.toString().trim()
                val fechaMesText = editFechaMes.text.toString().trim()
                val fechaAnoText = editFechaAno.text.toString().trim()
                val periodoText = editPeriodo.text.toString().trim()

                // Validación de nombre
                if (nombre.isEmpty()) {
                    Toast.makeText(this, getString(R.string.error_nombre_vacio), Toast.LENGTH_SHORT)
                        .show()
                    return@setPositiveButton
                }

                // Validación de cantidad actual
                val cantidadActual = cantidadActualText.toDoubleOrNull()
                if (cantidadActual == null || cantidadActual <= 0) {
                    Toast.makeText(this, getString(R.string.error_cantidad_invalida), Toast.LENGTH_SHORT)
                        .show()
                    return@setPositiveButton
                }

                // Validación de fecha
                val dia = fechaDiaText.toIntOrNull()
                val mes = fechaMesText.toIntOrNull()
                val ano = fechaAnoText.toIntOrNull()
                if (dia == null || mes == null || ano == null || dia !in 1..31 || mes !in 1..12) {
                    Toast.makeText(this, getString(R.string.error_fecha_invalida), Toast.LENGTH_SHORT)
                        .show()
                    return@setPositiveButton
                }

                // Validación de periodo
                val periodo = periodoText.toIntOrNull()
                if (periodo == null || periodo <= 0) {
                    Toast.makeText(this, getString(R.string.error_periodo_invalido), Toast.LENGTH_SHORT)
                        .show()
                    return@setPositiveButton
                }
                //Validacion de la imagen
                if (selectedImageUri == null) {
                    Toast.makeText(this, getString(R.string.error_imagen_vacia), Toast.LENGTH_SHORT)
                        .show()
                    return@setPositiveButton
                }

                // Si todo es válido, guardar los datos
                val nuevoAhorro = Ahorro(
                    nombre = nombre,
                    cantidad = cantidadActual,
                    fecha = Fecha(dia!!, mes!!, ano!!),
                    ultimoAhorro = Fecha(LocalDate.now().minusDays(1)),
                    imagen = selectedImageUri!!, // Actualizar con la URI seleccionada
                    cantidadActual = 0.0,
                    periodo = periodo
                )
                ahorroViewModel.addAhorro(nuevoAhorro)
            }.setNegativeButton(getString(R.string.cancelar), null).show()
    }
    fun reloadFragments() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentHome = HomeFragment()
        val fragmentAll = AllFragment()
        val fragmentStatistics = StatisticsFragment()

        fragmentManager.beginTransaction()
            .replace(R.id.homeFragment, fragmentHome)
            .replace(R.id.allFragment, fragmentAll)
            .replace(R.id.statisticsFragment, fragmentStatistics)
            .commit()
    }
}
