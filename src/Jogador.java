import java.util.ArrayList;

public class Jogador {
    private ArrayList<Carta> mao;
    private int soma;
    private int quantidadeAs;

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
}
