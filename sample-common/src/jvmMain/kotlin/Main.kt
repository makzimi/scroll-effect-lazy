import androidx.compose.ui.window.singleWindowApplication
import com.maxkach.elasticlist.navigation.AppNavigation
import com.maxkach.elasticlist.ui.theme.ElasticListTheme

fun main() {
    singleWindowApplication(
        title = "Elastic List"
    ) {
        ElasticListTheme {
            AppNavigation()
        }
    }
}