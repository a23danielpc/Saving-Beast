package misClases

import android.net.Uri
import java.net.URI
import java.time.LocalDate
import java.time.temporal.ChronoUnit

//Clase ahorro con atributos nombre, cantidad, cantidadActual, fecha, ultimoAhorro, imagen, periodo
class Ahorro(
    private var nombre: String,
    private var cantidad: Double,
    private var cantidadActual: Double,
    private var fecha: Fecha,
    private var ultimoAhorro: Fecha = Fecha(LocalDate.now().minusDays(1)),
    private var imagen: Uri,
    private var periodo: Int
) {

    //Getters y setters
    /////////////////////////////////////////////////
    //Funcion que retorna el nombre del ahorro
    fun getNombre(): String {
        return nombre
    }

    //Funcion que retorna la cantidad final del ahorro
    fun getCantidad(): Double {
        return cantidad
    }

    //Funcion que retorna la cantidad actual del ahorro
    fun getCantidadActual(): Double {
        return cantidadActual
    }

    //Funcion que retorna la fecha del ahorro
    fun getFecha(): Fecha {
        return fecha
    }

    //Funcion que retorna el ultimo ahorro del ahorro
    fun getUltimoAhorro(): Fecha {
        return ultimoAhorro
    }

    //Funcion que retorna la imagen del ahorro
    fun getImagen(): Uri {
        return imagen
    }

    //Funcion que retorna el periodo del ahorro
    fun getPeriodo(): Int {
        return periodo
    }

    //Funcion que modifica el nombre del ahorro
    fun setNombre(nombre: String) {
        this.nombre = nombre
    }

    //Funcion que modifica la cantidad final del ahorro
    fun setCantidad(cantidad: Double) {
        this.cantidad = cantidad
    }

    //Funcion que modifica la cantidad actual del ahorro
    fun setCantidadActual(cantidadActual: Double) {
        this.cantidadActual = cantidadActual
    }

    //Funcion que modifica la fecha del ahorro
    fun setFecha(fecha: Fecha) {
        this.fecha = fecha
    }

    //Funcion que modifica el ultimo ahorro del ahorro
    fun setUltimoAhorro(ultimoAhorro: Fecha) {
        this.ultimoAhorro = ultimoAhorro
    }

    //Funcion que modifica la imagen del ahorro
    fun setImagen(imagen: Uri) {
        this.imagen = imagen
    }

    //Funcion que modifica el periodo del ahorro
    fun setPeriodo(periodo: Int) {
        this.periodo = periodo
    }

    //Funcion que retorna la cantidad de ahorro restante
    fun getRestante(): Double {
        return cantidad - cantidadActual
    }

    //Funcion que retorna la cantidad de ahorro diario
    fun getDiario(): Double {
        return cantidad / getDiasRestantes()
    }

    //Funcion que retorna los dias restantes para llegar a la meta
    fun getDiasRestantes(): Long {
        return Math.abs(ChronoUnit.DAYS.between(ultimoAhorro.toLocalDate(), fecha.toLocalDate()))
    }

    fun copy(
        nombre: String = this.nombre,
        cantidad: Double = this.cantidad,
        cantidadActual: Double = this.cantidadActual,
        fecha: Fecha = this.fecha,
        ultimoAhorro: Fecha = this.ultimoAhorro,
        imagen: Uri = this.imagen,
        periodo: Int = this.periodo
    ): Ahorro {
        return Ahorro(nombre, cantidad, cantidadActual, fecha, ultimoAhorro, imagen, periodo)
    }

    fun getAhorroDiario(): Double {
        return cantidad / getDiasRestantes()
    }
}