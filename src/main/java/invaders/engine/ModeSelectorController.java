package invaders.engine;

import invaders.entities.SpaceBackground;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ModeSelectorController {
    private VBox view;

    public ModeSelectorController(Stage stage) {
        view = new VBox(10);

        Button easyButton = new Button("Easy Mode");
        Button mediumButton = new Button("Medium Mode");
        Button hardButton = new Button("Hard Mode");

        easyButton.setOnAction(e -> switchToGameScene(stage, "Easy"));
        mediumButton.setOnAction(e -> switchToGameScene(stage, "Medium"));
        hardButton.setOnAction(e -> switchToGameScene(stage, "Hard"));

        Label label = new Label("Please choose a game mode");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        label.setTextFill(Paint.valueOf("White"));

        view.getChildren().addAll(label, easyButton, mediumButton, hardButton);
        view.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        view.setAlignment(Pos.CENTER);
    }

    private void switchToGameScene(Stage stage, String mode) {
        GameEngine model = null;
        if (mode.equals("Easy")) {
            model = new GameEngine("src/main/resources/config_easy.json");
        } else if (mode.equals("Medium")) {
            model = new GameEngine("src/main/resources/config_medium.json");
        } else {
            model = new GameEngine("src/main/resources/config_hard.json");
        }

        GameWindow window = new GameWindow(model);
        window.run();

        stage.setTitle("Space Invaders");
        stage.setScene(window.getScene());
        stage.show();

        window.run();
    }

    public VBox getView() {
        return view;
    }
}
