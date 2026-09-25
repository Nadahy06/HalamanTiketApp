package com.example.halamantiketapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                var hargaTiket by rememberSaveable { mutableStateOf(25000) }
                var jumlahTiket by rememberSaveable { mutableStateOf(1) }
                var namaPembeli by rememberSaveable { mutableStateOf("") }
                var status by rememberSaveable { mutableStateOf("Silakan pesan tiket") }
                var attemptTrigger by rememberSaveable { mutableStateOf(0) }

                val isProcessing = status == "Memproses pesanan........."

                LaunchedEffect(attemptTrigger) {
                    if (attemptTrigger > 0) {
                        if (namaPembeli.isBlank()) {
                            status = "Nama masih kosong"
                        } else {
                            status = "Memproses pesanan........."
                            delay(5000)
                            status = "Tiket telah dipesan"
                        }
                    }
                }

                TicketOrderScreen(
                    hargaTiket = hargaTiket,
                    jumlahTiket = jumlahTiket,
                    namaPembeli = namaPembeli,
                    status = status,
                    isProcessing = isProcessing,
                    onNamaChange = { namaPembeli = it },
                    onJumlahIncrease = { if (jumlahTiket < 10) jumlahTiket++ },
                    onJumlahDecrease = { if (jumlahTiket > 1) jumlahTiket-- },
                    onPesanClick = { attemptTrigger++ }
                )
            }
        }
    }
}

private val BgDark = Color(0xFF0B0E1A)
private val CardDark = Color(0xFF161B2C)
private val CardBorder = Color(0xFF2A3350)
private val Indigo = Color(0xFF4F46E5)
private val Violet = Color(0xFF7C3AED)
private val NeonCyan = Color(0xFF22D3EE)
private val NeonPurple = Color(0xFF8B5CF6)
private val NeonPink = Color(0xFFEC4899)
private val NeonGreen = Color(0xFF34F5B0)
private val Rose = Color(0xFFEF4444)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFF8B93A7)

private val NeonGradient = Brush.horizontalGradient(listOf(NeonCyan, NeonPurple, NeonPink))

fun formatRupiah(amount: Int): String {
    val localeID = Locale("in", "ID")
    val format = NumberFormat.getCurrencyInstance(localeID)
    format.maximumFractionDigits = 0
    return format.format(amount)
}

@Composable
fun TicketOrderScreen(
    hargaTiket: Int,
    jumlahTiket: Int,
    namaPembeli: String,
    status: String,
    isProcessing: Boolean,
    onNamaChange: (String) -> Unit,
    onJumlahIncrease: () -> Unit,
    onJumlahDecrease: () -> Unit,
    onPesanClick: () -> Unit
) {
    val total = hargaTiket * jumlahTiket

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        HeaderSection()

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            InfoCard(shadowColor = Indigo) {
                Text("Harga Tiket", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${formatRupiah(hargaTiket)} / tiket",
                    color = NeonCyan,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            InfoCard(shadowColor = Indigo) {
                Text("Nama Pembeli", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = namaPembeli,
                    onValueChange = onNamaChange,
                    enabled = !isProcessing,
                    placeholder = { Text("Masukkan nama Anda", color = TextSecondary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CardBorder,
                        cursorColor = NeonCyan
                    )
                )
            }

            InfoCard(shadowColor = Indigo) {
                Text("Jumlah Tiket", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepperButton(symbol = "−", enabled = !isProcessing && jumlahTiket > 1, onClick = onJumlahDecrease)
                    Text(
                        text = jumlahTiket.toString(),
                        modifier = Modifier
                            .padding(horizontal = 28.dp)
                            .animateContentSize(),
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    StepperButton(symbol = "+", enabled = !isProcessing && jumlahTiket < 10, onClick = onJumlahIncrease)
                }
            }

            InfoCard(shadowColor = NeonGreen) {
                Text("Total Bayar", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatRupiah(total),
                    color = NeonGreen,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.animateContentSize()
                )
                Text(
                    text = "${formatRupiah(hargaTiket)} x $jumlahTiket tiket",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = onPesanClick,
                enabled = !isProcessing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                elevation = null
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = if (isProcessing) Brush.horizontalGradient(listOf(CardBorder, CardBorder)) else NeonGradient,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isProcessing) "Memproses..." else "Pesan Tiket",
                        color = if (isProcessing) TextSecondary else BgDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            StatusBadge(status = status)

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(colors = listOf(Indigo, Violet)),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(x = (-30).dp, y = (-30).dp)
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
        )
        Box(
            modifier = Modifier
                .size(90.dp)
                .offset(x = 20.dp, y = 10.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_ticket),
                    contentDescription = "Logo tiket",
                    modifier = Modifier.size(28.dp),
                    colorFilter = ColorFilter.tint(NeonCyan)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Pemesanan Tiket",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Amankan tiketmu, pesan sekarang!",
                color = TextPrimary.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun InfoCard(shadowColor: Color, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = shadowColor.copy(alpha = 0.2f),
                spotColor = shadowColor.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(CardDark)
            .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun StepperButton(symbol: String, enabled: Boolean, onClick: () -> Unit) {
    val bg = if (enabled) Indigo else Color(0xFF2A3350)
    val fg = if (enabled) TextPrimary else Color(0xFF5A6379)
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(bg)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(symbol, color = fg, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bg, textColor, icon) = when (status) {
        "Nama masih kosong" -> Triple(Rose.copy(alpha = 0.15f), Rose, "⚠")
        "Memproses pesanan........." -> Triple(CardDark, NeonCyan, null)
        "Tiket telah dipesan" -> Triple(NeonGreen.copy(alpha = 0.15f), NeonGreen, "✓")
        else -> Triple(CardDark, TextSecondary, null)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (status == "Memproses pesanan.........") {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = NeonCyan
            )
            Spacer(modifier = Modifier.width(10.dp))
        } else if (icon != null) {
            Text(icon, color = textColor, fontSize = 15.sp)
            Spacer(modifier = Modifier.width(10.dp))
        }
        Text(
            text = "Status: $status",
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}