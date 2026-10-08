package poo.roscos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
        "jdbc:mysql://localhost:3306/RoscosVarela?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "algofacil123";

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Prueba de conexión
    public static void main(String[] args) {
        try (Connection con = getConexion()) {
            System.out.println("Conectado a RoscosVarela");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}