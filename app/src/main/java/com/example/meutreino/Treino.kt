package com.example.meutreino

class Treino {
    var _id:Int
        get() {
            return field
        }
        set(value) { field = value }
    var circuito:String
        get() {
            return field
        }
        set(value) { field = value }

    constructor(_id: Int, circuito: String) {
        this._id = _id;
        this.circuito = circuito;
    }
}