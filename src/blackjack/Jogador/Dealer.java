package blackjack.Jogador;

import blackjack.modelo.BlackJack;

public class Dealer extends Jogador {

    public Dealer() {
        super();
    }

    @Override
    public void jogar(BlackJack jogo) {
        // Dealer recebe carta oculta
        while (obterSoma() < 17) {
            receberCarta(jogo.getBaralho().comprarCarta());
        }
    }
}
