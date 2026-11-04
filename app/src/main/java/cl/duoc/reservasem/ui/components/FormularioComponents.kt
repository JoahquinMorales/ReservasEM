package cl.duoc.reservasem.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon

@Composable
fun CampoTextoTactico(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector? = null,
    errorMensaje: String? = null,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
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
                        tint = MilitaryGoldGalon,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else null,
            isError = errorMensaje != null,
            supportingText = if (errorMensaje != null) {
                {
                    Text(
                        text = errorMensaje,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else null,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(8.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MilitaryGoldGalon,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f),
                focusedLabelColor = MilitaryGoldGalon
            ),
            modifier = Modifier.fillMaxWidth()
        )
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
                .exposedDropdownSize() // Esto iguala el ancho exacto
                .background(MaterialTheme.colorScheme.surface),
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = opcion,
                            style = MaterialTheme.typography.bodyMedium
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