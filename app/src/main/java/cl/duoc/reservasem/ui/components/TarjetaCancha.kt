package cl.duoc.reservasem.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.reservasem.model.CanchaModel
import cl.duoc.reservasem.ui.theme.BadgeAprobadaCamuflaje
import cl.duoc.reservasem.ui.theme.BadgeRechazadaOxido
import cl.duoc.reservasem.ui.theme.MilitaryGoldGalon
import cl.duoc.reservasem.ui.theme.VibrantMilitaryGreen

@Composable
fun TarjetaCancha(
    cancha: CanchaModel,
    onReservarClick: (CanchaModel) -> Unit,
    onVerFichaClick: ((CanchaModel) -> Unit)? = null
) {
    val containerColor by animateColorAsState(
        targetValue = if (cancha.enMantencion) {
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.92f)
        } else {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
        },
        animationSpec = tween(durationMillis = 350),
        label = "canchaContainerColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (cancha.enMantencion) {
            BadgeRechazadaOxido.copy(alpha = 0.85f)
        } else {
            MilitaryGoldGalon.copy(alpha = 0.75f)
        },
        animationSpec = tween(durationMillis = 350),
        label = "canchaBorderColor"
    )

    val badgeAccentColor by animateColorAsState(
        targetValue = if (cancha.enMantencion) {
            BadgeRechazadaOxido
        } else {
            VibrantMilitaryGreen
        },
        animationSpec = tween(durationMillis = 350),
        label = "canchaBadgeAccentColor"
    )

    val elevation by animateDpAsState(
        targetValue = if (cancha.enMantencion) 2.dp else 5.dp,
        label = "canchaElevation"
    )

    val shape = RoundedCornerShape(10.dp)

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = MilitaryGoldGalon.copy(alpha = 0.25f),
                spotColor = MilitaryGoldGalon.copy(alpha = 0.35f)
            )
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
            .then(
                if (onVerFichaClick != null) {
                    Modifier.clickable { onVerFichaClick(cancha) }
                } else {
                    Modifier
                }
            ),
        shape = shape,
        border = BorderStroke(1.2.dp, borderColor),
        colors = CardDefaults.outlinedCardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MilitaryGoldGalon.copy(alpha = 0.06f),
                            Color.Transparent
                        )
                    )
                )
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cancha.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(0.8.dp, MilitaryGoldGalon.copy(alpha = 0.55f))
                        ) {
                            Text(
                                text = cancha.categoria.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MilitaryGoldGalon
                            )
                            Text(
                                text = "AFORO MÁX: ${cancha.capacidad}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    color = badgeAccentColor.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, badgeAccentColor)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (cancha.enMantencion) Icons.Default.Build else Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = if (cancha.enMantencion) BadgeRechazadaOxido else BadgeAprobadaCamuflaje
                        )
                        Text(
                            text = if (cancha.enMantencion) "MANTENCIÓN" else "OPERATIVO",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (cancha.enMantencion) BadgeRechazadaOxido else BadgeAprobadaCamuflaje
                        )
                    }
                }
            }

            if (cancha.enMantencion) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = BadgeRechazadaOxido.copy(alpha = 0.14f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(0.8.dp, BadgeRechazadaOxido.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠ BLOQUEO OPERATIVO: ${cancha.motivoMantencion.ifBlank { "Mantención técnica programada" }}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(
                    thickness = 0.6.dp,
                    color = MilitaryGoldGalon.copy(alpha = 0.28f)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onVerFichaClick != null) {
                        OutlinedButton(
                            onClick = { onVerFichaClick(cancha) },
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, MilitaryGoldGalon.copy(alpha = 0.7f)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = MilitaryGoldGalon
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "FICHA TÉCNICA",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Button(
                        onClick = { onReservarClick(cancha) },
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, MilitaryGoldGalon),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = "SOLICITAR RESERVA",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
