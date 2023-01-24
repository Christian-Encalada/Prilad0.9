package com.hotmail.example.proyecto_mov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hotmail.example.proyecto_mov.databinding.ActivityNotasBinding

class NotasActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNotasBinding
    private lateinit var autotext: AutoCompleteTextView
    private lateinit var materias: ArrayAdapter<String>
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotasBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_notas)
        setContentView(binding.root)
        autotext=binding.txtMateria
        binding.etDate.setOnClickListener{showDatePickerDialog() }
        binding.etHour.setOnClickListener{showTimePickerDialog() }

        val materia= resources.getStringArray(R.array.materias_array)
        materias= ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,materia)
        autotext.setAdapter(materias)

        binding.btnGuardar.setOnClickListener {
            guardar()
        }
        binding.btnConsultar.setOnClickListener {
            consultar()
        }
        binding.btnEliminar.setOnClickListener {
            eliminar()
        }
        binding.btnModificar.setOnClickListener{
            modificar()
        }
        binding.btnLimpiar.setOnClickListener {
            limpiar()
        }
        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }

    private fun guardar() {
        db.collection("Notas").get().addOnCompleteListener { task ->
            var bandera = false
            val clave = binding.txtClave.getText().toString()
            if (clave != "") {
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (document.id == clave) {
                            binding.lblSalida.setText("""
                                            NOTA: ${binding.txtClave.text}
                             Título: ${binding.txtTitulo.text}
                             Materia: ${binding.txtMateria}
                             
                             Descripción: ${binding.txtDescripcion.text}
                             
                             Fecha: ${binding.etDate.text}
                             Hora: ${binding.etDate.text}
                         """.trimIndent())
                            bandera = true
                            break
                        }
                    }
                    if (!bandera) {
                        val clave: String = binding.txtClave.text.toString()
                        val tarea: MutableMap<String, Any> = HashMap()
                        tarea["titulo"] = binding.txtTitulo.text.toString()
                        tarea["materia"] = binding.txtMateria.text.toString()
                        tarea["descripcion"] = binding.txtDescripcion.text.toString()
                        tarea["fecha"] = binding.etDate.text.toString()
                        tarea["hora"] = binding.etHour.text.toString()
                        db.collection("Notas").document(clave).set(tarea).addOnSuccessListener {
                            Toast.makeText(this, "Nota guardada", Toast.LENGTH_LONG).show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Error en Consulta", Toast.LENGTH_LONG).show()
                            binding.lblSalida.setText("""
                                                NOTA: ${binding.txtClave.text}
                                Título: ${binding.txtTitulo.text}
                                Materia: ${binding.txtMateria}
                                
                                Descripción: ${binding.txtDescripcion.text}
                                
                                Fecha: ${binding.etDate.text}
                                Hora: ${binding.etDate.text}
                            """.trimIndent())
                        }.addOnFailureListener {
                            Toast.makeText(this, "¡Error!", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "¡Ya existe esa Nota!", Toast.LENGTH_LONG).show()
                        limpiar()
                    }
                } else {
                    Toast.makeText(this, "¡Error en consulta!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Ingrese datos",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun consultar() {
        val clave = binding.txtClave.text.toString()
        if(!"".equals(clave)) {
            db.collection("Notas").document(clave).get().addOnSuccessListener {
                val titulo = it.get("titulo") as String?
                val materia= it.get("materia") as String?
                val descripcion = it.get("descripcion") as String?
                val fecha = it.get("fecha") as String?
                val hora = it.get("hora") as String?

                if(!null.equals(titulo)) {
                    binding.txtTitulo.setText(titulo)
                    binding.txtMateria.setText(materia)
                    binding.txtDescripcion.setText(descripcion)
                    binding.etDate.setText(fecha)
                    binding.etHour.setText(hora)
                    binding.lblSalida.setText("""
                                NOTA: ${clave}
                Título: ${titulo}
                Materia: ${materia}
                
                Descripción: ${descripcion}
                
                Fecha: ${fecha}
                Hora: ${hora}
            """.trimIndent())
                }else{
                    Toast.makeText(this,"Error de consulta",Toast.LENGTH_LONG).show()
                    limpiar()
                }
            }
        }else{
            binding.txtClave.error="¡Ingrese Clave!"
            limpiar()
        }
    }

    private fun modificar() {
        db.collection("Notas").get().addOnCompleteListener {
            task ->
            var bandera=false
            if(task.isSuccessful){
                for(document in task.result!!){
                    var clave =  binding.txtClave.getText().toString()
                    if(document.id == clave){
                        binding.lblSalida.setText("""
                                                NOTA: ${binding.txtClave.text}
                                Título: ${binding.txtTitulo.text}
                                Materia: ${binding.txtMateria.text}
                                
                                Descripción: ${binding.txtDescripcion.text}
                                
                                Fecha: ${binding.etDate.text}
                                Hora: ${binding.etHour.text}
                         """.trimIndent())
                        bandera=true
                        break
                    }
                }
                if(bandera){
                    val clave : String = binding.txtClave.text.toString()
                    val titulo : String = binding.txtTitulo.text.toString()
                    val materia : String = binding.txtMateria.text.toString()
                    val descripcion : String = binding.txtDescripcion.text.toString()
                    val fecha : String = binding.etDate.text.toString()
                    val hora : String = binding.etHour.text.toString()
                    val tarea : MutableMap<String , Any> = HashMap()
                    tarea["titulo"]=titulo
                    tarea["materia"]=materia
                    tarea["descripcion"]=descripcion
                    tarea["fecha"]=fecha
                    tarea["hora"]=hora
                    db.collection("Notas").document(clave).update(tarea).addOnSuccessListener{
                        Toast.makeText(this,"Nota Modificada",Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        Toast.makeText(this,"¡Error en Consulta!",Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this,"¡No existe Nota!",Toast.LENGTH_LONG).show()
                    limpiar()
                }
            }else{
                Toast.makeText(this,"¡No existe Nota!",Toast.LENGTH_LONG).show()
                limpiar()
            }
        }
    }

    private fun eliminar() {
        db.collection("Notas").get().addOnCompleteListener { task ->
            var bandera = false
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val clave = binding.txtClave.text.toString()
                    if (document.id == clave) {
                        db.collection("Notas").document(clave).delete()
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Nota Elimianda", Toast.LENGTH_LONG).show()
                                    limpiar()
                                }.addOnFailureListener {
                                    Toast.makeText(this,"¡Error!",Toast.LENGTH_SHORT).show()
                                }
                        bandera = true
                        break
                    }
                }
                if(!bandera){
                    Toast.makeText(this,"No Existe Nota",Toast.LENGTH_LONG).show()
                    limpiar()
                }
            }
        }
    }

    private fun showTimePickerDialog() {
        val timePicker=TimePickerFragment{hour,minute->onTimeSelected(hour,minute)}
        timePicker.show(supportFragmentManager,"timePicker")
    }
    private fun onTimeSelected(hour: Int, minute: Int) {
        binding.etHour.setText("$hour : $minute")
    }
    private fun showDatePickerDialog() {
        val datePicker= DatePickerFragment{day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }
    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val mes = month + 1
        binding.etDate.setText("$day/$mes/$year")
    }
    private fun limpiar(){
        binding.txtClave.setText("")
        binding.txtTitulo.setText("")
        binding.txtMateria.setText("")
        binding.txtDescripcion.setText("")
        binding.etDate.setText("")
        binding.etHour.setText("")
        binding.lblSalida.setText("")
    }
}