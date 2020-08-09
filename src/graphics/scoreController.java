package graphics;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import logic.Timer;
import main.Main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

public class scoreController {

    @FXML
    Button returnButton;

    @FXML
    public void returnMethod() throws NoSuchMethodException, ParseException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
        Main.initialize();
        Main.animationTimer.start();
    }

}
