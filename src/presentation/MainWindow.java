package presentation;

import business.Sistema;
import data.Usuario;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainWindow extends JFrame {
    private Sistema sistema;
    private Usuario usuarioLogado;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private LoginPanel loginPanel;
    private FeedPanel feedPanel;
    private PerfilPanel perfilPanel;

    public MainWindow(Sistema sistema) {
        this.sistema = sistema;

        // Configuração básica da janela
        setTitle("Rede Social de Fotos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Inicializa o gerenciador de cards
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Cria os painéis
        loginPanel = new LoginPanel(this, sistema);
        feedPanel = new FeedPanel(this, sistema);
        perfilPanel = new PerfilPanel(this, sistema);

        // Adiciona os painéis ao cardPanel
        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(feedPanel, "FEED");
        cardPanel.add(perfilPanel, "PERFIL");

        // Adiciona o cardPanel à janela
        add(cardPanel);

        // Começa mostrando o painel de login
        mostrarLogin();
    }

    public void setUsuarioLogado(Usuario usuario) throws SQLException {
        this.usuarioLogado = usuario;
        feedPanel.atualizarFeed();
        perfilPanel.atualizarPerfil();
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void mostrarLogin() {
        cardLayout.show(cardPanel, "LOGIN");
    }

    public void mostrarFeed() {
        cardLayout.show(cardPanel, "FEED");
    }

    public void mostrarPerfil() {
        cardLayout.show(cardPanel, "PERFIL");
    }

    public void logout() {
        usuarioLogado = null;
        mostrarLogin();
    }
}