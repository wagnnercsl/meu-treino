package com.example.meutreino

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_cadastrar.*
import com.example.meutreino.CHANNEL_2_ID

class CadastrarActivity : AppCompatActivity() {
    val treino: Treino = Treino()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_cadastrar)

        if(intent != null) {
            val bundle: Bundle? = intent.extras
            if(bundle != null) {
                treino._id = intent.getIntExtra("_id", 0)
                treino.circuito = intent.getStringExtra("circuito").toString().trim()
                treino.atividade = intent.getStringExtra("atividade").toString().trim()
                treino.duracao = intent.getIntExtra("duracao", 0)
                treino.data_treino = intent.getStringExtra("data").toString()
                treino.realizado = intent.getBooleanExtra("realizado", false)

                txtCircuito.setText(treino.circuito)
                txtAtividade.setText(treino.atividade)
                txtDuracao.setText(treino.duracao.toString())
                val arrData = treino.data_treino.split('-')
                val ano = Integer.parseInt(arrData[0])
                val mes = Integer.parseInt(arrData[1])
                val dia = Integer.parseInt(arrData[2])

                dpData.init(ano, mes, dia, null)

                if(treino.realizado) {
                    txtStatus.text = "CONCLUÍDO"
                    txtStatus.setTextColor(Color.GREEN)
                    ckStatus.isChecked = true
                } else {
                    layConcluir.visibility = View.VISIBLE
                }

                btnSalvar.visibility = View.GONE
                btnAtualizar.visibility = View.VISIBLE
            }
        } else {
            txtStatus.visibility = View.GONE
        }
    }

    fun NovoTreino(view: View) {
        var database = Database(this)

        if(!validaCampos()) {
            Toast.makeText(this, " Ajuste os valores ", Toast.LENGTH_LONG).show()
        } else {

            val treino: Treino = Treino()
            treino.circuito = txtCircuito.text.toString().trim()
            treino.atividade = txtAtividade.text.toString().trim()
            treino.duracao = Integer.parseInt(txtDuracao.text.toString())
            treino.data_treino =
                dpData.year.toString() + '-' + dpData.month + '-' + dpData.dayOfMonth
            treino.realizado = false

            val ID = database.Insert(treino)
            if (ID > 0) {
                Toast.makeText(this,
                    " Treino incluído com sucesso! ",
                    Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, " Erro ao incluir o treino ", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun AtualizarTreino(view: View) {
        var database = Database(this)

        val valCircuito = txtCircuito.text.toString()

        treino.circuito = txtCircuito.text.toString()
        treino.atividade = txtAtividade.text.toString()
        treino.duracao = Integer.parseInt(txtDuracao.text.toString())
        treino.data_treino = dpData.year.toString() + '-' + dpData.month + '-' + dpData.dayOfMonth
        treino.realizado = ckStatus.isChecked

        val ID = database.Update(treino, "", arrayOf(treino._id.toString()))
        if(ID > 0) {
            Toast.makeText(this, " Treino atualizado com sucesso! ", Toast.LENGTH_LONG).show()
            if(treino.realizado) {

            val somAlarme = RingtoneManager.getDefaultUri(Notification.DEFAULT_SOUND)

            val builder =  NotificationCompat.Builder(this, CHANNEL_2_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Mais um treino concluído, parabéns!")
                    .setContentText("Continue assim que você vai longe, foco!")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSound(somAlarme)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)

                with(NotificationManagerCompat.from(this)) {
                    notify(1, builder.build())
                }
            }

            val intent = Intent(this, ListarActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, " Erro ao atualizar o treino ", Toast.LENGTH_LONG).show()
        }

    }

    private fun validaCampos(): Boolean {

        if(txtCircuito.text.toString().isNullOrBlank()) {
            txtCircuito.setError("Digite o nome do circuito")
            return false
        }
        if(txtAtividade.text.toString().isNullOrBlank()) {
            txtAtividade.setError("Digite o nome da atividade")
            return false
        }
        if(txtDuracao.text.toString().isNullOrBlank()) {
            txtDuracao.setError("Digite a duração do treino")
            return false
        }
        /*if(!dpData.dayOfMonth.toString().isNullOrBlank() &&
            !dpData.month.toString().isNullOrBlank() &&
            !dpData.year.toString().isNullOrBlank()) {
            txtAtividade.setError("Informe a data do treino")
            return false
        }*/

        return true;
    }
}