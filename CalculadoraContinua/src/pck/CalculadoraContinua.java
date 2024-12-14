package pck;

import java.util.Arrays;

public class CalculadoraContinua {

    // Método para calcular a média
    public double calcularMedia(double[] dados) {
        return Arrays.stream(dados).average().orElse(0);
    }

    // Método para calcular a "dispersão" (na verdade, variância populacional)
    public double calcularDispersao(double[] dados, double media) {
        return Arrays.stream(dados)
                     .map(valor -> Math.pow(valor - media, 2))
                     .sum() / dados.length; // Divisão por n
    }

    // Método para calcular a variância (amostral)
    public double calcularVariancia(double[] dados, double media) {
        return Arrays.stream(dados)
                     .map(valor -> Math.pow(valor - media, 2))
                     .sum() / (dados.length - 1); // Divisão por n-1
    }

    // Método para calcular o desvio padrão
    public double calcularDesvioPadrao(double variancia) {
        return Math.sqrt(variancia); // Raiz quadrada da variância
    }

    // Método para calcular o coeficiente de variação
    public double calcularCoeficienteVariacao(double desvioPadrao, double media) {
        return (desvioPadrao / media) * 100; // Em porcentagem
    }

    // Método para calcular a variância a partir da dispersão (se necessário)
    public double calcularVariancia(double dispersao, int length) {
        return dispersao * length / (length - 1); // Ajuste para amostral
    }
}
