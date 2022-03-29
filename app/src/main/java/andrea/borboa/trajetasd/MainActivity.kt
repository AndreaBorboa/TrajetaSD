package andrea.borboa.trajetasd

import andrea.borboa.trajetasd.databinding.ActivityMainBinding
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.widget.Toast
import java.io.*
import android.database.sqlite.SQLiteDatabase


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var amigosDBHelper: miSQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        amigosDBHelper = miSQLiteHelper(this)

        binding.btGuardar.setOnClickListener {
            if(binding.etNombre.text.isNotBlank() &&
                binding.etEmail.text.isNotBlank()){
                    amigosDBHelper.anyadirDato(binding.etNombre.text.toString(),
                        binding.etEmail.text.toString())
                binding.etNombre.text.clear()
                binding.etEmail.text.clear()
                Toast.makeText(this,"Guardado",
                    Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this,"No se ha podido guardar",
                        Toast.LENGTH_SHORT).show()
            }
        }

        binding.btConsultar.setOnClickListener{
            binding.tvConsulta.text=""
            val db : SQLiteDatabase = amigosDBHelper.readableDatabase
            val cursor = db.rawQuery(
                "SELECT * FROM amigos",
                null)

            if(cursor.moveToFirst()){
                do{
                    binding.tvConsulta.append(
                        cursor.getInt(0).toString()+ ": ")
                    binding.tvConsulta.append(
                        cursor.getString(1).toString()+ ": ")
                    binding.tvConsulta.append(
                        cursor.getString(2).toString()+ "\n")
                } while (cursor.moveToNext())
            }
        }
    }

    fun Guardar (texto: String){
        try{
            val rutaSD = baseContext.getExternalFilesDir(null)?.absolutePath
            val miCarpeta = File(rutaSD,"datos")
            if(!miCarpeta.exists()){
                miCarpeta.mkdir()
            }
            val ficheroFisico = File(miCarpeta, "datos.txt")
            ficheroFisico.appendText("$texto\n")
        } catch (e: Exception) {
            Toast.makeText(this,
                "No se ha podido guardar",
                Toast.LENGTH_LONG).show()
        }
    }


    fun Cargar() : String {
        var texto = ""
        try{
            val rutaSD = baseContext.getExternalFilesDir(null)?.absolutePath
            val miCarpeta = File(rutaSD, "datos")
            val ficheroFisico = File(miCarpeta, "datos.txt")
            val fichero = BufferedReader(
                InputStreamReader(FileInputStream(ficheroFisico))
            )
            texto = fichero.use(BufferedReader::readText)
        }
        catch(e : Exception){
            //NADA
        }
        return texto
    }



}