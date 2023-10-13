package invaders;

import invaders.engine.ModeSelectorController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;

import java.util.Map;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ModeSelectorController modeSelectorController = new ModeSelectorController(primaryStage);
        Scene modeSelectionScene = new Scene(modeSelectorController.getView(), 600, 800);

        primaryStage.setTitle("Game Mode Selector");
        primaryStage.setScene(modeSelectionScene);
        primaryStage.show();

//        GameEngine model = new GameEngine("src/main/resources/config_easy.json");
//        GameWindow window = new GameWindow(model);
//        window.run();

//        primaryStage.setTitle("Space Invaders");
//        primaryStage.setScene(window.getScene());
//        primaryStage.show();

//        window.run();
    }
}
