package cl.duoc.reservasem.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.theme.BadgeAprobadaCamuflaje
import cl.duoc.reservasem.ui.theme.BadgeRechazadaOxido
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.ui.theme.VibrantMilitaryGreen

/**
 * IE 2.2.1: Componente de entrada de texto táctico estandarizado.
 * Se enlaza reactivamente al UiState de cada ViewModel mostrando retroalimentación visual
 * consistente ante estados de error (borde rojo + ícono de alerta + supportingText) y
 * estados válidos (borde dorado/verde + ícono de verificación cuando contiene texto válido).
 */
@Composable
fun CampoTextoTactico(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector? = null,
    errorMensaje: String? = null,
    textoAyudaValido: String? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
) {
    val tieneError = errorMensaje != null
    val esValido = !tieneError && valor.trim().isNotEmpty()

    val iconTint by animateColorAsState(
        targetValue = when {
            tieneError -> MaterialTheme.colorScheme.error
            esValido -> VibrantMilitaryGreen
            else -> MilitaryGoldGalon
        },
        animationSpec = tween(250),
        label = "campoIconTint"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            enabled = enabled,
            label = {
                Text(
                    text = etiqueta,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            },
            leadingIcon = if (icono != null) {
                {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else null,
            trailingIcon = {
                when {
                    tieneError -> Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = "Error de validación",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                    esValido -> Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Campo válido",
                        tint = VibrantMilitaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
            },
            isError = tieneError,
            supportingText = if (tieneError) {
                {
                    Text(
                        text = errorMensaje ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (esValido && !textoAyudaValido.isNullOrBlank()) {
                {
                    Text(
                        text = textoAyudaValido,
                        color = VibrantMilitaryGreen,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else null,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(8.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (esValido) VibrantMilitaryGreen else MilitaryGoldGalon,
                unfocusedBorderColor = if (esValido) VibrantMilitaryGreen.copy(alpha = 0.65f)
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.55f),
                focusedLabelColor = if (esValido) VibrantMilitaryGreen else MilitaryGoldGalon,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Banner estandarizado de retroalimentación de validación de formulario.
 * Informa al usuario de forma visual e inmediata si todos los campos cumplen las reglas o cuántos errores restan.
 */
@Composable
fun BannerEstadoValidacion(
    erroresActivos: List<String?>,
    mensajeListo: String = "Formulario validado y listo para envío oficial",
    modifier: Modifier = Modifier
) {
    val listaErrores = erroresActivos.filterNotNull()
    val tieneErrores = listaErrores.isNotEmpty()

    AnimatedVisibility(visible = true, modifier = modifier.fillMaxWidth()) {
        val bgColor by animateColorAsState(
            targetValue = if (tieneErrores) BadgeRechazadaOxido.copy(alpha = 0.14f)
            else BadgeAprobadaCamuflaje.copy(alpha = 0.16f),
            animationSpec = tween(300),
            label = "bannerValBg"
        )
        val borderColor by animateColorAsState(
            targetValue = if (tieneErrores) BadgeRechazadaOxido.copy(alpha = 0.7f)
            else VibrantMilitaryGreen.copy(alpha = 0.75f),
            animationSpec = tween(300),
            label = "bannerValBorder"
        )

        Surface(
            color = bgColor,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (tieneErrores) Icons.Default.ErrorOutline else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (tieneErrores) MaterialTheme.colorScheme.error else VibrantMilitaryGreen,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (tieneErrores) {
                        "Revise ${listaErrores.size} campo(s) con observaciones antes de continuar."
                    } else {
                        "✔ $mensajeListo"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (tieneErrores) MaterialTheme.colorScheme.error else VibrantMilitaryGreen
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorTactico(
    opciones: List<String>,
    seleccionActual: String,
    onSeleccionChange: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = it },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = seleccionActual,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = etiqueta,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
            },
            shape = RoundedCornerShape(8.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MilitaryGoldGalon,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f),
                focusedLabelColor = MilitaryGoldGalon
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false },
            modifier = Modifier
                .exposedDropdownSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = opcion,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    onClick = {
                        onSeleccionChange(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}
