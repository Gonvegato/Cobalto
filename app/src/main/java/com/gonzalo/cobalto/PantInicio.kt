package com.gonzalo.cobalto

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * La portada de Cobalto. La cabecera y el menú, con las mismas medidas que los de Atrio, Marfil y Carmesí; lo de debajo, propio:
 * «Ahora» (la obra en la que estás, enmarcada, con las palabras de hoy, la escena por la que vas y un sprint a un toque) y
 * «Sobre la mesa» (las demás obras en marcha). Las barras de arriba cuentan palabras.
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun PantInicio(alAbrir: (String) -> Unit, alSeguir: (String, String?) -> Unit, alSprint: (String) -> Unit, alNueva: () -> Unit, alIr: (Destino) -> Unit, alPareja: () -> Unit) {
    val ctx = LocalContext.current
    val e = remember { Escritos.get(ctx) }
    val ver = e.version
    val hoy = remember { Calendar.getInstance() }
    val ahora = remember(ver) { e.obraActual() }
    val mesa = remember(ver) { e.obrasDe(EstadoObra.ESCRIBIENDO).filter { it.id != ahora?.id }.take(6) }
    val pausa = remember(ver) { e.obrasDe(EstadoObra.PAUSA).take(4) }
    val todas = remember(ver) { e.obras() }
    val secciones = listOf<Pair<String, () -> Unit>>(
        "Escribiendo" to { alIr(Destino.Escribiendo) }, "En pausa" to { alIr(Destino.EnPausa) },
        "Terminadas" to { alIr(Destino.Terminadas) }, "Archivo" to { alIr(Destino.Archivo) }
    )

    Column(Modifier.fillMaxSize().background(Paper).verticalScroll(rememberScrollState()).statusBarsPadding().padding(horizontal = 28.dp)) {
        Row(Modifier.fillMaxWidth().padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            EnlaceRevista("+ Nueva obra", acento = true) { alNueva() }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Filled.Settings, "Ajustes", tint = Charcoal, modifier = Modifier.padding(start = 6.dp).combinedClickable(onClick = { alIr(Destino.Ajustes) }).padding(8.dp).size(22.dp))
        }
        val ancho = minOf(androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp, 1080)
        Cabecera(if (ancho >= 700) 132.sp else 92.sp, Modifier.padding(top = 8.dp))
        FileteCabecera(3.dp)
        Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
            Text("Nº ${hoy.get(Calendar.DAY_OF_YEAR)}", color = Charcoal, fontFamily = Body, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(Modifier.weight(1f))
            Text(SimpleDateFormat("EEEE d 'de' MMMM", Locale("es", "ES")).format(hoy.time).uppercase(), color = Charcoal, fontFamily = Body, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
        Filete(1.dp)
        BarrasEscritura(e, ver)
        Column(Modifier.fillMaxWidth().padding(top = 18.dp)) {
            Filete(1.dp)
            FlowRow(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                secciones.forEach { (t, f) ->
                    Text(t.uppercase(), color = Charcoal, fontFamily = Body, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp, maxLines = 1,
                        modifier = Modifier.clickable(onClick = f).padding(vertical = 9.dp))
                }
            }
            Filete(1.dp)
        }
        Spacer(Modifier.height(18.dp))

        if (todas.isEmpty()) {
            Text("Aquí empieza tu novela. Crea una obra y escribe por capítulos y escenas, con fichas de personajes y lugares, notas, borradores e historial. Se guarda sola, funciona sin conexión y, con tu cuenta de Atrio, aparece en el PC y en tus otros móviles.",
                color = Muted, fontFamily = Body, fontSize = 15.sp, lineHeight = 22.sp)
            Spacer(Modifier.height(6.dp))
            MagLink("Nueva obra", "Con un capítulo y una escena para empezar ya", accent = true) { alNueva() }
        }

        // ---- Ahora: la obra en la que estás, enmarcada
        if (ahora != null) {
            val palabras = remember(ver, ahora.id) { e.palabrasObra(ahora.id) }
            val hoyObra = remember(ver, ahora.id) { e.escritas(Escritos.hoy(), ahora.id) }
            val escena = remember(ver, ahora.id) { e.ultimaEscenaDe(ahora.id) }
            val meta = ahora.i("meta"); val diaria = ahora.i("diaria")
            val color = colorEscrito(ahora.s("color"))
            Column(Modifier.fillMaxWidth().border(1.5.dp, Charcoal).clickable { alAbrir(ahora.id) }.padding(20.dp)) {
                Antetitulo("Ahora", Modifier.padding(bottom = 12.dp))
                Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    Box(Modifier.width(10.dp).fillMaxHeight().background(color))
                    Column(Modifier.weight(1f).padding(start = 16.dp)) {
                        FitTitle(ahora.s("titulo").ifBlank { "Sin título" }, 32.sp, 34.sp, maxLines = 3, minSize = 16f, letterSpacing = (-0.5).sp)
                        val sub = listOf(ahora.s("autor"), ahora.s("genero")).filter { it.isNotBlank() }.joinToString(" · ")
                        if (sub.isNotBlank()) Text(sub, color = Muted, fontFamily = Display, fontStyle = FontStyle.Italic, fontSize = 16.sp, modifier = Modifier.padding(top = 2.dp))
                        Row(Modifier.padding(top = 14.dp), verticalAlignment = Alignment.Bottom) {
                            Text(cifraE(hoyObra), color = if (diaria > 0 && hoyObra >= diaria) Acento else Charcoal, fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 52.sp, lineHeight = 52.sp)
                            Column(Modifier.padding(start = 10.dp, bottom = 8.dp)) {
                                Text(if (diaria > 0) "HOY · DE ${cifraE(diaria)}" else "PALABRAS HOY", color = Muted, fontFamily = Body, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.2.sp)
                            }
                        }
                        if (meta > 0) {
                            Bar((palabras.toFloat() / meta).coerceIn(0f, 1f), Acento, Modifier.padding(top = 6.dp), height = 3.dp)
                            Text("${cifraE(palabras)} de ${cifraE(meta)} palabras · ${(palabras * 100L / meta).coerceAtMost(999)} %".uppercase(), color = Muted, fontFamily = Body, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, modifier = Modifier.padding(top = 6.dp))
                        } else Text("${cifraE(palabras)} palabras · ${e.cuentaObra(ahora.id)}".uppercase(), color = Muted, fontFamily = Body, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, modifier = Modifier.padding(top = 6.dp))
                    }
                }
                if (escena != null) {
                    Spacer(Modifier.height(14.dp))
                    Filete(1.dp, Line)
                    val linea = escena.s("texto").lineSequence().map { it.trim() }.lastOrNull { it.isNotBlank() && !it.startsWith("#") && it != "***" }.orEmpty()
                    Text(escena.s("titulo").ifBlank { "Sin título" }, color = Charcoal, fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 10.dp))
                    if (linea.isNotEmpty()) Text("…" + linea.takeLast(140), color = Muted, fontFamily = Display, fontStyle = FontStyle.Italic, fontSize = 15.sp, lineHeight = 20.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                    Text("SEGUIR ESCRIBIENDO →", color = Acento, fontFamily = Body, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp,
                        modifier = Modifier.clickable { alSeguir(ahora.id, escena?.id) }.padding(vertical = 8.dp))
                    Text("SPRINT →", color = Charcoal, fontFamily = Body, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp,
                        modifier = Modifier.clickable { alSprint(ahora.id) }.padding(vertical = 8.dp))
                }
            }
        }

        TeComparte(alPareja)

        // ---- Sobre la mesa: las demás obras en marcha
        if (mesa.isNotEmpty()) {
            Spacer(Modifier.height(30.dp))
            Antetitulo("Sobre la mesa", Modifier.padding(bottom = 6.dp))
            Filete(1.dp)
            mesa.forEach { o -> FilaObra(e, o, alAbrir) }
            Text("VER LAS QUE ESCRIBES →", color = Acento, fontFamily = Body, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp,
                modifier = Modifier.clickable { alIr(Destino.Escribiendo) }.padding(vertical = 12.dp))
        }

        // ---- En el cajón: lo que está en pausa
        if (pausa.isNotEmpty()) {
            Spacer(Modifier.height(30.dp))
            Antetitulo("En el cajón", Modifier.padding(bottom = 6.dp))
            Filete(1.dp)
            pausa.forEach { o -> FilaObra(e, o, alAbrir) }
            Text("VER LAS QUE ESPERAN →", color = Acento, fontFamily = Body, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp,
                modifier = Modifier.clickable { alIr(Destino.EnPausa) }.padding(vertical = 12.dp))
        }

        Text("Cobalto · hecho en casa", color = Muted, fontFamily = Display, fontStyle = FontStyle.Italic, fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 18.dp))
        Spacer(Modifier.height(24.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()))
    }
}

/**
 * Entre la fecha y el menú, como en las portadas de Atrio y Marfil: lo escrito hoy (y tu objetivo, si tienes), los días seguidos y las
 * dos últimas semanas en barras (cada día, sus palabras; la raya fina es tu objetivo).
 */
@Composable
fun BarrasEscritura(e: Escritos, ver: Int) {
    val r = remember(ver) { e.resumen(14) }
    val algo = r.dias.any { it > 0 }
    if (!algo && r.diaria <= 0 && r.racha < 2) return
    Column(Modifier.fillMaxWidth().padding(top = 14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).background(Acento)); Spacer(Modifier.width(8.dp))
            Text(
                listOfNotNull(
                    "ESCRITURA",
                    when {
                        r.diaria > 0 && r.hoy >= r.diaria -> "OBJETIVO CUMPLIDO · ${cifraE(r.hoy)} PALABRAS HOY"
                        r.diaria > 0 -> "${cifraE(r.hoy)} DE ${cifraE(r.diaria)} PALABRAS HOY"
                        r.hoy > 0 -> "${cifraE(r.hoy)} PALABRAS HOY"
                        else -> null
                    },
                    if (r.racha >= 2) "${r.racha} DÍAS SEGUIDOS" else null
                ).joinToString(" · "),
                fontFamily = Body, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp,
                color = if (r.diaria > 0 && r.hoy >= r.diaria) Acento else Charcoal, maxLines = 2
            )
        }
        if (algo) Barras(r.dias, Modifier.fillMaxWidth().padding(top = 10.dp).height(34.dp), meta = r.diaria)
    }
}
