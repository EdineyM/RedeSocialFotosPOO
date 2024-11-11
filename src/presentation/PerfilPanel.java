package presentation;

import business.Sistema;
import data.Usuario;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PerfilPanel extends JPanel {
    private MainWindow mainWindow;
    private Sistema sistema;
    private JPanel followingList;
    private JTextField searchField;

    public PerfilPanel(MainWindow mainWindow, Sistema sistema) {
        this.mainWindow = mainWindow;
        this.sistema = sistema;

        setLayout(new BorderLayout());

        // Barra superior
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botões do lado esquerdo
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton voltarButton = new JButton("Voltar ao Feed");
        voltarButton.addActionListener(e -> mainWindow.mostrarFeed());
        leftButtons.add(voltarButton);

        // Botão de logout do lado direito
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> mainWindow.logout());
        rightButtons.add(logoutButton);

        topBar.add(leftButtons, BorderLayout.WEST);
        topBar.add(rightButtons, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Painel central
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Informações do perfil
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Área de pesquisa de usuários
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createTitledBorder("Buscar Usuários"));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(e -> buscarUsuario());

        searchPanel.add(searchField);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(searchButton);

        // Lista de usuários seguidos
        JPanel followingPanel = new JPanel();
        followingPanel.setBorder(BorderFactory.createTitledBorder("Seguindo"));
        followingPanel.setLayout(new BorderLayout());

        followingList = new JPanel();
        followingList.setLayout(new BoxLayout(followingList, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(followingList);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        followingPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(searchPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(followingPanel);

        add(new JScrollPane(centerPanel), BorderLayout.CENTER);
    }

    public void atualizarPerfil() {
        Usuario usuarioLogado = mainWindow.getUsuarioLogado();
        if (usuarioLogado != null) {
            atualizarListaSeguindo(usuarioLogado);
        }
    }

    private void atualizarListaSeguindo(Usuario usuario) {
        followingList.removeAll();
        List<Usuario> seguindo = usuario.getFollowing();

        if (seguindo.isEmpty()) {
            JLabel emptyLabel = new JLabel("Você ainda não segue ninguém");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            followingList.add(emptyLabel);
        } else {
            for (Usuario seguido : seguindo) {
                JPanel userPanel = new JPanel(new BorderLayout());
                userPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                JLabel nameLabel = new JLabel(seguido.getFullName() + " (@" + seguido.getUsername() + ")");
                JButton unfollowButton = new JButton("Deixar de Seguir");
                unfollowButton.addActionListener(e -> deixarDeSeguir(seguido));

                userPanel.add(nameLabel, BorderLayout.CENTER);
                userPanel.add(unfollowButton, BorderLayout.EAST);

                followingList.add(userPanel);
                followingList.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        revalidate();
        repaint();
    }

    private void buscarUsuario() {
        String username = searchField.getText().trim();
        if (!username.isEmpty()) {
            Usuario encontrado = sistema.buscarUsuario(username);
            if (encontrado != null && !encontrado.equals(mainWindow.getUsuarioLogado())) {
                int option = JOptionPane.showConfirmDialog(this,
                        "Deseja seguir " + encontrado.getFullName() + "?",
                        "Seguir Usuário",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    try {
                        sistema.seguirUsuario(mainWindow.getUsuarioLogado(), encontrado);
                        atualizarPerfil();
                        JOptionPane.showMessageDialog(this, "Agora você está seguindo " + encontrado.getFullName());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this,
                                "Erro ao seguir usuário: " + e.getMessage(),
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (encontrado == null) {
                JOptionPane.showMessageDialog(this,
                        "Usuário não encontrado",
                        "Busca",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        searchField.setText("");
    }

    private void deixarDeSeguir(Usuario seguido) {
        try {
            sistema.pararSeguirUsuario(mainWindow.getUsuarioLogado(), seguido);
            atualizarPerfil();
            JOptionPane.showMessageDialog(this,
                    "Você deixou de seguir " + seguido.getFullName(),
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao deixar de seguir: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}