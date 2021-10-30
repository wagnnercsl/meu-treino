package com.example.meutreino

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login()
        setContentView(R.layout.activity_main)
    }

    fun login() {
        var txt = "Hello World"
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
    }

    fun loadCadastro(view: View) {
        val intent = Intent(this, CadastrarActivity::class.java)
        startActivity(intent)
    }

    fun loadLista(view: View) {
        val intent = Intent(this, ListarActivity::class.java)
        startActivity(intent)
    }

}