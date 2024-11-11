package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImageUtil {

    public static ImageIcon createScaledImageIcon(byte[] imageData, int targetWidth, int targetHeight) {
        try {
            if (imageData == null || imageData.length == 0) {
                return createPlaceholderIcon(targetWidth, targetHeight);
            }

            // Tenta ler a imagem
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
            if (originalImage == null) {
                return createPlaceholderIcon(targetWidth, targetHeight);
            }

            // Calcula as dimensões mantendo a proporção
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            double widthRatio = (double) targetWidth / originalWidth;
            double heightRatio = (double) targetHeight / originalHeight;
            double ratio = Math.min(widthRatio, heightRatio);

            int scaledWidth = (int) (originalWidth * ratio);
            int scaledHeight = (int) (originalHeight * ratio);

            // Cria uma nova imagem com fundo branco
            BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = scaledImage.createGraphics();

            try {
                // Configura para melhor qualidade
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Preenche o fundo com branco
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, targetWidth, targetHeight);

                // Calcula a posição central
                int x = (targetWidth - scaledWidth) / 2;
                int y = (targetHeight - scaledHeight) / 2;

                // Desenha a imagem centralizada
                g2d.drawImage(originalImage, x, y, scaledWidth, scaledHeight, null);

            } finally {
                g2d.dispose();
            }

            return new ImageIcon(scaledImage);

        } catch (IOException e) {
            e.printStackTrace();
            return createPlaceholderIcon(targetWidth, targetHeight);
        }
    }

    public static ImageIcon createPlaceholderIcon(int width, int height) {
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = placeholder.createGraphics();

        try {
            // Configura para melhor qualidade
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Preenche o fundo com cinza muito claro
            g2d.setColor(new Color(245, 245, 245));
            g2d.fillRect(0, 0, width, height);

            // Desenha uma borda
            g2d.setColor(new Color(200, 200, 200));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(1, 1, width-2, height-2);

            // Desenha um ícone de imagem estilizado
            int iconSize = Math.min(width, height) / 3;
            int x = (width - iconSize) / 2;
            int y = (height - iconSize) / 2;

            g2d.setColor(new Color(180, 180, 180));
            g2d.setStroke(new BasicStroke(3));

            // Desenha uma moldura de foto
            g2d.drawRect(x, y, iconSize, iconSize);
            g2d.drawLine(x, y, x + iconSize, y + iconSize);
            g2d.drawLine(x + iconSize, y, x, y + iconSize);

            // Adiciona texto
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            String text = "Imagem não disponível";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            g2d.drawString(text, (width - textWidth)/2, y + iconSize + 30);

        } finally {
            g2d.dispose();
        }

        return new ImageIcon(placeholder);
    }
}