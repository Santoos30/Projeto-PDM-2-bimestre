package model

data class Atividade(
    var id: String? = null,
    var tipo: String? = null,
    var titulo: String? = null,
    var dificuldade: String? = null,
    var respostaCorreta: String? = null
)