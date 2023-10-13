package invaders.engine;

import java.util.List;
import java.util.ArrayList;

import invaders.ConfigReader;
import invaders.entities.EntityViewImpl;
import invaders.entities.SpaceBackground;
import invaders.gameobject.Enemy;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import invaders.entities.EntityView;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import org.json.simple.JSONObject;

public class GameWindow {
    private final int width;
    private final int height;
    private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews =  new ArrayList<EntityView>();
    private Renderable background;

    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
    private int scores = 0;
    private long elapsedTime = 0;
    private Label scoreLabel;
    private Label timerLabel;
    // private static final double VIEWPORT_MARGIN = 280.0;

    // Add a variable to store the elapsed time

    public GameWindow(GameEngine model){
        this.model = model;
        this.width =  model.getGameWidth();
        this.height = model.getGameHeight();

        pane = new Pane();
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

    }

    public void run() {
//        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> {
            this.updateElapsedTime(); // Update the elapsed time
            this.draw(); // Render the game state
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    private void draw(){
        model.update();

        if (scoreLabel != null) {
            pane.getChildren().remove(scoreLabel);
        }

        if (timerLabel != null) {
            pane.getChildren().remove(timerLabel);
        }

        displayScores();
        displayTimer();

        List<Renderable> renderables = model.getRenderables();
        for (Renderable entity : renderables) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (Renderable entity : renderables){
            if (!entity.isAlive()){
                for (EntityView entityView : entityViews){
                    if (entityView.matchesEntity(entity)){
                        entityView.markForDelete();
                    }
                }
            }
        }

        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }


        model.getGameObjects().removeAll(model.getPendingToRemoveGameObject());
        model.getGameObjects().addAll(model.getPendingToAddGameObject());
        model.getRenderables().removeAll(model.getPendingToRemoveRenderable());
        model.getRenderables().addAll(model.getPendingToAddRenderable());

        model.getPendingToAddGameObject().clear();
        model.getPendingToRemoveGameObject().clear();
        model.getPendingToAddRenderable().clear();
        model.getPendingToRemoveRenderable().clear();

        entityViews.removeIf(EntityView::isMarkedForDelete);

    }

    public Scene getScene() {
        return scene;
    }

    public void displayScores() {
        scoreLabel = createScoreLabel(); // Create the label
        pane.getChildren().add(scoreLabel); // Add it to the pane
    }

    private Label createScoreLabel() {
        Label label = new Label("Scores: " + model.getScore());
        label.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        label.setLayoutX(10); // Adjust X position as needed
        label.setLayoutY(10); // Adjust Y position as needed
        return label;
    }

    private Label createTimerDisplay() {
        // Convert elapsed time to minutes and seconds
        long totalSeconds = elapsedTime / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        Label timerLabel = new Label();
        // Update the display with the formatted time
        // For example, if you have a Label called timerLabel:
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        timerLabel.setLayoutX(275);
        return timerLabel;
    }

    public void displayTimer() {
        timerLabel = createTimerDisplay(); // Create the label
        pane.getChildren().add(timerLabel); // Add it to the pane
    }

    private void updateElapsedTime() {
        // Update elapsedTime based on game logic
        elapsedTime += 17/2;
    }
}