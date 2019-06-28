package javaFxGame;

import javafx.application.Application;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Example4M extends Application
{
  public static void main(String[] args)
  {
    launch(args);
  }

  @Override
  public void start(Stage theStage)
  {
    theStage.setTitle( "Click the Target!" );

    Group root = new Group();
    Scene theScene = new Scene( root );
    theStage.setScene( theScene );

    Canvas canvas = new Canvas( 500, 500 );
    //Image restart = new Image("restart.png");

    root.getChildren().add( canvas );

    Circle targetData = new Circle(100,100,32);

    theScene.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          public void handle(MouseEvent e) {
            if (targetData.contains(e.getX(), e.getY())) {
              double x = 50 + 400 * Math.random();
              double y = 50 + 400 * Math.random();
              targetData.setCenterX(x);
              targetData.setCenterY(y);
            }
          }});

    GraphicsContext gc = canvas.getGraphicsContext2D();

    Image bullseye = new Image( "images/bullseye.png" );

    new AnimationTimer() {
      public void handle(long currentNanoTime){
        // Clear the canvas
        gc.setFill( new Color(0.85, 0.85, 1.0, 1.0) );
        gc.fillRect(0,0, 512,512);

        gc.drawImage( bullseye,
            targetData.getCenterX() - targetData.getRadius(),
            targetData.getCenterY() - targetData.getRadius() );

        gc.setFill( Color.BLUE );
      }
    }.start();


    theStage.show();
  }
}