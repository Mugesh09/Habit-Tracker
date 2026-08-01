package com.example.habittracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittracker.ui.theme.BorderCol
import com.example.habittracker.ui.theme.Canvas
import com.example.habittracker.ui.theme.HoverStrong
import com.example.habittracker.ui.theme.Ink
import com.example.habittracker.ui.theme.InkFaint

@Composable
fun AddHabitSheet(onSave: (String, String) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("\uD83C\uDFAF") }
    var showPicker by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .navigationBarsPadding()
            .imePadding()
            .padding(bottom = 16.dp)
    ) {
        Text("New habit", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Ink)
        Spacer(Modifier.height(16.dp))

        Text("NAME", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = InkFaint, letterSpacing = 0.5.sp)
        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(HoverStrong)
                    .clickable { showPicker = !showPicker },
                contentAlignment = Alignment.Center
            ) { Text(emoji, fontSize = 22.sp) }

            Spacer(Modifier.width(10.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("e.g. Read for 20 minutes", color = InkFaint) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Ink,
                    unfocusedBorderColor = BorderCol,
                    focusedContainerColor = Canvas,
                    unfocusedContainerColor = Canvas,
                    cursorColor = Ink
                )
            )
        }

        if (showPicker) {
            Spacer(Modifier.height(10.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, BorderCol, RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                EMOJIS.chunked(8).forEach { rowEmojis ->
                    Row(Modifier.fillMaxWidth()) {
                        rowEmojis.forEach { e ->
                            Box(
                                Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { emoji = e; showPicker = false }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) { Text(e, fontSize = 20.sp) }
                        }
                        // pad short last row
                        repeat(8 - rowEmojis.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, BorderCol, RoundedCornerShape(8.dp))
                    .clickable { onCancel() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) { Text("Cancel", fontSize = 15.sp, color = InkFaint) }

            val enabled = name.isNotBlank()
            Box(
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (enabled) Ink else HoverStrong)
                    .clickable(enabled = enabled) { onSave(name.trim(), emoji) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Add habit", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = if (enabled) Color.White else InkFaint)
            }
        }
    }
}
