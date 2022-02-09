package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class gameexception extends Exception {

	public gameexception() {
		// TODO Auto-generated constructor stub
	}

	public gameexception(String message) {
		super(message);
		// TODO Auto-generated constructor stub
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(message);
		alert.setTitle("Error");
		alert.show();
	}

	public gameexception(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public gameexception(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public gameexception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
