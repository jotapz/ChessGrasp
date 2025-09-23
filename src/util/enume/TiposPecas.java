package util.enume;

public enum TiposPecas {
    REI("Rei"),
    RAINHA("Rainha"),
    TORRE("Torre"),
    BISPO("Bispo"),
    CAVALO("Cavalo"),
    PEAO("Peão");

    // Atributo para guardar o nome da peça
    private final String nome;

    // Construtor privado para inicializar o nome da peça
    TiposPecas(String nome) {
        this.nome = nome;
    }

    // Método para obter o nome da peça
    public String getNome() {
        return nome;
    }
}
