module org.dstu.lab1fx {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.media;

    exports org.dstu.lab1fx.task1;
    exports org.dstu.lab1fx.task2;
    exports org.dstu.lab1fx.task3;
    exports org.dstu.lab1fx.task4;
    exports org.dstu.lab1fx.task5;
    exports org.dstu.lab1fx.task6;
    exports org.dstu.lab1fx.task7;
    opens org.dstu.lab1fx.task4 to javafx.fxml;
    opens org.dstu.lab1fx.task5 to javafx.fxml;
    opens org.dstu.lab1fx.task6 to javafx.fxml;
    opens org.dstu.lab1fx.task7 to javafx.fxml, javafx.media;
}