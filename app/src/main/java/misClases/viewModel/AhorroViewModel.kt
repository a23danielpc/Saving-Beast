package misClases.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import misClases.Ahorro

class AhorroViewModel : ViewModel() {
    private val _ahorros = MutableLiveData<MutableList<Ahorro>>()
    val ahorros: LiveData<MutableList<Ahorro>> get() = _ahorros

    init {
        // Inicializa la lista con algunos elementos
        _ahorros.value = mutableListOf()
    }

    // Función para actualizar un ahorro
    fun updateAhorro(position: Int, ahorro: Ahorro) {
        _ahorros.value?.let {
            it[position] = ahorro
            _ahorros.value = it // Notificar a los observadores
        }
    }

    // Función para eliminar un ahorro
    fun deleteAhorro(position: Int) {
        _ahorros.value?.let {
            it.removeAt(position)
            _ahorros.value = it // Notificar a los observadores
        }
    }

    fun addAhorro(nuevoAhorro: Ahorro) {
        _ahorros.value?.let {
            it.add(nuevoAhorro)
            _ahorros.value = it // Notificar a los observadores
        }
    }

}