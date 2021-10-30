package com.example.meutreino

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cadastrar.*

class CadastrarActivity : AppCompatActivity() {
    val treino: Treino = Treino(0, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar)

        var circuitoEt = txtCircuito.text.toString()

        val newIntent: Intent = intent
        if(intent != null) {
            val bundle: Bundle? = intent.extras
            if(bundle != null) {
                treino._id = intent.getIntExtra("_id", 0)
                treino.circuito = intent.getStringExtra("circuito").toString()
                txtCircuito.setText(treino.circuito)
                btnSalvar.visibility = View.GONE
                btnAtualizar.visibility = View.VISIBLE
            }
        }
    }

    fun NovoTreino(view: View) {
        var database = Database(this)

        val valCircuito = txtCircuito.text.toString()

        val treino: Treino = Treino(0, valCircuito)
        treino.circuito = valCircuito

        val ID = database.Insert(treino)
        if(ID > 0) {
            Toast.makeText(this, " Treino incluído com sucesso! " + treino.circuito + ID, Toast.LENGTH_LONG).show()
            finish()
        } else {
            Toast.makeText(this, " Erro ao incluir o treino ", Toast.LENGTH_LONG).show()
        }

    }

    fun AtualizarTreino(view: View) {
        var database = Database(this)

        val valCircuito = txtCircuito.text.toString()

        treino.circuito = valCircuito

        val ID = database.Update(treino, "", arrayOf(treino._id.toString()))
        if(ID > 0) {
            Toast.makeText(this, " Treino atualizado com sucesso! " + treino.circuito + ID, Toast.LENGTH_LONG).show()
            //finish()
            val intent = Intent(this, ListarActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, " Erro ao atualizar o treino ", Toast.LENGTH_LONG).show()
        }

    }
}