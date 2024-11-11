package presentation;

import business.Sistema;
import data.Usuario;
import data.Post;
import data.Photo;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.List;

public class FeedPanel extends JPanel {
    private MainWindow mainWindow;
    private Sistema sistema;
    private JPanel feedContent;

    public FeedPanel(MainWindow mainWindow, Sistema sistema) {
        this.mainWindow = mainWindow;
        this.sistema = sistema;

        setLayout(new BorderLayout());

        // Barra superior
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botões do lado esquerdo
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton perfilButton = new JButton("Meu Perfil");
        perfilButton.addActionListener(e -> mainWindow.mostrarPerfil());
        leftButtons.add(perfilButton);

        JButton novoPostButton = new JButton("Novo Post");
        novoPostButton.addActionListener(e -> criarNovoPost());
        leftButtons.add(novoPostButton);

        JButton refreshButton = new JButton("↻ Atualizar Feed");  // Unicode para símbolo de refresh
        refreshButton.addActionListener(e -> {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            atualizarFeed();
            setCursor(Cursor.getDefaultCursor());
            JOptionPane.showMessageDialog(this,
                    "Feed atualizado com sucesso!",
                    "Atualização",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        leftButtons.add(refreshButton);

        // Botão de logout do lado direito
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> mainWindow.logout());
        rightButtons.add(logoutButton);

        topBar.add(leftButtons, BorderLayout.WEST);
        topBar.add(rightButtons, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Área de rolagem para o feed
        feedContent = new JPanel();
        feedContent.setLayout(new BoxLayout(feedContent, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(feedContent);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void atualizarFeed() {
        feedContent.removeAll();
        Usuario usuarioLogado = mainWindow.getUsuarioLogado();
        if (usuarioLogado != null) {
            List<Post> posts = sistema.getFeedUsuario(usuarioLogado);

            if (posts.isEmpty()) {
                JPanel emptyPanel = new JPanel();
                emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
                emptyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Adiciona algum espaço no topo
                emptyPanel.add(Box.createRigidArea(new Dimension(0, 50)));

                // Ícone de informação
                JLabel iconLabel = new JLabel("ℹ");  // Unicode para símbolo de informação
                iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
                iconLabel.setForeground(new Color(100, 100, 100));
                iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                emptyPanel.add(iconLabel);

                emptyPanel.add(Box.createRigidArea(new Dimension(0, 20)));

                // Mensagem principal
                JLabel emptyLabel = new JLabel("Seu feed está vazio");
                emptyLabel.setFont(new Font("Arial", Font.BOLD, 18));
                emptyLabel.setForeground(new Color(80, 80, 80));
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                emptyPanel.add(emptyLabel);

                // Mensagem secundária
                JLabel tipLabel = new JLabel("Siga outros usuários para ver seus posts!");
                tipLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                tipLabel.setForeground(new Color(120, 120, 120));
                tipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                emptyPanel.add(tipLabel);

                feedContent.add(emptyPanel);
            } else {
                for (Post post : posts) {
                    feedContent.add(criarComponentePost(post));
                    feedContent.add(Box.createRigidArea(new Dimension(0, 20)));
                }
            }
        }
        revalidate();
        repaint();
    }

    private JPanel criarComponentePost(Post post) {
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        postPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        postPanel.setBackground(Color.WHITE);

        // Cabeçalho do post
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);

        // Nome do autor
        JLabel autorLabel = new JLabel(post.getAuthor().getFullName());
        autorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(autorLabel);

        // Badge "Seu post" se for do usuário logado
        if (post.getAuthor().equals(mainWindow.getUsuarioLogado())) {
            JLabel badgeLabel = new JLabel(" (Seu post)");
            badgeLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            badgeLabel.setForeground(new Color(0, 120, 215));  // Azul
            headerPanel.add(badgeLabel);
        }

        // Data do post
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        datePanel.setBackground(Color.WHITE);
        String dataFormatada = post.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        JLabel dateLabel = new JLabel(dataFormatada);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(Color.GRAY);
        datePanel.add(dateLabel);

        // Adiciona ambos os painéis ao header usando BorderLayout
        JPanel fullHeaderPanel = new JPanel(new BorderLayout());
        fullHeaderPanel.setBackground(Color.WHITE);
        fullHeaderPanel.add(headerPanel, BorderLayout.WEST);
        fullHeaderPanel.add(datePanel, BorderLayout.EAST);

        // Substitui o headerPanel original pelo fullHeaderPanel
        headerPanel = fullHeaderPanel;

        // Área da imagem
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);

        // Converte os bytes da imagem em ImageIcon usando o ImageUtil
        byte[] imageData = post.getPhoto().getImageData();
        ImageIcon imageIcon = ImageUtil.createScaledImageIcon(imageData, 400, 300);

        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setPreferredSize(new Dimension(400, 300));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        imagePanel.add(imageLabel);

        // Legenda
        JPanel captionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        captionPanel.setBackground(Color.WHITE);
        JLabel captionLabel = new JLabel(post.getCaption());
        captionPanel.add(captionLabel);

        // Botões de interação
        JPanel interactionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        interactionPanel.setBackground(Color.WHITE);
        JButton likeButton = new JButton("❤ " + post.getLikes().size());
        likeButton.addActionListener(e -> curtirPost(post));
        interactionPanel.add(likeButton);

        postPanel.add(headerPanel);
        postPanel.add(imagePanel);
        postPanel.add(captionPanel);
        postPanel.add(interactionPanel);

        return postPanel;
    }

    private void criarNovoPost() {
        // Seletor de arquivo para a imagem
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
                "Arquivos de Imagem (*.jpg, *.jpeg, *.png, *.gif)",
                "jpg", "jpeg", "png", "gif"
        );
        fileChooser.setFileFilter(imageFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();

                // Verifica o tamanho do arquivo (limita a 5MB)
                if (selectedFile.length() > 5 * 1024 * 1024) {
                    throw new Exception("A imagem é muito grande. O tamanho máximo permitido é 5MB.");
                }

                // Lê os bytes da imagem
                byte[] imageData = Files.readAllBytes(selectedFile.toPath());

                // Verifica se é uma imagem válida
                try {
                    ImageIcon testIcon = new ImageIcon(imageData);
                    if (testIcon.getIconWidth() <= 0 || testIcon.getIconHeight() <= 0) {
                        throw new Exception("O arquivo selecionado não é uma imagem válida.");
                    }
                } catch (Exception e) {
                    throw new Exception("O arquivo selecionado não é uma imagem válida.");
                }

                // Solicita a legenda
                String caption = JOptionPane.showInputDialog(this,
                        "Digite a legenda do post:",
                        "Nova Postagem",
                        JOptionPane.PLAIN_MESSAGE);

                if (caption != null && !caption.trim().isEmpty()) {
                    Photo photo = new Photo(imageData, selectedFile.getName());
                    sistema.criarPost(photo, caption.trim(), mainWindow.getUsuarioLogado());
                    atualizarFeed();
                    JOptionPane.showMessageDialog(this,
                            "Post criado com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (caption != null) {
                    JOptionPane.showMessageDialog(this,
                            "A legenda não pode estar vazia.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao criar post: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void curtirPost(Post post) {
        try {
            Usuario usuarioLogado = mainWindow.getUsuarioLogado();
            if (post.getLikes().contains(usuarioLogado)) {
                sistema.descurtirPost(usuarioLogado, post);
            } else {
                sistema.curtirPost(usuarioLogado, post);
            }
            atualizarFeed();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao curtir post: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}