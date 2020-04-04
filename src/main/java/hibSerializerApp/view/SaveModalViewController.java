package hibSerializerApp.view;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class SaveModalViewController {
    private Boolean save;

    public void save(ActionEvent event) {
        save = true;
        close(event);
    }

    public void dontSave(ActionEvent event) {
        save = false;
        close(event);
    }

    public void cancel(ActionEvent event) {
        save = null;
        close(event);
    }

    private void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
