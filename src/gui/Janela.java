package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import blackjack.modelo.BlackJack;
import blackjack.modelo.Carta;

public class Janela extends JFrame {
    private final BlackJack jogo; // Jogo de BlackJack
    private JPanel painelJogo;
    private JButton botaoComprar;
    private JButton botaoParar;
    private JButton botaoJogarNovamente;  // Botão para jogar novamente
    private Image cartaVirada;
    private Image imagemFundo;

    // Construtor
    public Janela() {
        super("BlackJack");

        Font fontePersonalizada = new Font("Arial", Font.PLAIN, 16);

        UIManager.put("Label.font", fontePersonalizada);
        UIManager.put("Button.font", fontePersonalizada);
        UIManager.put("TextField.font", fontePersonalizada);
        UIManager.put("TextArea.font", fontePersonalizada);
        UIManager.put("ComboBox.font", fontePersonalizada);
        UIManager.put("Table.font", fontePersonalizada);
        UIManager.put("OptionPane.messageFont", fontePersonalizada);
        UIManager.put("OptionPane.buttonFont", fontePersonalizada);


        jogo = new BlackJack(this);  // Passa a instância de Janela para o jogo

        try {
            // Carregar a imagem da carta virada
            cartaVirada = ImageIO.read(new File("src/cards/BACK.png"));
            cartaVirada = cartaVirada.getScaledInstance(80, 110, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem da carta virada");
            cartaVirada = null;
        }

        try {
            imagemFundo = ImageIO.read(new File("src/cards/9fundoblackjava.png"));
            imagemFundo = imagemFundo.getScaledInstance(700, 500, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem de fundo");
            imagemFundo = null;
        }

        configurarJanela();   // Configura a janela principal
        configurarPainel();   // Configura o painel de jogo
        configurarBotoes();   // Configura os botões de interação

        this.setVisible(true);
    }

    // Configurações iniciais da janela
    private void configurarJanela() {
        this.setSize(700, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);  // Centraliza a janela
        this.setResizable(false);         // Impede redimensionamento
    }

    // Configuração do painel de jogo
    private void configurarPainel() {
        painelJogo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagemFundo != null) {
                    g.drawImage(imagemFundo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    setBackground(Color.DARK_GRAY); // fallback
                }
                desenharCartas(g);   // Desenha as cartas no painel
                desenharMensagemFinal(g); // Exibe a mensagem final (vencedor)
            }
        };
        painelJogo.setBackground(new Color(0, 166, 255));  // Cor de fundo
        this.add(painelJogo, BorderLayout.CENTER); // Adiciona o painel ao centro da janela
    }

    // Configura os botões da interface
    private void configurarBotoes() {
        JPanel painelBotoes = new JPanel();
        botaoComprar = new JButton("Comprar");
        botaoParar = new JButton("Parar");

        painelBotoes.add(botaoComprar);
        painelBotoes.add(botaoParar);
        this.add(painelBotoes, BorderLayout.SOUTH);  // Adiciona os botões à parte inferior

        // Ação para o botão Comprar
        botaoComprar.addActionListener(e -> {
            jogo.getJogador().comprarCarta();  // Chama a função para comprar uma carta
            if (jogo.getJogador().obterSoma() > 21) {
                botaoComprar.setEnabled(false);  // Desabilita o botão de Comprar se o jogador estourar
                botaoParar.setEnabled(false);    // Desabilita o botão de Parar também
            }
            painelJogo.repaint();  // Redesenha a interface após a ação
        });

        // Ação para o botão Parar
        botaoParar.addActionListener(e -> {
            botaoComprar.setEnabled(false);
            botaoParar.setEnabled(false);
            jogo.getJogador().parar();   // Chama a função de parar
            painelJogo.repaint();        // Redesenha a interface após a ação
        });

        // Configuração do botão "Jogar Novamente"
        botaoJogarNovamente = new JButton("Jogar Novamente");
        painelBotoes.add(botaoJogarNovamente);
        botaoJogarNovamente.setVisible(false);  // Inicialmente invisível

        // Ação do botão "Jogar Novamente"
        botaoJogarNovamente.addActionListener(e -> {
            jogo.getJogador().limparMao();  // Limpa as mãos dos jogadores
            jogo.getDealer().limparMao();
            jogo.iniciarJogo();  // Reinicia o jogo
            painelJogo.repaint();  // Redesenha a interface

            // Esconde o botão "Jogar Novamente" e habilita os outros botões
            botaoJogarNovamente.setVisible(false);
            botaoComprar.setEnabled(true);
            botaoParar.setEnabled(true);
        });
    }


    // Desenha as cartas na tela
    private void desenharCartas(Graphics g) {
        int larguraCarta = 80;
        int alturaCarta = 110;

        Font fontePersonalizada = new Font("Arial", Font.PLAIN, 16);

        g.setFont(fontePersonalizada);


        // Cria um Graphics2D para permitir a interpolação bilinear
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Desenha as cartas do dealer
        g.setColor(Color.WHITE);
        String textoDealer = "Dealer";
        if (!botaoParar.isEnabled()) {  // Quando o jogador parar, mostra todas as cartas do dealer
            textoDealer += ": " + jogo.getDealer().obterSoma();  // Mostra a soma final do dealer
        } else {
            textoDealer += ":";  // Se o jogador ainda não parou, não mostra a soma
        }
        g.drawString(textoDealer, 30, 20);

        int x = 30;
        int y = 40;

        // Primeira carta do dealer (virada para baixo)
        boolean primeiraCarta = true;
        for (Carta carta : jogo.getDealer().getMao()) {
            if (primeiraCarta && botaoParar.isEnabled()) {
                if (cartaVirada != null) {
                    g2d.drawImage(cartaVirada, x, y, larguraCarta, alturaCarta, null);  // Carta virada
                } else {
                    g.drawRect(x, y, larguraCarta, alturaCarta);
                    g.drawString("BACK", x + 10, y + 60);  // Se não carregar a imagem
                }
                primeiraCarta = false;
            } else {
                // Quando o jogador parar, mostra todas as cartas
                Image imgCarta = carregarImagemCarta(carta);  // Carrega a imagem da carta
                if (imgCarta != null) {
                    g2d.drawImage(imgCarta, x, y, larguraCarta, alturaCarta, null);
                } else {
                    g.drawRect(x, y, larguraCarta, alturaCarta);
                    g.drawString(carta.toString(), x + 10, y + 60);  // Se não carregar a imagem
                }
            }
            x += larguraCarta + 10;  // Move a posição da próxima carta
        }

        // Jogador
        g.setColor(Color.WHITE);
        g.drawString("Você: " + jogo.getJogador().obterSoma(), 30, 200);
        x = 30;
        y = 220;

        for (Carta carta : jogo.getJogador().getMao()) {
            Image imgCarta = carregarImagemCarta(carta);
            if (imgCarta != null) {
                g2d.drawImage(imgCarta, x, y, larguraCarta, alturaCarta, null);
            } else {
                g.drawRect(x, y, larguraCarta, alturaCarta);
                g.drawString(carta.toString(), x + 10, y + 60);
            }
            x += larguraCarta + 10;  // Move a posição da próxima carta
        }
    }

    // Carrega as imagens das cartas a partir do diretório
    private Image carregarImagemCarta(Carta carta) {
        try {
            String valor = carta.getValorString();
            String naipe = carta.getNaipe();

            String nomeArquivo = valor + "-" + naipe + ".png";
            BufferedImage img = ImageIO.read(new File("src/cards/" + nomeArquivo));  // Caminho para as imagens

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

    // Exibe a mensagem de resultado final
    private void desenharMensagemFinal(Graphics g) {
        if (!botaoParar.isEnabled()) {  // Quando o jogo terminar
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

            // Exibe a mensagem
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            g.drawString(mensagem, 250, 180);

            // Torna o botão "Jogar Novamente" visível imediatamente após o fim do jogo
            botaoJogarNovamente.setVisible(true);
        }
    }


    // Método para mostrar mensagens de vitória/derrota
    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    // Método para desabilitar os botões depois que o jogador parar ou estourar
    public void desabilitarBotoes() {
        botaoComprar.setEnabled(false);
        botaoParar.setEnabled(false);
    }

    // Método para retornar o objeto do jogo
    public BlackJack getJogo() {
        return jogo;
    }
}
