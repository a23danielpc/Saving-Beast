package misClases

import java.time.LocalDate

class Fecha {
    private var dia: Int
    private var mes: Int
    private var anio: Int

    constructor(dia: Int, mes: Int, anio: Int) {
        this.dia = dia
        this.mes = mes
        this.anio = anio
    }
constructor(fecha:String){
    val fechaArray = fecha.split("/")
    this.dia = fechaArray[0].toInt()
    this.mes = fechaArray[1].toInt()
    this.anio = fechaArray[2].toInt()
}
    constructor(fecha: LocalDate) {

        this.dia = fecha.dayOfMonth
        this.mes = fecha.monthValue
        this.anio = fecha.year
    }

    fun getDia(): Int {
        return dia
    }

    fun getMes(): Int {
        return mes
    }

    fun getAnio(): Int {
        return anio
    }

    fun setDia(dia: Int) {
        this.dia = dia
    }

    fun setMes(mes: Int) {
        this.mes = mes
    }

    fun setAnio(anio: Int) {
        this.anio = anio
    }

    fun isBisiesto(): Boolean {
        return anio % 4 == 0 && (anio % 100 != 0 || anio % 400 == 0)
    }

    fun diasMes(): Int {
        return when (mes) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isBisiesto()) 29 else 28
            else -> 0
        }
    }

    override fun toString(): String {
        return "$dia/$mes/$anio"
    }

    fun toLocalDate(): LocalDate {
        return LocalDate.of(anio, mes, dia)
    }
}