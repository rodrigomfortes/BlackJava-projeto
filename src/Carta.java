public class Carta {
    private String valor;
    private String naipe;

    public Carta(String valor, String naipe) {
        this.valor = valor;
        this.naipe = naipe;
    }

    public String toString() {
        return valor + "-" + naipe;
    }

    public String getCaminhoImagem() {
        return "/cards/" + toString() + ".png";
    }

    public int getValor() {
        if ("JQK".contains(valor)) return 10;
        if (valor.equals("A")) return 11;
        return Integer.parseInt(valor);
    }

    public boolean ehAs() {
        return valor.equals("A");
    }

    public String getValorString() {
        return valor;
    }

    public String getNaipe() {
        return naipe;
    }
}
