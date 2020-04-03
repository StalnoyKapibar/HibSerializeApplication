package hibSerializerApp;

import hibSerializerApp.view.WebCamPreviewViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;

public class HibSerializerApplication extends Application {

    public static WebCamPreviewViewController startWebCamModal() {
        FXMLLoader fxmlLoader = new FXMLLoader(HibSerializerApplication.class.getResource("/views/webCamPreview.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("Make Photo");
        stage.getIcons().add(new Image("icon.png"));
        Scene scene = new Scene(root, 900, 690);
        stage.setScene(scene);
        JMetro jMetro = new JMetro(root, Style.LIGHT);
        jMetro.setAutomaticallyColorPanes(true);
        stage.showAndWait();
        return fxmlLoader.getController();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("HIB serializer");
        stage.getIcons().add(new Image("icon.png"));
        Parent root = FXMLLoader.load(getClass().getResource("/views/main.fxml"));
        Scene scene = new Scene(root);
        JMetro jMetro = new JMetro(root, Style.LIGHT);
        jMetro.setAutomaticallyColorPanes(true);
        stage.setScene(scene);
        stage.show();
    }
}
