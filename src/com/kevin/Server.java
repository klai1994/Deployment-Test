package com.kevin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {

	@Override
	public void start(Stage primaryStage) {
		TextArea text = new TextArea();

		Scene scene = new Scene(new ScrollPane(text), 450, 200);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();

		new Thread(() -> {
			try {

				ServerSocket serverSocket = new ServerSocket(8000);
				Platform.runLater(() -> text.appendText("Server started at " + new Date() + '\n'));
				Socket socket = serverSocket.accept();
				DataInputStream clientInput = new DataInputStream(socket.getInputStream());
				DataOutputStream clientOutput = new DataOutputStream(socket.getOutputStream());

				while (true) {
					boolean result = true;
					// Get user's input to check if prime
					int input = clientInput.readInt();
					// Check if prime
					if (input < 2) {
						result = false;
					}
					else {
						for (int i = 2; i * i <= input; i++) {
							if (input % i == 0) {
								result = false;
							}
						}
					}
					boolean isPrime = result;
					clientOutput.writeBoolean(result);
					Platform.runLater(() -> {
						text.appendText("Input: " + input + '\n');
						text.appendText("Prime: " + isPrime + '\n');
						text.setScrollTop(Double.MIN_VALUE);
					});
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
