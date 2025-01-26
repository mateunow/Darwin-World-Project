package darwinProject.statistics;

import darwinProject.model.maps.AbstractWorldMap;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class now{

    public static void show(AbstractWorldMap map) {
        Stage stage = new Stage();
        stage.setTitle("Statistics");

        VBox root = new VBox(10,
                new Text("Number of animals: " + map.getElements().size()),
                new Text("Example Stat: TBD")
        );

        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }
}
