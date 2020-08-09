package logic;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class Counter {

    public static Label child = new Label();
    public static int value;

    static {
        child.setFont(new Font("serif", 16));
        child.setAlignment(Pos.TOP_CENTER);
        child.setPadding(new Insets(0, 0, 450, 0));
    }

    public static void increment() {
        value++;
        child.setText(Integer.toString(value));
    }

}
