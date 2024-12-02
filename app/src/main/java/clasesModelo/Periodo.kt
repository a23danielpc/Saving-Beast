package clasesModelo

//Data class Periodo con atributos nombre:String y dias:Int y un constructor secundario que recibe un nombre y establece un entero
data class Periodo(var nombre: String, var dias: Int) {
    fun getDias(): Any {
        return dias
    }

    constructor(nombre: String) : this(nombre, 0) {
        if (nombre == "Diario") {
            this.dias = 1
        } else if (nombre == "Semanal") {
            this.dias = 7
        } else if (nombre == "Mensual") {
            this.dias = 30
        } else if (nombre == "Anual") {
            this.dias = 365
        } else {
            this.nombre = "Libre"
            this.dias = 0
        }
    }
}