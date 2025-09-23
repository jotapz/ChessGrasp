package util.classes;

import pecas.Peca;
import util.enume.TiposPecas;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Pontuacao {
    Map<TiposPecas, Integer> pontu;

    private int pontosCapturadosPorBrancas = 0;
    private int pontosCapturadosPorPretas = 0;

    public Pontuacao(){
        pontu = new EnumMap<>(TiposPecas.class);
        pontu.put(TiposPecas.PEAO, 1);
        pontu.put(TiposPecas.CAVALO, 3);
        pontu.put(TiposPecas.BISPO, 3);
        pontu.put(TiposPecas.TORRE, 5);
        pontu.put(TiposPecas.RAINHA, 9);
        pontu.put(TiposPecas.REI, 0); // O rei não tem valor de pontuação
    }

    public int getValor(TiposPecas tipo) {
        // Usa getOrDefault para retornar 0 se o tipo de peça não estiver no mapa
        return pontu.getOrDefault(tipo, 0);
    }

    // Calcula a pontuação total inicial para um jogador

    public void registrarCaptura(Peca p) {
        if (p == null) return;

        int valor = getValor(p.tipo);
        if (p.ehBranco) {
            // peça branca capturada -> pontos para pretas
            pontosCapturadosPorPretas += valor;
        } else {
            // peça preta capturada -> pontos para brancas
            pontosCapturadosPorBrancas += valor;
        }
    }

    public int getPontosCapturadosPorBrancas() {
        return pontosCapturadosPorBrancas;
    }

    public int getPontosCapturadosPorPretas() {
        return pontosCapturadosPorPretas;
    }

    // Não utilizados mais
    private int calculaTotalInicial() {
        int total = 0;
        total += getValor(TiposPecas.PEAO) * 8;
        total += getValor(TiposPecas.CAVALO) * 2;
        total += getValor(TiposPecas.BISPO) * 2;
        total += getValor(TiposPecas.TORRE) * 2;
        total += getValor(TiposPecas.RAINHA);
        return total;
    }

    // Calcula a pontuação das peças capturadas
    public int calculaPontuacaoCapturada(List<Peca> pecasNoTabuleiro, boolean ehBranco){
        int totalAtual = 0;
        for (Peca p: pecasNoTabuleiro){
            if (p.ehBranco == ehBranco){
                totalAtual += getValor(p.tipo);
            }
        }
        return calculaTotalInicial() - totalAtual;
    }
}