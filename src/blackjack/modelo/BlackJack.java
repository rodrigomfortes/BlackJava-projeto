package blackjack.modelo;

import blackjack.Jogador.Dealer;
import blackjack.Jogador.JogadorHumano;
import gui.Janela;

public class BlackJack {
    private Baralho baralho;
    private JogadorHumano jogador;
    private Dealer dealer;
    private Carta cartaOculta;
    private Janela janela;  // Instância de Janela

    // Construtor que recebe uma instância de Janela
    public BlackJack(Janela janela) {
        this.janela = janela;
        iniciarJogo();  // Chama o método para iniciar o jogo
    }

    public void iniciarJogo() {
        baralho = new Baralho();
        jogador = new JogadorHumano(janela);  // Passa a janela para o jogador humano
        dealer = new Dealer();

        // Dealer recebe uma carta oculta
        cartaOculta = baralho.comprarCarta();
        dealer.receberCarta(baralho.comprarCarta());

        // Jogador recebe suas cartas
        jogador.receberCarta(baralho.comprarCarta());
        jogador.receberCarta(baralho.comprarCarta());
    }

    public void jogadorCompra() {
        jogador.receberCarta(baralho.comprarCarta());
    }

    public void dealerJoga() {
        dealer.receberCarta(cartaOculta);
        while (dealer.obterSoma() < 17) {
            dealer.receberCarta(baralho.comprarCarta());
        }
    }

    public JogadorHumano getJogador() {
        return jogador;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Baralho getBaralho() {
        return baralho;
    }

    public Carta getCartaOculta() {
        return cartaOculta;
    }
}
