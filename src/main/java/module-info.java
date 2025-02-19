module com.facturacion.factu3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;

    opens com.facturacion.factu3 to javafx.fxml;
    exports com.facturacion.factu3;
    exports com.facturacion.factu3.controllers;
    opens com.facturacion.factu3.controllers to javafx.fxml;
}