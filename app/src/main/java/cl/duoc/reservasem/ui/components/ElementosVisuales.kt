package cl.duoc.reservasem.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.ui.theme.BadgeAprobadaCamuflaje
import cl.duoc.reservasem.ui.theme.BadgePendienteAmbar
import cl.duoc.reservasem.ui.theme.BadgeRechazadaOxido
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon

@Composable
fun BadgeEstado(estado: String) {
    val bgColor by animateColorAsState(
        targetValue = when (estado.uppercase()) {
            "APROBADA", "OPERATIVO" -> BadgeAprobadaCamuflaje
            "RECHAZADA", "MANTENCIÓN", "MANTENCION" -> BadgeRechazadaOxido
            else -> BadgePendienteAmbar
        },
        animationSpec = tween(durationMillis = 350),
        label = "badgeEstadoColor"
    )
    val borderColor by animateColorAsState(
        targetValue = when (estado.uppercase()) {
            "APROBADA", "OPERATIVO" -> MilitaryGoldGalon
            "RECHAZADA", "MANTENCIÓN", "MANTENCION" -> BadgeRechazadaOxido.copy(alpha = 0.9f)
            else -> MilitaryGoldGalon.copy(alpha = 0.8f)
        },
        animationSpec = tween(durationMillis = 350),
        label = "badgeEstadoBorder"
    )

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color = Color.White, shape = CircleShape)
            )
            Text(
                text = estado.uppercase(),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
        }
    }
}

@Composable
fun KpiCardTactica(
    titulo: String,
    valor: String,
    icono: ImageVector,
    colorAcento: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    val animatedAccent by animateColorAsState(
        targetValue = colorAcento,
        animationSpec = tween(durationMillis = 350),
        label = "kpiAccentColor"
    )
    val shape = RoundedCornerShape(10.dp)

    OutlinedCard(
        modifier = modifier
            .shadow(
                elevation = 3.dp,
                shape = shape,
                ambientColor = MilitaryGoldGalon.copy(alpha = 0.2f),
                spotColor = MilitaryGoldGalon.copy(alpha = 0.3f)
            )
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
        shape = shape,
        border = BorderStroke(1.1.dp, MilitaryGoldGalon.copy(alpha = 0.65f)),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.93f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            animatedAccent.copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    )
                )
                .padding(horizontal = 11.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Surface(
                color = animatedAccent.copy(alpha = 0.18f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(0.8.dp, animatedAccent.copy(alpha = 0.6f)),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = animatedAccent,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = valor,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = titulo.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun EncabezadoSeccionTactico(
    titulo: String,
    subtitulo: String? = null,
    accionTexto: String? = null,
    onAccionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(20.dp)
                    .background(
                        color = MilitaryGoldGalon,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
            Column {
                Text(
                    text = titulo.uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!subtitulo.isNullOrBlank()) {
                    Text(
                        text = subtitulo,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        if (accionTexto != null && onAccionClick != null) {
            OutlinedButton(
                onClick = onAccionClick,
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.7f)),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                modifier = Modifier.height(28.dp)
            ) {
                Text(
                    text = accionTexto.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MilitaryGoldGalon
                )
            }
        }
    }
}
