package model

data class Resultado(
    var id: String? = null,
    var criancaId: String? = null,
    var criancaNome: String? = null,
    var atividadeId: String? = null,
    var atividadeTitulo: String? = null,
    var respostaRecebida: String? = null,
    var acertou: Boolean = false,
    var data: Long = 0
)