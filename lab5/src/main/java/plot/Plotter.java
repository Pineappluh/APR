package plot;

import math.Matrix;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Plotter {

    private JFrame frame;

    public Plotter() {
        frame = new JFrame();
        frame.setLayout(new GridLayout(0, 2));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1920, 1080));
    }

    public void showPlots(String title) {
        SwingUtilities.invokeLater(() -> {
            frame.setTitle(title);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public void addPlot(String title, List<Matrix> X, double T)  {
        XYSeriesCollection datasets = createDatasets(X, T);

        JFreeChart chart = ChartFactory.createXYLineChart(title, "t", "x", datasets);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);

        frame.add(chartPanel);
    }

    private XYSeriesCollection createDatasets(List<Matrix> X, double T) {
        List<XYSeries> series = new ArrayList<>();
        for (int i = 0; i < X.get(0).getRows(); i++) {
            series.add(new XYSeries("x" + i));
        }

        for (int i = 0; i < X.size(); i++) {
            Matrix x = X.get(i);
            for (int j = 0; j < x.getRows(); j++) {
                series.get(j).add((double) i * T, x.getElement(0, j));
            }
        }

        XYSeriesCollection datasets = new XYSeriesCollection();
        series.forEach(datasets::addSeries);

        return datasets;
    }
}
