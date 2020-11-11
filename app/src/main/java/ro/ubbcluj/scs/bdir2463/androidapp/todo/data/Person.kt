package ro.ubbcluj.scs.bdir2463.androidapp.todo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
class Person(
    @PrimaryKey @ColumnInfo(name = "_id") val _id: String,
    @ColumnInfo(name = "nume") var nume: String,
    @ColumnInfo(name = "prenume") var prenume: String,
    @ColumnInfo(name = "telefon") var telefon: String,
    @ColumnInfo(name = "ocupatie") var ocupatie: String
) {
    override fun toString(): String {
        return "Person(_id='$_id', nume='$nume', prenume='$prenume', telefon='$telefon', ocupatie='$ocupatie')"
    }
}