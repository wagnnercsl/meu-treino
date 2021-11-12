package com.example.meutreino

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detalhe.*
import kotlinx.android.synthetic.main.activity_detalhe.view.*

class ListarActivity: AppCompatActivity() {
    var listTreino = ArrayList<Treino>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar)

        loadQuery("%")
    }

    fun loadQuery(nomeTreino: String) {
        var database = Database(this)
        val projections = arrayOf("_id", "circuito", "atividade", "duracao", "data", "realizado")
        val selectionArgs = arrayOf(nomeTreino)
        val cursor = database.Select(projections, "circuito like ?", selectionArgs, "_id")

        listTreino.clear()
        if (cursor.moveToFirst()) {
            do {
                var treino: Treino = Treino()
                treino._id = cursor.getInt(cursor.getColumnIndex("_id"))
                treino.circuito = cursor.getString(cursor.getColumnIndex("circuito"))
                treino.atividade = cursor.getString(cursor.getColumnIndex("atividade"))
                treino.duracao = Integer.parseInt(cursor.getString(cursor.getColumnIndex("duracao")))
                treino.data_treino = cursor.getString(cursor.getColumnIndex("data"))
                val treinoRealizado = cursor.getInt(cursor.getColumnIndex("realizado")) > 0
                treino.realizado = treinoRealizado

                listTreino.add(treino)
            } while (cursor.moveToNext())
        }

        var treinosAdapter = TreinosAdapter(this, listTreino)
        val lvListarTreinos = findViewById(R.id.lvListarTreinos) as ListView
        lvListarTreinos.adapter = treinosAdapter
    }

    //var treinoAdapter = TreinoAdapter()
    inner class TreinosAdapter: BaseAdapter {
        var listTreinosAdapter = ArrayList<Treino>()
        var context: Context?=null

        constructor(context: Context, listTreinosAdapter: ArrayList<Treino>):super() {
            this.listTreinosAdapter = listTreinosAdapter
            this.context = context
        }

        override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
            var viewDetalhe = layoutInflater.inflate(R.layout.activity_detalhe, null)
            var treinoAtual = listTreinosAdapter[position]

            viewDetalhe.tvId.text = treinoAtual._id.toString()
            viewDetalhe.tvCircuito.text = treinoAtual.circuito
            viewDetalhe.tvAtividade.text = treinoAtual.atividade
            viewDetalhe.tvDuracao.text = treinoAtual.duracao.toString() + " minutos"

            val arrData = treinoAtual.data_treino.split('-')
            val dataFormatada = arrData[2].padStart(2, '0') + "/" + arrData[1] + "/" + arrData[0]
            viewDetalhe.tvData.text = dataFormatada

            viewDetalhe.ivEdit.setOnClickListener(View.OnClickListener {
                var database = Database(this.context!!)
                val selectionArgs = arrayOf(treinoAtual._id.toString())

                Toast.makeText(this.context, " Editando! ", Toast.LENGTH_SHORT).show()

                val intent = Intent(this.context, CadastrarActivity::class.java)
                intent.putExtra("_id", listTreino.get(position)._id)
                intent.putExtra("circuito", listTreino.get(position).circuito)
                intent.putExtra("atividade", listTreino.get(position).atividade)
                intent.putExtra("duracao", listTreino.get(position).duracao)
                intent.putExtra("data", listTreino.get(position).data_treino)
                intent.putExtra("realizado", listTreino.get(position).realizado)
                startActivity(intent)
            })

            viewDetalhe.ivDelete.setOnClickListener(View.OnClickListener {
                val builder = AlertDialog.Builder(this@ListarActivity)
                builder.setMessage("Tem certeza que deseja excluir?")
                    .setPositiveButton("Sim") { dialog, id ->
                        var database = Database(this.context!!)
                        val selectionArgs = arrayOf(treinoAtual._id.toString())

                        Toast.makeText(this.context, " Excluído! ", Toast.LENGTH_SHORT).show()
                        database.Delete("_id=?", selectionArgs)
                        viewDetalhe.visibility = View.GONE
                        loadQuery("%")
                    }
                    .setNegativeButton("Não") { dialog, id ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()

            })

            return viewDetalhe;
        }

        override fun getItem(p0: Int): Any {
            return listTreinosAdapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return listTreinosAdapter.size
        }
    }
}