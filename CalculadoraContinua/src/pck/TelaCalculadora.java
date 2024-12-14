package pck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URI;
import java.util.Arrays;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class TelaCalculadora extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextArea inputArea;
    private JLabel resultadoLabel;

    private static final String IMAGE_PATH = "C:\\Users\\USER\\eclipse-workspace\\CalculadoraMediana\\src\\img\\Sinop.jpg";
    private static final String LINKEDIN_ICON_PATH = "C:\\Users\\USER\\eclipse-workspace\\CalculadoraMediana\\src\\img\\linkedin.png";
    private static final String GITHUB_ICON_PATH = "C:\\Users\\USER\\eclipse-workspace\\CalculadoraMediana\\src\\img\\github.png";

    private CalculadoraContinua calculadora = new CalculadoraContinua();

    private JPanel boxPlotPanel; // Painel para o gráfico de Box-Plot
	
    public TelaCalculadora() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\USER\\Downloads\\estatisticas.png"));
        setTitle("Calculadora Estatística Contínua");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        JPanel entradaResultadosPanel = criarPainelEntradaResultados();
        tabbedPane.addTab("Entrada e Resultados", entradaResultadosPanel);

        JPanel conclusoesPanel = criarPainelConclusoes();
        tabbedPane.addTab("Conclusões e Fórmulas", conclusoesPanel);

        JPanel boxPlotPanel = criarPainelBoxPlot();
        tabbedPane.addTab("Box-Plot", boxPlotPanel);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel rodapePanel = criarRodape();
        getContentPane().add(rodapePanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    // Painel de entrada e resultados
    private JPanel criarPainelEntradaResultados() {
        JPanel panel = new JPanel(new BorderLayout());

        inputArea = new JTextArea(3, 30);
        inputArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane inputScroll = new JScrollPane(inputArea);

        JLabel inputLabel = new JLabel("Digite os dados em formato: [1,2,3] ou [a,b,c,...]:");
        inputLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        inputLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton calcularButton = new JButton("Calcular ");
        calcularButton.setIcon(new ImageIcon(TelaCalculadora.class.getResource("/img/igual.png")));
        calcularButton.setHorizontalAlignment(SwingConstants.LEADING);
        calcularButton.setFont(new Font("SansSerif", Font.BOLD, 17));
        calcularButton.setBackground(Color.LIGHT_GRAY);
        calcularButton.setForeground(Color.BLUE);
        calcularButton.addActionListener(new CalcularEstatisticasListener());

        resultadoLabel = new JLabel("<html><b>Média: <br>Dispersão: <br>Variância: <br>Desvio Padrão: <br>Coef. de Variação: </b></html>");
        resultadoLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        resultadoLabel.setVerticalAlignment(SwingConstants.TOP);
        resultadoLabel.setBorder(BorderFactory.createTitledBorder("Resultados"));

        panel.add(inputLabel, BorderLayout.NORTH);
        panel.add(inputScroll, BorderLayout.CENTER);
        panel.add(calcularButton, BorderLayout.EAST);
        panel.add(resultadoLabel, BorderLayout.SOUTH);

        return panel;
    }

    // Painel de conclusões e fórmulas
    private JPanel criarPainelConclusoes() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel formulasPanel = new JPanel();
        formulasPanel.setLayout(new BoxLayout(formulasPanel, BoxLayout.Y_AXIS));

        // Fórmulas já existentes
        JLabel tituloDispersao = new JLabel("Dispersão:");
        tituloDispersao.setFont(new Font("SansSerif", Font.BOLD, 16));
        TeXFormula formulaDispersao = new TeXFormula("Dispersão = \\frac{\\sum_{i=1}^{n} (X_i - \\mu)^2}{n}");

        JLabel tituloVariancia = new JLabel("Variância:");
        tituloVariancia.setFont(new Font("SansSerif", Font.BOLD, 16));
        TeXFormula formulaVariancia = new TeXFormula("S^2 = \\frac{\\sum_{i=1}^{n} (X_i - \\bar{X})^2}{n - 1}");

        JLabel tituloDesvioPadrao = new JLabel("Desvio Padrão:");
        tituloDesvioPadrao.setFont(new Font("SansSerif", Font.BOLD, 16));
        TeXFormula formulaDesvioPadrao = new TeXFormula("S = \\sqrt{S^2}");

        JLabel tituloCoefVariacao = new JLabel("Coeficiente de Variação:");
        tituloCoefVariacao.setFont(new Font("SansSerif", Font.BOLD, 16));
        TeXFormula formulaCoefVariacao = new TeXFormula("CV = \\frac{S}{\\bar{X}} \\times 100");

        // Fórmulas do Box-Plot
        JLabel tituloBoxPlot = new JLabel("Análise Box-Plot:");
        tituloBoxPlot.setFont(new Font("SansSerif", Font.BOLD, 16));
        TeXFormula formulaBoxPlot = new TeXFormula("Q1 = \\text{Percentil de 25%}, Q2 = \\text{Mediana (Percentil de 50%)}, Q3 = \\text{Percentil de 75%}");
        TeXFormula formulaIQR = new TeXFormula("IQR = Q3 - Q1");
        TeXFormula formulaLI = new TeXFormula("LI = Q1 - 1.5 \\times IQR");
        TeXFormula formulaLS = new TeXFormula("LS = Q3 + 1.5 \\times IQR");

        // Adicionando fórmulas ao painel
        formulasPanel.add(tituloDispersao);
        formulasPanel.add(new JLabel(formulaDispersao.createTeXIcon(TeXFormula.BOLD, 20)));

        formulasPanel.add(tituloVariancia);
        formulasPanel.add(new JLabel(formulaVariancia.createTeXIcon(TeXFormula.BOLD, 20)));

        formulasPanel.add(tituloDesvioPadrao);
        formulasPanel.add(new JLabel(formulaDesvioPadrao.createTeXIcon(TeXFormula.BOLD, 20)));

        formulasPanel.add(tituloCoefVariacao);
        formulasPanel.add(new JLabel(formulaCoefVariacao.createTeXIcon(TeXFormula.BOLD, 20)));

        formulasPanel.add(tituloBoxPlot);
        formulasPanel.add(new JLabel(formulaBoxPlot.createTeXIcon(TeXFormula.BOLD, 20)));
        formulasPanel.add(new JLabel(formulaIQR.createTeXIcon(TeXFormula.BOLD, 20)));
        formulasPanel.add(new JLabel(formulaLI.createTeXIcon(TeXFormula.BOLD, 20)));
        formulasPanel.add(new JLabel(formulaLS.createTeXIcon(TeXFormula.BOLD, 20)));

        JScrollPane formulasScroll = new JScrollPane(formulasPanel);

        JLabel titulo = new JLabel("Fórmulas e Conclusões Estatísticas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(formulasScroll, BorderLayout.CENTER);

        return panel;
    }


    // Criar painel de Box-Plot (inicialmente vazio)
    private JPanel criarPainelBoxPlot() {
        boxPlotPanel = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("Box-Plot dos Dados");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel aviso = new JLabel("Insira os dados na aba de entrada para gerar o Box-Plot.");
        aviso.setFont(new Font("SansSerif", Font.ITALIC, 14));
        aviso.setHorizontalAlignment(SwingConstants.CENTER);

        boxPlotPanel.add(titulo, BorderLayout.NORTH);
        boxPlotPanel.add(aviso, BorderLayout.CENTER);

        return boxPlotPanel;
    }

    // Rodapé com ícones e logo
    private JPanel criarRodape() {
        JPanel rodapePanel = new JPanel(new BorderLayout());
        JPanel iconesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel linkedinLabel = criarIcone(LINKEDIN_ICON_PATH, "https://www.linkedin.com/in/marcos-junior-da-silva-242a26186/");
        JLabel githubLabel = criarIcone(GITHUB_ICON_PATH, "https://github.com/MANTHUS01");

        iconesPanel.add(linkedinLabel);
        iconesPanel.add(githubLabel);

        try {
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File(IMAGE_PATH)));
            Image image = imageIcon.getImage().getScaledInstance(180, 50, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            rodapePanel.add(imageLabel, BorderLayout.EAST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        rodapePanel.add(iconesPanel, BorderLayout.WEST);
        return rodapePanel;
    }

    private JLabel criarIcone(String caminho, String link) {
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new File(caminho)));
            Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaledImage));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openLink(link);
                }
            });
            return label;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JLabel();
    }

    private void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Listener do botão de cálculo
    private class CalcularEstatisticasListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String inputText = inputArea.getText().replaceAll("[\\[\\]\\s]", "");
                String[] inputArray = inputText.split(",");
                double[] dados = Arrays.stream(inputArray)
                                       .mapToDouble(Double::parseDouble)
                                       .sorted() // Ordenação para cálculos de quartis
                                       .toArray();

                // Cálculos básicos
                double media = calculadora.calcularMedia(dados);
                double dispersao = calculadora.calcularDispersao(dados, media);
                double variancia = calculadora.calcularVariancia(dados, media);
                double desvioPadrao = calculadora.calcularDesvioPadrao(variancia);
                double coefVariacao = calculadora.calcularCoeficienteVariacao(desvioPadrao, media);

                // Cálculos do Boxplot
                double Q1 = calcularPercentil(dados, 25);
                double Q2 = calcularPercentil(dados, 50); // Mediana
                double Q3 = calcularPercentil(dados, 75);
                double IQR = Q3 - Q1;
                double LI = Q1 - 1.5 * IQR;
                double LS = Q3 + 1.5 * IQR;

                // Identificar outliers
                String outliers = Arrays.stream(dados)
                                        .filter(x -> x < LI || x > LS)
                                        .mapToObj(String::valueOf)
                                        .reduce("", (a, b) -> a + b + ", ");

                // Atualizar gráfico de Boxplot
                atualizarBoxPlot(dados, Q1, Q2, Q3, LI, LS);

                // Exibir resultados
                resultadoLabel.setText(String.format(
                    "<html><b>Média: %.2f<br>Dispersão: %.2f<br>Variância: %.2f<br>Desvio Padrão: %.2f<br>Coef. de Variação: %.2f%%<br>Q1: %.2f<br>Q2 (Mediana): %.2f<br>Q3: %.2f<br>LI: %.2f<br>LS: %.2f<br>Outliers: %s</b></html>",
                    media, dispersao, variancia, desvioPadrao, coefVariacao, Q1, Q2, Q3, LI, LS, outliers.isEmpty() ? "Nenhum" : outliers
                ));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(TelaCalculadora.this, "Erro ao processar os dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para calcular percentil
    private double calcularPercentil(double[] dados, double percentil) {
        double pos = (percentil / 100.0) * (dados.length - 1);
        int base = (int) Math.floor(pos);
        double resto = pos - base;
        if (base + 1 < dados.length) {
            return dados[base] + resto * (dados[base + 1] - dados[base]);
        } else {
            return dados[base];
        }
    }

    // Método para atualizar o gráfico de Boxplot
 // Método para atualizar o gráfico de Boxplot
    private void atualizarBoxPlot(double[] dados, double Q1, double Q2, double Q3, double LI, double LS) {
        try {
            DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

            // Calcular valores mínimos e máximos
            double min = Arrays.stream(dados).filter(x -> x >= LI).min().orElse(Double.NaN);
            double max = Arrays.stream(dados).filter(x -> x <= LS).max().orElse(Double.NaN);

            // Adicionar dados ao dataset
            dataset.add(Arrays.asList(min, Q1, Q2, Q3, max), "Boxplot", "Dados");

            // Criar o gráfico
            JFreeChart boxPlot = ChartFactory.createBoxAndWhiskerChart(
                    "Box-Plot",  // Título do gráfico
                    "Dados",  // Eixo X
                    "Valores",  // Eixo Y
                    dataset,
                    false  // Legenda desativada
            );

            // Configuração do eixo Y (garante que o eixo abranja os valores desejados)
            CategoryPlot plot = (CategoryPlot) boxPlot.getPlot();
            NumberAxis rangeAxis = new NumberAxis("Valores");
            rangeAxis.setRange(Math.min(min, LI) - 10, Math.max(max, LS) + 10);  // Ajuste a faixa do eixo Y
            plot.setRangeAxis(rangeAxis);

            // Configurar o gráfico
            plot.setDomainGridlinesVisible(true);

            // Personalizar o renderizador
            BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
            renderer.setFillBox(true); // Preencher a caixa
            renderer.setMaximumBarWidth(0.20); // Aumentar a largura da caixa
            renderer.setUseOutlinePaintForWhiskers(true); // Usar cor de contorno para os bigodes
            renderer.setSeriesPaint(0, Color.CYAN); // Cor da caixa
            renderer.setMedianVisible(true); // Certificar que a mediana está visível
            renderer.setMeanVisible(false); // Ocultar o ponto médio, se necessário
            plot.setRenderer(renderer);

            // Adicionar bolinhas para os limites LI e LS
            // Bolinha para LI (Limite Inferior)
            ValueMarker liMarker = new ValueMarker(LI);
            liMarker.setPaint(Color.RED);  // Cor vermelha para o limite inferior
            liMarker.setLabel("LI");
            liMarker.setLabelAnchor(RectangleAnchor.BOTTOM);
            liMarker.setLabelTextAnchor(TextAnchor.BOTTOM_CENTER);
            plot.addRangeMarker(liMarker);

            // Bolinha para LS (Limite Superior)
            ValueMarker lsMarker = new ValueMarker(LS);
            lsMarker.setPaint(Color.BLUE);  // Cor azul para o limite superior
            lsMarker.setLabel("LS");
            lsMarker.setLabelAnchor(RectangleAnchor.BOTTOM);
            lsMarker.setLabelTextAnchor(TextAnchor.BOTTOM_CENTER);
            plot.addRangeMarker(lsMarker);

            // Bolinha para Q1
            ValueMarker q1Marker = new ValueMarker(Q1);
            q1Marker.setPaint(Color.GREEN);  // Cor verde para Q1
            q1Marker.setLabel("Q1");
            q1Marker.setLabelAnchor(RectangleAnchor.BOTTOM);
            q1Marker.setLabelTextAnchor(TextAnchor.BOTTOM_CENTER);
            plot.addRangeMarker(q1Marker);

            // Bolinha para Q2 (Mediana)
            ValueMarker q2Marker = new ValueMarker(Q2);
            q2Marker.setPaint(Color.ORANGE);  // Cor laranja para Q2 (mediana)
            q2Marker.setLabel("Q2 (Mediana)");
            q2Marker.setLabelAnchor(RectangleAnchor.BOTTOM);
            q2Marker.setLabelTextAnchor(TextAnchor.BOTTOM_CENTER);
            plot.addRangeMarker(q2Marker);

            // Bolinha para Q3
            ValueMarker q3Marker = new ValueMarker(Q3);
            q3Marker.setPaint(Color.MAGENTA);  // Cor magenta para Q3
            q3Marker.setLabel("Q3");
            q3Marker.setLabelAnchor(RectangleAnchor.BOTTOM);
            q3Marker.setLabelTextAnchor(TextAnchor.BOTTOM_CENTER);
            plot.addRangeMarker(q3Marker);

            // Criar a expressão LaTeX para as anotações
            String latexFormula = String.format(
                    "\\textbf{Md: %.2f} \\\\ \\textbf{Q1: %.2f} \\\\ \\textbf{Q3: %.2f} \\\\ \\textbf{IQR: %.2f} \\\\ \\textbf{LI: %.2f} \\\\ \\textbf{LS: %.2f}",
                    Q2, Q1, Q3, (Q3 - Q1), LI, LS
            );

            // Criar o painel com o layout dividido
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // Configurar painel do gráfico
            ChartPanel chartPanel = new ChartPanel(boxPlot);
            chartPanel.setPreferredSize(new Dimension(500, 600)); // Ajuste para o gráfico

            // Configurar o painel das anotações
            JPanel annotationsPanel = new JPanel();
            annotationsPanel.setLayout(new BorderLayout());

            // Título das anotações (negrito e itálico)
            JLabel titleLabel = new JLabel("Dados", JLabel.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 16)); // Negrito e itálico, tamanho 16
            annotationsPanel.add(titleLabel, BorderLayout.NORTH);

            // Renderizar o LaTeX com JLaTeXMath
            TeXFormula formula = new TeXFormula(latexFormula);
            TeXIcon icon = formula.createTeXIcon(TeXFormula.BOLD, 20); // Ajuste o tamanho do ícone (20 é o tamanho do texto)

            // Criar o JLabel para exibir o LaTeX
            JLabel latexLabel = new JLabel(icon);
            annotationsPanel.add(latexLabel, BorderLayout.CENTER); // Adicionar o JLabel com o ícone LaTeX ao painel

            // Adicionar os componentes ao painel principal
            panel.add(chartPanel, BorderLayout.CENTER);
            panel.add(annotationsPanel, BorderLayout.EAST);

            // Ajustar o painel do box plot para exibir as anotações ao lado
            boxPlotPanel.removeAll();
            boxPlotPanel.add(panel, BorderLayout.CENTER);

            // Atualizar a janela
            getContentPane().revalidate();
            getContentPane().repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar o gráfico de Box-Plot.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaCalculadora frame = new TelaCalculadora();
            frame.setVisible(true);
        });
    }
}
