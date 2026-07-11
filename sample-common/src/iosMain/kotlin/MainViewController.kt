import androidx.compose.ui.window.ComposeUIViewController
import com.maxkach.scrolleffects.sample.App

/**
 * Entry point consumed by the iOS host app (see iosApp/). Returns a
 * UIViewController that hosts the shared Compose [App].
 */
@Suppress("unused", "FunctionName")
fun MainViewController() = ComposeUIViewController { App() }
