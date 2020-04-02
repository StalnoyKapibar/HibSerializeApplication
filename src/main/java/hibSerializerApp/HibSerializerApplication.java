package hibSerializerApp;

import hibSerializerApp.view.WebCamPreviewViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
        stage.setScene(new Scene(root, 900, 690));
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
//        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/views/main.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
