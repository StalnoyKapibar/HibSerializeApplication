import hibSerializerApp.HibSerializerApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;

public class SaveModalTestLaunch extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Do you want to save the changes?");
        stage.getIcons().add(new Image("icon.png"));
        Parent root = null;
        try {
            root = FXMLLoader.load(HibSerializerApplication.class.getResource("/views/save-modal.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        JMetro jMetro = new JMetro(root, Style.LIGHT);
        jMetro.setAutomaticallyColorPanes(true);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
