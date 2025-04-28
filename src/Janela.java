import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Janela extends JFrame {
    private final BlackJack jogo; // Adicionado 'final' como sugerido
    private JPanel painelJogo;
    private JButton botaoComprar;
    private JButton botaoParar;
    private Image cartaVirada;

    public Janela() {
        super("BlackJack");

        jogo = new BlackJack();

        try {
            cartaVirada = ImageIO.read(new File("src/cards/BACK.png"));
            cartaVirada = cartaVirada.getScaledInstance(80, 110, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem da carta virada");
            cartaVirada = null;
        }

        configurarJanela();
        configurarPainel();
        configurarBotoes();

        this.setVisible(true);
    }

    private void configurarJanela() {
        this.setSize(700, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    private void configurarPainel() {
        painelJogo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                desenharCartas(g);
                desenharMensagemFinal(g);
            }
        };
        painelJogo.setBackground(new Color(53, 101, 77));
        this.add(painelJogo, BorderLayout.CENTER);
    }

    private void configurarBotoes() {
        JPanel painelBotoes = new JPanel();
        botaoComprar = new JButton("Comprar");
        botaoParar = new JButton("Parar");

        painelBotoes.add(botaoComprar);
        painelBotoes.add(botaoParar);
        this.add(painelBotoes, BorderLayout.SOUTH);

        // Substituído ActionListener por lambda
        botaoComprar.addActionListener(e -> {
            jogo.jogadorCompra();
            if (jogo.getJogador().obterSoma() > 21) {
                botaoComprar.setEnabled(false);
                botaoParar.setEnabled(false);
            }
            painelJogo.repaint();
        });

        // Substituído ActionListener por lambda
        botaoParar.addActionListener(e -> {
            botaoComprar.setEnabled(false);
            botaoParar.setEnabled(false);
            jogo.dealerJoga();
            painelJogo.repaint();
        });
    }

    private void desenharCartas(Graphics g) {
        int larguraCarta = 80;
        int alturaCarta = 110;

        // Cria um Graphics2D para permitir a interpolação bilinear
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Dealer
        g.setColor(Color.WHITE);
        String textoDealer = "Dealer";
        if (!botaoParar.isEnabled()) {
            textoDealer += ": " + jogo.getDealer().obterSoma();
        } else {
            textoDealer += ":";
        }
        g.drawString(textoDealer, 30, 20);

        int x = 30;
        int y = 40;

        boolean primeiraCarta = true;
        for (Carta carta : jogo.getDealer().getMao()) {
            if (primeiraCarta && botaoParar.isEnabled()) {
                if (cartaVirada != null) {
                    // Usando Graphics2D para escalar com interpolação bilinear
                    g2d.drawImage(cartaVirada, x, y, larguraCarta, alturaCarta, null);
                } else {
                    g.drawRect(x, y, larguraCarta, alturaCarta);
                    g.drawString("BACK", x + 10, y + 60);
                }
                primeiraCarta = false;
            } else {
                Image imgCarta = carregarImagemCarta(carta);
                if (imgCarta != null) {
                    // Usando Graphics2D para escalar com interpolação bilinear
                    g2d.drawImage(imgCarta, x, y, larguraCarta, alturaCarta, null);
                } else {
                    g.drawRect(x, y, larguraCarta, alturaCarta);
                    g.drawString(carta.toString(), x + 10, y + 60);
                }
            }
            x += larguraCarta + 10;
        }

        // Jogador
        g.setColor(Color.WHITE);
        g.drawString("Você: " + jogo.getJogador().obterSoma(), 30, 200);
        x = 30;
        y = 220;

        for (Carta carta : jogo.getJogador().getMao()) {
            Image imgCarta = carregarImagemCarta(carta);
            if (imgCarta != null) {
                // Usando Graphics2D para escalar com interpolação bilinear
                g2d.drawImage(imgCarta, x, y, larguraCarta, alturaCarta, null);
            } else {
                g.drawRect(x, y, larguraCarta, alturaCarta);
                g.drawString(carta.toString(), x + 10, y + 60);
            }
            x += larguraCarta + 10;
        }
    }


    private Image carregarImagemCarta(Carta carta) {
        try {
            String valor = carta.getValorString();
            String naipe = carta.getNaipe();

            String nomeArquivo = valor + "-" + naipe + ".png";
            BufferedImage img = ImageIO.read(new File("src/cards/" + nomeArquivo));

            // Cria uma nova imagem escalada utilizando Graphics2D
            BufferedImage imagemEscalada = new BufferedImage(80, 110, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = imagemEscalada.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(img, 0, 0, 80, 110, null);
            g2d.dispose();

            return imagemEscalada;
        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem da carta: " + e.getMessage());
            return null;
        }
    }



    // Novo método para converter valor int para String
    private String obterValorString(int valor) {
        switch (valor) {
            case 1: return "A";
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            case 10: return "T"; // Para o 10, usamos T como no baralho tradicional
            default: return String.valueOf(valor);
        }
    }

    // Novo método para converter naipe int para String
    private String obterNaipeString(int naipe) {
        switch (naipe) {
            case 1: return "S"; // Espadas (Spades)
            case 2: return "H"; // Copas (Hearts)
            case 3: return "D"; // Ouros (Diamonds)
            case 4: return "C"; // Paus (Clubs)
            default: return "U"; // Unknown
        }
    }

    private void desenharMensagemFinal(Graphics g) {
        if (!botaoParar.isEnabled()) {
            int jogadorSoma = jogo.getJogador().obterSoma();
            int dealerSoma = jogo.getDealer().obterSoma();
            String mensagem;

            if (jogadorSoma > 21) {
                mensagem = "Você perdeu!";
            } else if (dealerSoma > 21) {
                mensagem = "Você venceu!";
            } else if (jogadorSoma > dealerSoma) {
                mensagem = "Você venceu!";
            } else if (jogadorSoma < dealerSoma) {
                mensagem = "Você perdeu!";
            } else {
                mensagem = "Empate!";
            }

            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.setColor(Color.YELLOW);
            g.drawString(mensagem, 250, 180);
        }
    }
}