package business;

import persistence.DatabaseConnection;
import presentation.MainWindow;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Configura o look and feel para parecer com o sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Estabelece a conexão com o banco de dados
        try {
            Connection connection = DatabaseConnection.getConnection();
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao estabelecer conexão com o banco de dados: " + e.getMessage());
            return;
        }

        // Inicia a aplicação na Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            Sistema sistema = new Sistema();

            // Cria alguns usuários de teste
            try {
                sistema.cadastrarUsuario("joao", "123", "João Silva");
                sistema.cadastrarUsuario("maria", "123", "Maria Santos");
                sistema.cadastrarUsuario("pedro", "123", "Pedro Souza");
                sistema.cadastrarUsuario("ediney", "123", "Ediney Mendonça");
                System.out.println("Usuários de teste criados com sucesso!");
            } catch (Exception e) {
                System.err.println("Erro ao criar usuários de teste: " + e.getMessage());
            }

            // Cria e exibe a janela principal
            MainWindow mainWindow = new MainWindow(sistema);
            mainWindow.setVisible(true);
        });
    }
}