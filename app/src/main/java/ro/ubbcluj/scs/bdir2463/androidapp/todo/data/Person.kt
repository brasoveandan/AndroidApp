package ro.ubbcluj.scs.bdir2463.androidapp.todo.data

class Person(
    val id: String,
    var nume: String,
    var prenume: String,
    var telefon: String,
    var ocupatie: String,
) {
    override fun toString(): String {
        return "Person(id='$id', nume='$nume', prenume='$prenume', telefon='$telefon', ocupatie='$ocupatie')"
    }
}