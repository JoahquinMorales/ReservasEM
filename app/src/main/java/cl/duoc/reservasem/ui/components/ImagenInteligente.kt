package cl.duoc.reservasem.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import java.io.File

/**
 * Componente modular de integración con hardware nativo (Cámara y Galería de Imágenes).
 * Gestiona permisos en tiempo de ejecución, FileProvider para URIs seguras y decodificación de mapas de bits.
 */
@Composable
fun ImagenInteligente(
    fotoUri: Uri?,
    onFotoCambiada: (Uri?) -> Unit
) {
    val context = LocalContext.current

    //region Gestión de Archivo Temporal y FileProvider
    // Generación de archivo efímero en cacheDir para evitar saturación de almacenamiento externo
    val archivoTemporal = remember {
        File.createTempFile("perfil_tactico_cadete_", ".jpg", context.cacheDir)
    }

    // Uso de FileProvider para evitar exposición de rutas físicas de archivo (FileUriExposedException)
    val uriTemporal = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            archivoTemporal
        )
    }
    //endregion

    //region Launchers Nativos y Contratos
    // Contrato para captura fotográfica en resolución completa mediante URI preasignada
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            onFotoCambiada(uriTemporal)
        }
    }

    // Solicitud reactiva de permiso peligroso en tiempo de ejecución
    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            camaraLauncher.launch(uriTemporal)
        }
        // TODO: Implementar diálogo explicativo (rationale) si el usuario rechaza el permiso de cámara
    }

    // Contrato para selección de contenido multimedia desde almacenamiento interno o SAF
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            onFotoCambiada(uri)
        }
    }
    //endregion

    //region Renderizado y Transformación Gráfica
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Contenedor circular con borde institucional táctico
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(BorderStroke(2.5.dp, MilitaryGoldGalon), CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            // Decodificación de imagen adaptativa según versión de API (ImageDecoder para API 28+)
            val bitmap = remember(fotoUri) {
                if (fotoUri == null) null
                else runCatching {
                    if (Build.VERSION.SDK_INT < 28) {
                        @Suppress("DEPRECATION")
                        MediaStore.Images.Media.getBitmap(context.contentResolver, fotoUri)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, fotoUri)
                        ImageDecoder.decodeBitmap(source)
                    }
                }.getOrNull()
            }

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Fotografía Táctica de Perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Sin foto táctica",
                    modifier = Modifier.size(64.dp),
                    tint = MilitaryGoldGalon
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Controles de acción directa sobre APIs nativas del dispositivo
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    val tienePermiso = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                    if (tienePermiso) {
                        camaraLauncher.launch(uriTemporal)
                    } else {
                        permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MilitaryGoldGalon),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CÁMARA NATIVA",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            OutlinedButton(
                onClick = { galeriaLauncher.launch("image/*") },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MilitaryGoldGalon),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = null,
                    tint = MilitaryGoldGalon,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ABRIR GALERÍA",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
    //endregion
}
