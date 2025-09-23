package util.classes;

import game.Tabuleiro;
import pecas.*;

import java.util.ArrayList;
import java.util.List;

public class FabricadorPecas {



    public static List<Peca> criarPecas(Tabuleiro tab){
        List<Peca> list = new ArrayList<>();

        // Pretas:
        list.add(new Rei(tab, 0, 4, false));
        list.add(new Rainha(tab, 0, 3, false));
        list.add(new Bispo(tab, 0, 2, false));
        list.add(new Bispo(tab, 0, 5, false));
        list.add(new Torre(tab, 0, 0, false));
        list.add(new Torre(tab, 0, 7, false));
        list.add(new Cavalo(tab, 0, 1, false));
        list.add(new Cavalo(tab, 0, 6, false));
        list.add(new Peao(tab, 1, 0, false));
        list.add(new Peao(tab, 1, 1, false));
        list.add(new Peao(tab, 1, 2, false));
        list.add(new Peao(tab, 1, 3, false));
        list.add(new Peao(tab, 1, 4, false));
        list.add(new Peao(tab, 1, 5, false));
        list.add(new Peao(tab, 1, 6, false));
        list.add(new Peao(tab, 1, 7, false));

        // Brancas:
        list.add(new Rei(tab, 7, 4, true));
        list.add(new Rainha(tab, 7, 3, true));
        list.add(new Bispo(tab, 7, 2, true));
        list.add(new Bispo(tab, 7, 5, true));
        list.add(new Torre(tab, 7, 0, true));
        list.add(new Torre(tab, 7, 7, true));
        list.add(new Cavalo(tab, 7, 1, true));
        list.add(new Cavalo(tab, 7, 6, true));
        list.add(new Peao(tab, 6, 0, true));
        list.add(new Peao(tab, 6, 1, true));
        list.add(new Peao(tab, 6, 2, true));
        list.add(new Peao(tab, 6, 3, true));
        list.add(new Peao(tab, 6, 4, true));
        list.add(new Peao(tab, 6, 5, true));
        list.add(new Peao(tab, 6, 6, true));
        list.add(new Peao(tab, 6, 7, true));

        return list;
    }

    public static Peca promocaoPeao(String tipo, Tabuleiro tab, int linha, int coluna, boolean ehBranco){
        switch (tipo) {
            case "Torre": return new Torre(tab, linha, coluna, ehBranco);
            case "Bispo": return new Bispo(tab, linha, coluna, ehBranco);
            case "Cavalo": return new Cavalo(tab, linha, coluna, ehBranco);
            default: return new Rainha(tab, linha, coluna, ehBranco);
        }


    }
}
