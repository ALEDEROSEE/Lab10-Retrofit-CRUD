package com.tecsup.lab10.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tecsup.lab10.data.SerieApiService
import com.tecsup.lab10.data.SerieModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ContenidoSeriesListado(navController: NavHostController, servicio: SerieApiService) {
   var listaSeries: SnapshotStateList<SerieModel> = remember { mutableStateListOf() }
   var errorRed by remember { mutableStateOf<String?>(null) }
   LaunchedEffect(Unit) {
       // sin try/catch una excepcion de red sale del LaunchedEffect y tumba la app
       try {
           val listado = servicio.selectSeries()
           listado.forEach { listaSeries.add(it) }
       } catch (e: Exception) {
           Log.e("SERIE-LISTA", "No se pudo traer el listado", e)
           errorRed = e.message ?: e.javaClass.simpleName
       }
   }

   LazyColumn (
       // deja espacio al final para que el boton + no tape los iconos de la ultima fila
       contentPadding = PaddingValues(bottom = 88.dp)
   ){
       if (errorRed != null) {
           item {
               Text("No pude traer las series: $errorRed", color = Color.Red, modifier = Modifier.padding(16.dp))
           }
       }
       item {
           Row (
               modifier = Modifier.fillParentMaxWidth(),
               verticalAlignment = Alignment.CenterVertically
           ) {
               Text(
                   text = "ID",
                   fontSize = 20.sp,
                   fontWeight = FontWeight.Bold,
                   modifier = Modifier.weight(0.1f)
               )
               Text(
                   text = "SERIE",
                   fontSize = 18.sp,
                   fontWeight = FontWeight.Bold,
                   modifier = Modifier.weight(0.7f)
               )
               Text(
                   text = "Accion",
                   fontSize = 18.sp,
                   fontWeight = FontWeight.Bold,
                   modifier = Modifier.weight(0.2f)
               ) //, fontWeight = FontWeight.Bold)
           }
       }

       items(listaSeries) { item ->
           Row(
               modifier = Modifier.padding(start=8.dp).fillParentMaxWidth(),
               verticalAlignment = Alignment.CenterVertically
           ) {
               Text(text = "${item.id}", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier=Modifier.weight(0.1f))
               Text(text = item.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier=Modifier.weight(0.6f))
               IconButton(
                   onClick = {
                       navController.navigate("serieVer/${item.id}")
                       Log.e("SERIE-VER","ID = ${item.id}")
                   },
                   Modifier.weight(0.1f)
               ) {
                   Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Ver", modifier=Modifier.align(Alignment.CenterVertically))
               }
               IconButton(
                   onClick = {
                       navController.navigate("serieDel/${item.id}")
                       Log.e("SERIE-DEL","ID = ${item.id}")
                   },
                   Modifier.weight(0.1f)
               ) {
                   Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Ver", modifier=Modifier.align(Alignment.CenterVertically))
               }
           }
       }
   }
}

@Composable
fun ContenidoSerieEditar(navController: NavHostController, servicio: SerieApiService, pid: Int = 0 ) {
   var id by remember { mutableStateOf<Int>(pid) }
   var name by remember { mutableStateOf<String?>("") }
   var release_date by remember { mutableStateOf<String?>("") }
   var rating by remember { mutableStateOf<String?>("") }
   var category by remember { mutableStateOf<String?>("") }
   var grabando by remember { mutableStateOf(false) }
   var mensaje by remember { mutableStateOf<String?>(null) }
   val scope = rememberCoroutineScope()

   if (id != 0) {
       LaunchedEffect(Unit) {
           try {
               val objSerie = servicio.selectSerie(id.toString())
               delay(100)
               val s = objSerie.body()
               if (s == null) {
                   // el servidor responde 500 si la serie ya no existe (Serie.objects.get lanza DoesNotExist)
                   mensaje = "No se pudo leer la serie (el servidor respondio ${objSerie.code()})"
               } else {
                   name = s.name
                   release_date = s.release_date
                   rating = s.rating.toString()
                   category = s.category
               }
           } catch (e: Exception) {
               Log.e("SERIE-VER", "No se pudo leer la serie", e)
               mensaje = "No se pudo leer la serie: ${e.message}"
           }
       }
   }

   Column(
       modifier = Modifier
           .fillMaxSize()
           .padding(16.dp)
   ){
       // Spacer(Modifier.height(50.dp))
       TextField(
           value = id.toString(),
           onValueChange = { },
           label = { Text("ID (solo lectura)") },
           readOnly = true,
           singleLine = true
       )
       TextField(
           value = name!!,
           onValueChange = { name = it },
           label = { Text("Name: ") },
           singleLine = true
       )
       TextField(
           value = release_date!!,
           onValueChange = { release_date = it },
           label = { Text("Release Date:") },
           singleLine = true
       )
       TextField(
           value = rating!!,
           onValueChange = { rating = it },
           label = { Text("Rating:") },
           singleLine = true
       )
       TextField(
           value = category!!,
           onValueChange = { category = it },
           label = { Text("Category:") },
           singleLine = true
       )
       Button(
           enabled = !grabando,
           onClick = {
               // el rating tiene que ser un numero: con toInt() a secas un campo vacio tumba la app
               val calif = rating?.toIntOrNull()
               if (name.isNullOrBlank() || release_date.isNullOrBlank() || calif == null || category.isNullOrBlank()) {
                   mensaje = "Completa todos los campos (el rating es un numero)"
                   return@Button
               }
               grabando = true
               // la peticion sale desde el onClick, en un scope que sobrevive a la navegacion
               scope.launch {
                   try {
                       val objSerie = SerieModel(id, name!!, release_date!!, calif, category!!)
                       val r = if (id == 0) servicio.insertSerie(objSerie) else servicio.updateSerie(id.toString(), objSerie)
                       if (r.isSuccessful) navController.navigate("series") else mensaje = "El servidor respondio ${r.code()}"
                   } catch (e: Exception) {
                       Log.e("SERIE-GRABAR", "No se pudo grabar", e)
                       mensaje = "No se pudo grabar: ${e.message}"
                   }
                   grabando = false
               }
           }
       ) {
           Text(if (grabando) "Grabando..." else "Grabar", fontSize=16.sp)
       }
       if (mensaje != null) {
           Text(mensaje!!, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
       }
   }
}

@Composable
fun ContenidoSerieEliminar(navController: NavHostController, servicio: SerieApiService, id: Int) {
   var showDialog by remember { mutableStateOf(true) }
   var borrar by remember { mutableStateOf(false) }

   if (showDialog) {
       AlertDialog(
           onDismissRequest = { showDialog = false },
           title = { Text(text = "Confirmar Eliminación") },
           text = {  Text("¿Está seguro de eliminar la Serie?") },
           confirmButton = {
               Button(
                   onClick = {
                       showDialog = false
                       borrar = true
                   } ) {
                   Text("Aceptar")
               }
           },
           dismissButton = {
               Button( onClick = { showDialog = false } ) {
                   Text("Cancelar")
                   navController.navigate("series")
               }
           }
       )
   }
   if (borrar) {
       LaunchedEffect(Unit) {
           // val objSerie = servicio.selectSerie(id.toString())
           servicio.deleteSerie(id.toString())
           borrar = false
           navController.navigate("series")
       }
   }
}
