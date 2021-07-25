package com.kevin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {

	DataInputStream fromServer = null;
	DataOutputStream toServer = null;

	@Override
	public void start(Stage primaryStage) {
		// Pane to hold label and text field
		BorderPane textPane = new BorderPane();
		textPane.setPadding(new Insets(5, 5, 5, 5));
		textPane.setStyle("-fx-borger-color: green");
		textPane.setLeft(new Label("Number to check if prime: "));
		// Aligns text field within pane
		TextField inputField = new TextField();
		inputField.setAlignment(Pos.BOTTOM_RIGHT);
		textPane.setCenter(inputField);

		// Pane for whole window
		BorderPane mainPane = new BorderPane();
		// Pane to hold output
		TextArea text = new TextArea();
		mainPane.setCenter(new ScrollPane(text));
		mainPane.setTop(textPane);

		// Set scene
		Scene scene = new Scene(mainPane, 450, 200);
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);
		primaryStage.show();

		inputField.setOnAction(e -> {
			try {
				// Push input to server
				int input = Integer.parseInt(inputField.getText().trim());
				toServer.writeInt(input);
				toServer.flush();
				inputField.clear();

				// Pull output from server
				boolean isPrime = fromServer.readBoolean();
				text.appendText("Input is: " + input + '\n');
				text.appendText("Result from server (prime number?): " + isPrime + '\n');
				text.setScrollTop(Double.MIN_VALUE);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		try {
			Socket socket = new Socket("localhost", 8000);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			text.appendText(e.toString() + '\n');
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
