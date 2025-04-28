package blackjack.Jogador;

import blackjack.acao.JogadorAcao;
import blackjack.modelo.BlackJack;
import gui.Janela;

public class JogadorHumano extends Jogador implements JogadorAcao {

    private Janela janela;

    public JogadorHumano(Janela janela) {
        super();
        this.janela = janela;
    }

    @Override
    public void comprarCarta() {
        if (obterSoma() <= 21) {
            janela.getJogo().jogadorCompra();
            janela.repaint();

            if (obterSoma() > 21) {
                janela.mostrarMensagem("Você estourou!");
                janela.desabilitarBotoes();
            }
        }
    }

    @Override
    public void parar() {
        janela.desabilitarBotoes();
        janela.getJogo().dealerJoga();
        janela.repaint();
        verificarVencedor();
    }

    private void verificarVencedor() {
        int jogadorSoma = obterSoma();
        int dealerSoma = janela.getJogo().getDealer().obterSoma();

        if (jogadorSoma > 21) {
            janela.mostrarMensagem("Você perdeu!");
        } else if (dealerSoma > 21) {
            janela.mostrarMensagem("Você venceu!");
        } else if (jogadorSoma > dealerSoma) {
            janela.mostrarMensagem("Você venceu!");
        } else if (jogadorSoma < dealerSoma) {
            janela.mostrarMensagem("Você perdeu!");
        } else {
            janela.mostrarMensagem("Empate!");
        }
    }

    @Override
    public void jogar(BlackJack jogo) {
    }
}
