package hib;

import hib.controller.WebCamPreviewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HibSerializerApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("HIB serializer");
        Parent root = FXMLLoader.load(getClass().getResource("view/hibserializer.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static WebCamPreviewController startWebCamModal() {
        FXMLLoader fxmlLoader = new FXMLLoader(HibSerializerApplication.class.getResource("view/webCamPreview.fxml"));
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
        stage.setScene(new Scene(root, 900, 690));
        stage.showAndWait();
        return fxmlLoader.getController();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
