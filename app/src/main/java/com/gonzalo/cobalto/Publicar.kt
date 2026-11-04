package com.gonzalo.cobalto

import android.content.Context
import org.json.JSONObject
import java.security.MessageDigest

/**
 * Lo que Cobalto publica para Azabache (y quien venga), bajo `/users/<uid>/cobalto/` (ver docs/CONTRATO-DATOS.md):
 *  - `lectura/<obra>/<capítulo>`: una copia de SOLO LECTURA del texto de cada capítulo marcado como terminado («hecho» en el
 *    capítulo o en su parte). Al quitar la marca, se quita la copia. Es lo que lee Azabache: nunca los documentos de Escribir.
 *  - `palabras/<dispositivo>`: las palabras escritas cada día en este dispositivo (`"AAAA-MM-DD"` → palabras), ligero, para las
 *    barras y el Diario. Cada dispositivo escribe solo lo suyo.
 * Se publica tras cada sincronización de Escribir (con la app abierta o desde la tarea de fondo); solo lo que ha cambiado.
 */
object Publicar {
    private fun prefs(c: Context) = c.getSharedPreferences("cobalto_publicar", Context.MODE_PRIVATE)

    /** Los 16 primeros hex del SHA-1 (para que un texto largo tenga una firma corta). */
    fun firma(s: String): String = MessageDigest.getInstance("SHA-1").digest(s.toByteArray()).joinToString("") { "%02x".format(it) }.take(16)

    /** Un día (días desde 1970, en la hora de aquí) como «AAAA-MM-DD». */
    fun fecha(dia: Long): String = java.time.LocalDate.ofEpochDay(dia).toString()

    /** El texto de un capítulo tal como se lee: sus escenas, separadas por un salto de escena. */
    fun textoCapitulo(e: Escritos, cid: String): String = e.escenas(cid).map { it.s("texto").trim() }.filter { it.isNotEmpty() }.joinToString("\n\n***\n\n")

    /** Lo que debe haber publicado de cada capítulo terminado: obra → capítulo → documento. */
    fun capitulosTerminados(e: Escritos): Map<String, Map<String, JSONObject>> {
        val out = HashMap<String, HashMap<String, JSONObject>>()
        for (o in e.obras()) {
            val caps = e.capitulos(o.id)
            caps.forEachIndexed { i, c ->
                if (!Compartir.hecho(e, c)) return@forEachIndexed
                val texto = textoCapitulo(e, c.id)
                val parte = e.get(c.s("aid"))
                out.getOrPut(o.id) { HashMap() }[c.id] = JSONObject()
                    .put("t", o.s("titulo")).put("a", o.s("autor")).put("c", c.s("titulo")).put("n", i + 1)
                    .apply { if (parte != null) put("pa", parte.s("titulo")) }
                    .put("u", maxOf(c.u, e.escenas(c.id).maxOfOrNull { it.u } ?: 0L)).put("p", Escritos.palabras(texto)).put("x", texto)
            }
        }
        return out
    }

    /** Las palabras de cada día de este dispositivo. */
    fun palabrasPorDia(e: Escritos): Map<String, Int> {
        val out = HashMap<String, Int>()
        for (d in e.todos().filter { it.t == Escritos.T_LOG }) {
            val n = d.o.optJSONObject("dev")?.optInt(Cuenta.dispositivo) ?: 0
            if (n > 0) out[fecha(d.n("dia"))] = (out[fecha(d.n("dia"))] ?: 0) + n
        }
        return out
    }

    /** Publica lo que haya cambiado (bloqueante, con red): llamar fuera del hilo principal. */
    fun todo(c: Context): Boolean {
        if (!Cuenta.vinculada) return false
        val e = Escritos.get(c)
        val p = prefs(c)
        val raiz = Cuenta.raiz
        var cambios = false
        // ---- Los capítulos terminados
        val quiero = capitulosTerminados(e)
        val firmas = runCatching { JSONObject(p.getString("lectura", "{}")!!) }.getOrDefault(JSONObject())
        val nuevas = JSONObject()
        val cuerpo = JSONObject()
        quiero.forEach { (pid, caps) -> caps.forEach { (cid, doc) ->
            val f = firma(doc.toString())
            nuevas.put("$pid/$cid", f)
            if (firmas.optString("$pid/$cid") != f) cuerpo.put("lectura/$pid/$cid", doc)
        } }
        for (k in firmas.keys()) if (!nuevas.has(k)) cuerpo.put("lectura/$k", JSONObject.NULL)
        // ---- Las palabras de cada día de este dispositivo
        val dias = palabrasPorDia(e)
        val firmaDias = firma(dias.toSortedMap().toString())
        if (p.getString("palabras", "") != firmaDias) cuerpo.put("palabras/${Cuenta.dispositivo}", JSONObject().also { o -> dias.forEach { (k, v) -> o.put(k, v) } })
        if (cuerpo.length() > 0) {
            Cuenta.db("PATCH", raiz, cuerpo.toString(), "print=silent")
            cambios = true
        }
        p.edit().putString("lectura", nuevas.toString()).putString("palabras", firmaDias).apply()
        return cambios
    }

    /** Al salir de la cuenta: se olvida lo publicado (en la siguiente cuenta se publica todo). */
    fun olvidar(c: Context) = prefs(c).edit().clear().apply()
}
