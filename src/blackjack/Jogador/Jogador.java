package blackjack.Jogador;

import blackjack.modelo.BlackJack;
import blackjack.modelo.Carta;

import java.util.ArrayList;

public abstract class Jogador {
    protected ArrayList<Carta> mao;
    protected int soma;
    protected int quantidadeAs;

    public Jogador() {
        mao = new ArrayList<>();
        soma = 0;
        quantidadeAs = 0;
    }

    public void receberCarta(Carta carta) {
        mao.add(carta);
        soma += carta.getValor();
        if (carta.ehAs()) {
            quantidadeAs++;
        }
        ajustarAs();
    }

    private void ajustarAs() {
        while (soma > 21 && quantidadeAs > 0) {
            soma -= 10;
            quantidadeAs--;
        }
    }

    public int obterSoma() {
        return soma;
    }

    public ArrayList<Carta> getMao() {
        return mao;
    }

    public void limparMao() {
        mao.clear();
        soma = 0;
        quantidadeAs = 0;
    }

    // Método abstrato para o comportamento do jogador (comprar ou parar)
    public abstract void jogar(BlackJack jogo);
}
