package com.example.meutreino

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
        val projections = arrayOf("_id", "circuito")
        val selectionArgs = arrayOf(nomeTreino)
        val cursor = database.Select(projections, "circuito like ?", selectionArgs, "_id")

        listTreino.clear()
        if (cursor.moveToFirst()) {
            do {
                val _id = cursor.getInt(cursor.getColumnIndex("_id"))
                val circuito = cursor.getString(cursor.getColumnIndex("circuito"))

                listTreino.add(Treino(_id, circuito))
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

            viewDetalhe.ivSave.setOnClickListener(View.OnClickListener {
                var database = Database(this.context!!)
                val selectionArgs = arrayOf(treinoAtual._id.toString())

                Toast.makeText(this.context, " Editando! ", Toast.LENGTH_SHORT).show()
                viewDetalhe.ivEdit.visibility = View.VISIBLE
                viewDetalhe.ivDelete.visibility = View.VISIBLE
            })

            viewDetalhe.ivEdit.setOnClickListener(View.OnClickListener {
                var database = Database(this.context!!)
                val selectionArgs = arrayOf(treinoAtual._id.toString())

                Toast.makeText(this.context, " Editando! ", Toast.LENGTH_SHORT).show()
                viewDetalhe.ivEdit.visibility = View.GONE
                viewDetalhe.ivDelete.visibility = View.GONE

                val intent = Intent(this.context, CadastrarActivity::class.java)
                intent.putExtra("circuito", listTreino.get(position).circuito)
                intent.putExtra("_id", listTreino.get(position)._id)
                startActivity(intent)
            })

            viewDetalhe.ivDelete.setOnClickListener(View.OnClickListener {
                var database = Database(this.context!!)
                val selectionArgs = arrayOf(treinoAtual._id.toString())

                Toast.makeText(this.context, " Deletando! ", Toast.LENGTH_SHORT).show()
                database.Delete("_id=?", selectionArgs)
                viewDetalhe.visibility = View.GONE
                loadQuery("%")
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