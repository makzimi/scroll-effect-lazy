import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.maxkach.elasticlist.navigation.AppNavigation
import com.maxkach.elasticlist.ui.theme.ElasticListTheme

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        ElasticListTheme {
            AppNavigation()
        }
    }
}