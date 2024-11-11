package presentation;

import business.Sistema;
import data.Usuario;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainWindow mainWindow;
    private Sistema sistema;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cadastroButton;

    public LoginPanel(MainWindow mainWindow, Sistema sistema) {
        this.mainWindow = mainWindow;
        this.sistema = sistema;

        setLayout(new BorderLayout());

        // Painel central com os campos
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Título
        JLabel titleLabel = new JLabel("Rede Social de Fotos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(titleLabel, gbc);

        // Campo de usuário
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        centerPanel.add(new JLabel("Usuário:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        centerPanel.add(usernameField, gbc);

        // Campo de senha
        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        centerPanel.add(passwordField, gbc);

        // Botão de login
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> realizarLogin());
        centerPanel.add(loginButton, gbc);

        // Botão de cadastro
        gbc.gridy = 4;
        cadastroButton = new JButton("Criar Nova Conta");
        cadastroButton.addActionListener(e -> mostrarTelaCadastro());
        centerPanel.add(cadastroButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void realizarLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Usuario usuario = sistema.login(username, password);
            mainWindow.setUsuarioLogado(usuario);
            mainWindow.mostrarFeed();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao fazer login: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarTelaCadastro() {
        String username = JOptionPane.showInputDialog(this, "Digite o nome de usuário:");
        if (username == null || username.trim().isEmpty()) return;

        String password = JOptionPane.showInputDialog(this, "Digite a senha:");
        if (password == null || password.trim().isEmpty()) return;

        String fullName = JOptionPane.showInputDialog(this, "Digite seu nome completo:");
        if (fullName == null || fullName.trim().isEmpty()) return;

        try {
            Usuario usuario = sistema.cadastrarUsuario(username, password, fullName);
            JOptionPane.showMessageDialog(this,
                    "Usuário cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        usernameField.setText("");
        passwordField.setText("");
    }
}