package clasesModelo

import java.io.File
import java.time.temporal.ChronoUnit

//Clase ahorro con atributos nombre, cantidad, cantidadActual, fecha, ultimoAhorro, imagen, periodo
class Ahorro(
    private var nombre: String,
    private var cantidad: Double,
    private var cantidadActual: Double,
    private var fecha: Fecha,
    private var ultimoAhorro: Fecha,
    private var imagen: File,
    private var periodo: Periodo
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
    fun getImagen(): File {
        return imagen
    }

    //Funcion que retorna el periodo del ahorro
    fun getPeriodo(): Periodo {
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
    fun setImagen(imagen: File) {
        this.imagen = imagen
    }

    //Funcion que modifica el periodo del ahorro
    fun setPeriodo(periodo: Periodo) {
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
    private fun getDiasRestantes(): Long {
        return Math.abs(ChronoUnit.DAYS.between(ultimoAhorro.toLocalDate(), fecha.toLocalDate()))
    }
}