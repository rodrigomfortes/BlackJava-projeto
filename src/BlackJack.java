public class BlackJack {
    private Baralho baralho;
    private Jogador jogador;
    private Jogador dealer;
    private Carta cartaOculta;

    public BlackJack() {
        iniciarJogo();
    }

    public void iniciarJogo() {
        baralho = new Baralho();
        jogador = new Jogador();
        dealer = new Jogador();

        // Dealer
        cartaOculta = baralho.comprarCarta();
        dealer.receberCarta(baralho.comprarCarta());

        // Jogador
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

    public Jogador getJogador() {
        return jogador;
    }

    public Jogador getDealer() {
        return dealer;
    }

    public Carta getCartaOculta() {
        return cartaOculta;
    }
}
