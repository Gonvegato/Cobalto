package com.gonzalo.cobalto

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

/** Lo que Cobalto publica para Azabache: los capítulos terminados (su texto de solo lectura) y las palabras de cada día. Sin red. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PublicarTest {
    private val ctx: Context get() = ApplicationProvider.getApplicationContext()
    private lateinit var e: Escritos

    @Before fun limpia() {
        Escritos.reiniciaParaPruebas()
        File(ctx.filesDir, "escritos").deleteRecursively()
        Cuenta.cargar(ctx)
        e = Escritos.get(ctx)
    }

    @Test fun soloLosCapitulosTerminadosYConSuParte() {
        val o = e.nuevo(Escritos.T_OBRA, "") { put("titulo", "La ciudad"); put("autor", "Yo") }
        val parte = e.nuevo(Escritos.T_PARTE, o.id) { put("titulo", "Primera parte"); put("hecho", 1) }
        val c1 = e.nuevo(Escritos.T_CAP, o.id) { put("titulo", "Uno"); put("aid", parte.id) }
        val c2 = e.nuevo(Escritos.T_CAP, o.id) { put("titulo", "Dos") }
        val c3 = e.nuevo(Escritos.T_CAP, o.id) { put("titulo", "Tres"); put("hecho", 1) }
        e.nuevo(Escritos.T_ESCENA, o.id) { put("cid", c1.id); put("titulo", "a"); put("texto", "Primera escena.") }
        e.nuevo(Escritos.T_ESCENA, o.id) { put("cid", c1.id); put("titulo", "b"); put("texto", "Segunda escena.") }
        e.nuevo(Escritos.T_ESCENA, o.id) { put("cid", c2.id); put("titulo", "c"); put("texto", "No terminado.") }
        e.nuevo(Escritos.T_ESCENA, o.id) { put("cid", c3.id); put("titulo", "d"); put("texto", "Tercero.") }
        val t = Publicar.capitulosTerminados(e)
        val caps = t[o.id]!!
        assertEquals(setOf(c1.id, c3.id), caps.keys)
        val d1 = caps[c1.id]!!
        assertEquals("La ciudad", d1.getString("t")); assertEquals("Yo", d1.getString("a")); assertEquals("Uno", d1.getString("c"))
        assertEquals("Primera parte", d1.getString("pa")); assertEquals(1, d1.getInt("n"))
        assertEquals("Primera escena.\n\n***\n\nSegunda escena.", d1.getString("x")); assertEquals(4, d1.getInt("p"))
        val d3 = caps[c3.id]!!
        assertEquals(3, d3.getInt("n")); assertFalse(d3.has("pa"))
        // Al quitar la marca, deja de publicarse
        e.edita(c3.id) { put("hecho", 0) }
        assertEquals(setOf(c1.id), Publicar.capitulosTerminados(e)[o.id]!!.keys)
    }

    @Test fun lasPalabrasDeCadaDiaSonSoloLasDeEsteDispositivo() {
        val o = e.nuevo(Escritos.T_OBRA, "") { put("titulo", "A") }
        val hoy = Escritos.hoy()
        e.registra(o.id, 70)
        e.registra(o.id, 30)
        e.pon(JSONObject().put("id", "l_${o.id}_${hoy - 1}").put("t", Escritos.T_LOG).put("pid", o.id).put("dia", hoy - 1).put("dev", JSONObject().put(Cuenta.dispositivo, 40).put("otro", 500)))
        val d = Publicar.palabrasPorDia(e)
        assertEquals(100, d[Publicar.fecha(hoy)]); assertEquals(40, d[Publicar.fecha(hoy - 1)])
        assertEquals(2, d.size)
        assertEquals(10, Publicar.fecha(hoy).length)
    }

    @Test fun laFirmaEsCortaYEstable() {
        assertEquals(16, Publicar.firma("hola").length)
        assertEquals(Publicar.firma("hola"), Publicar.firma("hola"))
        assertNotEquals(Publicar.firma("hola"), Publicar.firma("adiós"))
    }
}
