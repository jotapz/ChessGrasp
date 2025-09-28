package game.movimento;

import pecas.Peca;

public class Movimento {
    private Peca pecaMovida;
    private Casa casaOrigem;
    private Casa casaDestino;
    private Peca pecaCapturada;

    public Movimento(Peca pecaMovida, Casa casaOrigem, Casa casaDestino, Peca pecaCapturada) {
        this.pecaMovida = pecaMovida;
        this.casaOrigem = casaOrigem;
        this.casaDestino = casaDestino;
        this.pecaCapturada = pecaCapturada;
    }
    .
    public Peca getPecaMovida() { return pecaMovida; }
    public Casa getCasaOrigem() { return casaOrigem; }
    public Casa getCasaDestino() { return casaDestino; }
    public Peca getPecaCapturada() { return pecaCapturada; }
    public boolean ehCaptura() { return pecaCapturada != null; }

    @Override
    public String toString() {
        String descricao = pecaMovida.nome + " de " + casaOrigem + " para " + casaDestino;
        if (ehCaptura()) {
            descricao += " capturando " + pecaCapturada.nome;
        }
        return descricao;
    }
}