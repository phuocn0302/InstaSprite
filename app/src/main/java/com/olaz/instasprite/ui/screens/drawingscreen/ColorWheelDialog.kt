package com.olaz.instasprite.ui.screens.drawingscreen

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.Shader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toRect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.graphics.Color as AndroidColor
import androidx.core.graphics.createBitmap
import com.olaz.instasprite.ui.theme.HomeScreenColor

@Composable
fun ColorPickerDialog(
    initialColor: Color = Color.Blue,
    onDismiss: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    val hsv = remember {
        val hsvArray = floatArrayOf(0f, 0f, 0f)
        AndroidColor.colorToHSV(initialColor.toArgb(), hsvArray)
        mutableStateOf(Triple(hsvArray[0], hsvArray[1], hsvArray[2]))
    }

    val selectedColor = remember(hsv.value) {
        mutableStateOf(
            Color.hsv(
                hsv.value.first,
                hsv.value.second,
                hsv.value.third
            )
        )
    }

    val redValue = remember { mutableStateOf((selectedColor.value.red * 255).toInt().toString()) }
    val greenValue =
        remember { mutableStateOf((selectedColor.value.green * 255).toInt().toString()) }
    val blueValue = remember { mutableStateOf((selectedColor.value.blue * 255).toInt().toString()) }

    val hexValue = remember {
        mutableStateOf(String.format("%06X", (selectedColor.value.toArgb() and 0xFFFFFF)))
    }

    fun updateInputFields() {
        redValue.value = (selectedColor.value.red * 255).toInt().toString()
        greenValue.value = (selectedColor.value.green * 255).toInt().toString()
        blueValue.value = (selectedColor.value.blue * 255).toInt().toString()
        hexValue.value = String.format("%06X", (selectedColor.value.toArgb() and 0xFFFFFF))
    }

    fun updateColorFromRGB() {
        try {
            val r = redValue.value.toIntOrNull()?.coerceIn(0, 255) ?: return
            val g = greenValue.value.toIntOrNull()?.coerceIn(0, 255) ?: return
            val b = blueValue.value.toIntOrNull()?.coerceIn(0, 255) ?: return

            val color = Color(r / 255f, g / 255f, b / 255f)
            val hsvArray = floatArrayOf(0f, 0f, 0f)
            AndroidColor.colorToHSV(color.toArgb(), hsvArray)
            hsv.value = Triple(hsvArray[0], hsvArray[1], hsvArray[2])
        } catch (e: Exception) {
        }
    }

    fun updateColorFromHex() {
        try {
            val cleanHex = hexValue.value.removePrefix("#")
            if (cleanHex.length == 6) {
                val colorInt = cleanHex.toLong(16).toInt()
                val color = Color(colorInt or 0xFF000000.toInt())
                val hsvArray = floatArrayOf(0f, 0f, 0f)
                AndroidColor.colorToHSV(color.toArgb(), hsvArray)
                hsv.value = Triple(hsvArray[0], hsvArray[1], hsvArray[2])
            }
        } catch (e: Exception) {
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .width(380.dp)
                .background(
                    color = HomeScreenColor.BackgroundColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Select Color",
                    fontSize = 18.sp,
                    color = Color.White
                )

                SatValPanel(
                    hue = hsv.value.first,
                    saturation = hsv.value.second,
                    value = hsv.value.third
                ) { sat, value ->
                    hsv.value = Triple(hsv.value.first, sat, value)
                    updateInputFields()
                }

                Row {
                    HueBar(
                        hue = hsv.value.first
                    ) { hue ->
                        hsv.value = Triple(hue, hsv.value.second, hsv.value.third)
                        updateInputFields()
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .border(
                                width = 2.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                color = selectedColor.value,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = hexValue.value,
                        onValueChange = { newText ->
                            val filtered = newText.uppercase().filter { it in "0123456789ABCDEF" }
                            hexValue.value = filtered.take(6)
                            updateColorFromHex()
                        },
                        label = { Text("Hex") },
                        placeholder = {
                            Text(
                                text = "FFFFFF",
                                color = Color.White
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.White,
                        ),
                        modifier = Modifier.weight(.4f)
                    )
                    OutlinedTextField(
                        value = redValue.value,
                        onValueChange = { newText ->
                            val filtered = newText.filter { it in "0123456789" }
                            if (filtered.isEmpty()) {
                                redValue.value = ""
                            } else {
                                val number = filtered.toInt()
                                redValue.value = when {
                                    number <= 255 -> filtered
                                    else -> "255"
                                }
                            }
                            updateColorFromRGB()
                        },
                        label = { Text("R") },
                        placeholder = {
                            Text(
                                text = "0",
                                color = Color.White
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.White,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(.2f)
                    )
                    OutlinedTextField(
                        value = greenValue.value,
                        onValueChange = { newText ->
                            val filtered = newText.filter { it in "0123456789" }
                            if (filtered.isEmpty()) {
                                greenValue.value = ""
                            } else {
                                val number = filtered.toInt()
                                greenValue.value = when {
                                    number <= 255 -> filtered
                                    else -> "255"
                                }
                            }
                            updateColorFromRGB()
                        },
                        label = { Text("G") },
                        placeholder = {
                            Text(
                                text = "0",
                                color = Color.White
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.White,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(.2f)
                    )
                    OutlinedTextField(
                        value = blueValue.value,
                        onValueChange = { newText ->
                            val filtered = newText.filter { it in "0123456789" }
                            if (filtered.isEmpty()) {
                                blueValue.value = ""
                            } else {
                                val number = filtered.toInt()
                                blueValue.value = when {
                                    number <= 255 -> filtered
                                    else -> "255"
                                }
                            }
                            updateColorFromRGB()
                        },
                        label = { Text("B") },
                        placeholder = {
                            Text(
                                text = "0",
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.White,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(.2f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onColorSelected(selectedColor.value)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HomeScreenColor.ButtonColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .width(120.dp)
                    ) {
                        Text("Select")
                    }

                    Spacer(modifier = Modifier.width(60.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HomeScreenColor.ButtonColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .width(120.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
fun HueBar(
    hue: Float,
    setColor: (Float) -> Unit
) {
    val scope = rememberCoroutineScope()
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val pressOffset = remember {
        mutableStateOf(Offset.Zero)
    }

    Canvas(
        modifier = Modifier
            .height(40.dp)
            .width(280.dp)
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(50)
            )
            .clip(RoundedCornerShape(50))
            .emitDragGesture(interactionSource)
    ) {
        val drawScopeSize = size
        val bitmap = createBitmap(size.width.toInt(), size.height.toInt())
        val hueCanvas = Canvas(bitmap)

        val huePanel = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())

        val hueColors = IntArray((huePanel.width()).toInt())
        var hueStep = 0f
        for (i in hueColors.indices) {
            hueColors[i] = AndroidColor.HSVToColor(floatArrayOf(hueStep, 1f, 1f))
            hueStep += 360f / hueColors.size
        }

        val linePaint = Paint()
        linePaint.strokeWidth = 0F
        for (i in hueColors.indices) {
            linePaint.color = hueColors[i]
            hueCanvas.drawLine(i.toFloat(), 0F, i.toFloat(), huePanel.bottom, linePaint)
        }

        drawBitmap(
            bitmap = bitmap,
            panel = huePanel
        )

        fun pointToHue(pointX: Float): Float {
            val width = huePanel.width()
            val x = when {
                pointX < huePanel.left -> 0F
                pointX > huePanel.right -> width
                else -> pointX - huePanel.left
            }
            return x * 360f / width
        }

        fun hueToPoint(hue: Float): Float {
            val width = huePanel.width()
            return (hue / 360f) * width
        }

        val indicatorX = hueToPoint(hue)
        pressOffset.value = Offset(indicatorX, size.height / 2)

        scope.collectForPress(interactionSource) { pressPosition ->
            val pressPos = pressPosition.x.coerceIn(0f..drawScopeSize.width)
            pressOffset.value = Offset(pressPos, size.height / 2)
            val selectedHue = pointToHue(pressPos)
            setColor(selectedHue)
        }

        drawCircle(
            Color.White,
            radius = 38.dp.toPx() / 2,
            center = pressOffset.value,
            style = Stroke(
                width = 2.dp.toPx()
            )
        )
    }
}

@Composable
fun SatValPanel(
    hue: Float,
    saturation: Float,
    value: Float,
    setSatVal: (Float, Float) -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val scope = rememberCoroutineScope()
    var sat: Float
    var brightness: Float

    val pressOffset = remember {
        mutableStateOf(Offset.Zero)
    }

    Canvas(
        modifier = Modifier
            .size(280.dp)
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .emitDragGesture(interactionSource)
            .clip(RoundedCornerShape(12.dp))
    ) {
        val cornerRadius = 12.dp.toPx()
        val satValSize = size

        val bitmap = createBitmap(size.width.toInt(), size.height.toInt())
        val canvas = Canvas(bitmap)
        val satValPanel = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())

        val rgb = AndroidColor.HSVToColor(floatArrayOf(hue, 1f, 1f))

        val satShader = LinearGradient(
            satValPanel.left, satValPanel.top, satValPanel.right, satValPanel.top,
            -0x1, rgb, Shader.TileMode.CLAMP
        )
        val valShader = LinearGradient(
            satValPanel.left, satValPanel.top, satValPanel.left, satValPanel.bottom,
            -0x1, -0x1000000, Shader.TileMode.CLAMP
        )

        canvas.drawRoundRect(
            satValPanel,
            cornerRadius,
            cornerRadius,
            Paint().apply {
                shader = ComposeShader(
                    valShader,
                    satShader,
                    PorterDuff.Mode.MULTIPLY
                )
            }
        )

        drawBitmap(
            bitmap = bitmap,
            panel = satValPanel
        )

        fun pointToSatVal(pointX: Float, pointY: Float): Pair<Float, Float> {
            val width = satValPanel.width()
            val height = satValPanel.height()

            val x = when {
                pointX < satValPanel.left -> 0f
                pointX > satValPanel.right -> width
                else -> pointX - satValPanel.left
            }

            val y = when {
                pointY < satValPanel.top -> 0f
                pointY > satValPanel.bottom -> height
                else -> pointY - satValPanel.top
            }

            val satPoint = 1f / width * x
            val valuePoint = 1f - 1f / height * y

            return satPoint to valuePoint
        }

        fun satValToPoint(saturation: Float, brightness: Float): Offset {
            val width = satValPanel.width()
            val height = satValPanel.height()

            val x = saturation * width
            val y = (1f - brightness) * height

            return Offset(x, y)
        }

        val indicatorPosition = satValToPoint(saturation, value)
        pressOffset.value = indicatorPosition

        scope.collectForPress(interactionSource) { pressPosition ->
            val pressPositionOffset = Offset(
                pressPosition.x.coerceIn(0f..satValSize.width),
                pressPosition.y.coerceIn(0f..satValSize.height)
            )

            pressOffset.value = pressPositionOffset
            val (satPoint, valuePoint) = pointToSatVal(pressPositionOffset.x, pressPositionOffset.y)
            sat = satPoint
            brightness = valuePoint

            setSatVal(sat, brightness)
        }

        drawCircle(
            color = Color.White,
            radius = 8.dp.toPx(),
            center = pressOffset.value,
            style = Stroke(
                width = 2.dp.toPx()
            )
        )

        drawCircle(
            color = Color.White,
            radius = 2.dp.toPx(),
            center = pressOffset.value,
        )
    }
}


fun CoroutineScope.collectForPress(
    interactionSource: InteractionSource,
    setOffset: (Offset) -> Unit
) {
    launch {
        interactionSource.interactions.collect { interaction ->
            (interaction as? PressInteraction.Press)
                ?.pressPosition
                ?.let(setOffset)
        }
    }
}

private fun Modifier.emitDragGesture(
    interactionSource: MutableInteractionSource
): Modifier = composed {
    val scope = rememberCoroutineScope()

    pointerInput(Unit) {
        detectDragGestures { input, _ ->
            scope.launch {
                interactionSource.emit(PressInteraction.Press(input.position))
            }
        }
    }.clickable(interactionSource, null) {

    }
}

private fun DrawScope.drawBitmap(
    bitmap: Bitmap,
    panel: RectF
) {
    drawIntoCanvas {
        it.nativeCanvas.drawBitmap(
            bitmap,
            null,
            panel.toRect(),
            null
        )
    }
}
