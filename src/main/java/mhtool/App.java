package mhtool;

import java.sql.SQLException;

import mhtool.model.Model;
import mhtool.data.*;

public final class App {
    public static void main(String[] args)throws SQLException {
        var connection = DAOUtils.localMySQLConnection("MonsterHunter", "root", "root");
        var model = Model.fromConnection(connection);
        //var model = Model.mock();
        var view = new View(() -> {
            try {
                connection.close();
            } catch (Exception e) {}
        });
        var controller = new Controller(model, view);
        view.setController(controller);
        controller.loginForm();
    }
}
