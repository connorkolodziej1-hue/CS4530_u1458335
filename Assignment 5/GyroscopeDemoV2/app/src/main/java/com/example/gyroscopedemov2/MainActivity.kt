package com.example.gyroscopedemov2

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gyroscopedemov2.ui.theme.GyroscopeDemoV2Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    private val viewModel: GravityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GyroscopeDemoV2Theme {
                GravitySensorScreen(viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.registerSensor()
    }

    override fun onPause() {
        super.onPause()
        viewModel.unregisterSensor()
    }
}

data class GravityReading(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f)

//View Model put in main activity for simplicity
class GravityViewModel(application: Application) : AndroidViewModel(application) {

    private val sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val gravitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        ?: sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val _gravityReading = MutableStateFlow(GravityReading())
    val gravityReading: StateFlow<GravityReading> = _gravityReading.asStateFlow()

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            _gravityReading.value = GravityReading(event.values[0], event.values[1], event.values[2])
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }

    fun registerSensor() {
        gravitySensor?.let { sensor ->
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun unregisterSensor() {
        sensorManager.unregisterListener(listener)
    }

    override fun onCleared() {
        unregisterSensor()
    }
}

@Composable
fun GravitySensorScreen(
    viewModel: GravityViewModel
) {
    if (viewModel.gravitySensor == null) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center) {
                Text("No gravity sensor available on this device", fontSize = 18.sp)
            }
        }
        return
    }

    val gravityReading by viewModel.gravityReading.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Gravity reading:\nX: %.2f  Y: %.2f  Z: %.2f".format(
                    gravityReading.x,
                    gravityReading.y,
                    gravityReading.z
                ),
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray.copy(alpha = 0.2f), shape = MaterialTheme.shapes.medium)
            ) {
                val density = LocalDensity.current
                val ballSizePx = with(density) { 40.dp.toPx() }
                val maxX = (constraints.maxWidth.toFloat() - ballSizePx).coerceAtLeast(0f)
                val maxY = (constraints.maxHeight.toFloat() - ballSizePx).coerceAtLeast(0f)

                var positionX by remember { mutableFloatStateOf(0f) }
                var positionY by remember { mutableFloatStateOf(0f) }
                var velocityX by remember { mutableFloatStateOf(0f) }
                var velocityY by remember { mutableFloatStateOf(0f) }
                var isInitialized by remember { mutableStateOf(false) }

                // Center the ball initially
                if (!isInitialized && maxX > 0f && maxY > 0f) {
                    positionX = maxX / 2f
                    positionY = maxY / 2f
                    isInitialized = true
                }

                // Smooth physics animation driven by device tilt
                //AI Used for help with frame timing code
                LaunchedEffect(isInitialized) {
                    if (!isInitialized) return@LaunchedEffect
                    var lastFrameTimeNanos = 0L

                    while (true) {
                        withFrameNanos { frameTimeNanos ->
                            if (lastFrameTimeNanos != 0L) {
                                val dt = ((frameTimeNanos - lastFrameTimeNanos) / 1_000_000_000f)
                                    .coerceIn(0.001f, 0.05f)

                                val ax = -gravityReading.x * 600f
                                val ay = gravityReading.y * 600f

                                velocityX += ax * dt
                                velocityY += ay * dt

                                // Apply natural friction
                                velocityX *= 0.95f
                                velocityY *= 0.95f

                                var newX = positionX + velocityX * dt
                                var newY = positionY + velocityY * dt

                                // Keep within boundaries (no clipping off screen)
                                if (newX < 0f) {
                                    newX = 0f
                                    velocityX = 0f
                                } else if (newX > maxX) {
                                    newX = maxX
                                    velocityX = 0f
                                }

                                if (newY < 0f) {
                                    newY = 0f
                                    velocityY = 0f
                                } else if (newY > maxY) {
                                    newY = maxY
                                    velocityY = 0f
                                }

                                positionX = newX
                                positionY = newY
                            }
                            lastFrameTimeNanos = frameTimeNanos
                        }
                    }
                }

                // Render blue marble inside BoxWithConstraints
                Box(
                    modifier = Modifier
                        .offset { IntOffset(positionX.roundToInt(), positionY.roundToInt()) }
                        .size(40.dp)
                        .background(Color.Blue, CircleShape)
                )
            }
        }
    }
}
