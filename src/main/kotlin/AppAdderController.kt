import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.TextArea
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.stage.StageStyle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*

class AppAdderController : Initializable {

    @FXML
    private lateinit var appTextArea: TextArea

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        appTextArea.text = AppManager.customApps.readText()
    }

    @FXML
    private fun loadButtonPressed(event: ActionEvent) {
        FileChooser().apply {
            extensionFilters.add(FileChooser.ExtensionFilter("Metin Dosyası", "*"))
            title = "Bir metin dosyası seçin"
            appTextArea.text = showOpenDialog((event.source as Node).scene.window)?.readText()
        }
    }

    @FXML
    private fun okButtonPressed(event: ActionEvent) {
        if (!appTextArea.text.isNullOrBlank()) {
            Alert(Alert.AlertType.WARNING).apply {
                initStyle(StageStyle.UTILITY)
                isResizable = false
                headerText = "Varsayılan olarak listelenmeyen uygulamaların kaldırılması cihazınızı engelleyebilir."
                showAndWait()
            }
            GlobalScope.launch {
                AppManager.apply {
                    customApps.writeText(appTextArea.text.trim())
                    readPotentialApps()
                    createTables()
                }
            }
        }
        ((event.source as Node).scene.window as Stage).close()
    }
}
