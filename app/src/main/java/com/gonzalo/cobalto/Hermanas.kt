package com.gonzalo.cobalto

import android.content.Context
import android.content.Intent

/** Las apps hermanas que están en este móvil: Marfil lee los capítulos terminados (los que Cobalto publica en tu cuenta). */
object Hermanas {
    const val PAQ_MARFIL = "com.gonzalo.marfil"

    fun instalada(c: Context, paq: String) = runCatching { c.packageManager.getLaunchIntentForPackage(paq) != null }.getOrDefault(false)
    fun hayMarfil(c: Context) = instalada(c, PAQ_MARFIL)

    /** Abre Marfil en «Escritos» (el extra `go` lo entiende Marfil 0.12.0 o más nueva; una más vieja abre su portada). */
    fun leerEnMarfil(c: Context) {
        val i = c.packageManager.getLaunchIntentForPackage(PAQ_MARFIL) ?: return
        c.startActivity(i.putExtra("go", "escritos").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /** Cuántos capítulos de la obra están terminados (los que Marfil puede leer). */
    fun capitulosTerminados(e: Escritos, obraId: String): Int = e.capitulos(obraId).count { Compartir.hecho(e, it) }
}
