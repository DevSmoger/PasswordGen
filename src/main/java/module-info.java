module ee.coar.passwordgen {
    requires javafx.controls;
    requires javafx.fxml;


    opens ee.coar.passwordgen to javafx.fxml;
    exports ee.coar.passwordgen;
}