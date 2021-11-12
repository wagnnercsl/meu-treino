package com.example.meutreino

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder

class Database {
    val NOM_BANCO = "meu_treino"
    val VER_BANCO = 3

    val NOM_TABELA = "treino"
    var COL_ID = "_id"
    val COL_CIRCUITO = "circuito"
    val COL_ATIVIDADE = "atividade"
    val COL_DURACAO = "duracao"
    val COL_DATA = "data"
    val COL_HORA = "hora"
    val COL_REALIZADO = "realizado"

    var database:SQLiteDatabase?=null
    val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${NOM_TABELA} (" +
                "${COL_ID} INTEGER PRIMARY KEY," +
                "${COL_CIRCUITO} TEXT," +
                "${COL_ATIVIDADE} TEXT," +
                "${COL_DURACAO} INT," +
                "${COL_DATA} TEXT," +
                "${COL_HORA} TEXT," +
                "${COL_REALIZADO} BOOLEAN)"

    val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${NOM_TABELA}"

    constructor(context: Context) {
        var db = DatabaseHelperTreino(context)
        database = db.writableDatabase
    }

    inner class DatabaseHelperTreino(context: Context) : SQLiteOpenHelper(context, NOM_BANCO, null, VER_BANCO) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        }
    }

    fun Insert(treino: Treino): Long {
        val values = ContentValues()
        values.put("circuito", treino.circuito)
        values.put("atividade", treino.atividade)
        values.put("duracao", treino.duracao)
        values.put("data", treino.data_treino)
        values.put("realizado", treino.realizado)

        val id = database!!.insert(NOM_TABELA, "", values)
        return id;
    }

    fun Select(projection: Array<String>, selection: String, selectionArgs: Array<String>, sortOrder: String): Cursor {
        val queryBuilder = SQLiteQueryBuilder()

        queryBuilder.tables = NOM_TABELA
        val cursor = queryBuilder.query(database, projection, selection, selectionArgs, null,  null, sortOrder)
        return cursor
    }

    fun Update(treino: Treino, selection: String, selectionArgs:Array<String>): Int {
        val values = ContentValues()
        values.put("_id", treino._id)
        values.put("circuito", treino.circuito)
        values.put("atividade", treino.atividade)
        values.put("duracao", treino.duracao)
        values.put("data", treino.data_treino)
        values.put("realizado", treino.realizado)

        val count = database!!.update(NOM_TABELA, values, "_id = ?", selectionArgs)
        return count
    }

    fun Delete(selection: String, selectionArgs:Array<String>): Int {
        val count = database!!.delete(NOM_TABELA, selection, selectionArgs)
        return count
    }

}