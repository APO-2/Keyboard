package com.example.keyboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Canvas canvas;

    private boolean isRunning;
    private GraphicsContext graphicsContext;
    private Rectangle keyboardRect;
    private Rectangle mouseRect;
    private double keyboardRectX;
    private double keyboardRectY;
    private double mouseRectX;
    private double mouseRectY;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // inicializar los eventos del teclado y del mause
        initEvents();
        isRunning = true;

        graphicsContext = canvas.getGraphicsContext2D();
        keyboardRectX = 50;
        keyboardRectY = 50;
        mouseRectX = 50;
        mouseRectY = 50;

        keyboardRect = new Rectangle(keyboardRectX, keyboardRectY, 50, 50);
        mouseRect = new Rectangle(mouseRectX, mouseRectY, 50, 50);

        // ejecutarl el hilo de pintado
        pain();

    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    private void initEvents(){
        // Agregar controladores de eventos para el movimiento del rectángulo del teclado
        canvas.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    upPressed = true;
                    break;
                case DOWN:
                    downPressed = true;
                    break;
                case LEFT:
                    leftPressed = true;
                    break;
                case RIGHT:
                    rightPressed = true;
                    break;
                default:
                    break;
            }
        });

        canvas.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:
                    upPressed = false;
                    break;
                case DOWN:
                    downPressed = false;
                    break;
                case LEFT:
                    leftPressed = false;
                    break;
                case RIGHT:
                    rightPressed = false;
                    break;
                default:
                    break;
            }
        });

        canvas.setOnMouseMoved(event -> {
            mouseRectX = event.getX();
            mouseRectY = event.getY();
        });
    }

    private void pain(){
        new Thread(() -> {
            while (isRunning) {
                Platform.runLater(() -> {
                    // Actualizar la posición de los rectángulos
                    updateRectangles();

                    // Dibujar los rectángulos en el canvas
                    drawRectangles();
                });

                try {
                    // Esperar un momento antes de volver a actualizar la posición de los rectángulos
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void updateRectangles() {
        // Actualizar la posición del rectángulo controlado por el teclado
        if (upPressed) {
            keyboardRectY -= 5;
        }
        if (downPressed) {
            keyboardRectY += 5;
        }
        if (leftPressed) {
            keyboardRectX -= 5;
        }
        if (rightPressed) {
            keyboardRectX += 5;
        }

    }
    private void drawRectangles() {
        // Limpiar el canvas
        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());

        // Dibujar los rectángulos en el canvas
        graphicsContext.setFill(Color.RED);
        graphicsContext.fillRect( keyboardRectX, keyboardRectY, keyboardRect.getWidth(), keyboardRect.getHeight());
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillRect(mouseRectX, mouseRectY, mouseRect.getWidth(), mouseRect.getHeight());
    }

}