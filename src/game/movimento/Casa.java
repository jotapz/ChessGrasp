package game.movimento;

public class Casa {
    private int linha;
    private int coluna;

    public Casa(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }

    @Override
    public String toString() {
        return "" + (char)('a' + coluna) + (8 - linha);
    }
}