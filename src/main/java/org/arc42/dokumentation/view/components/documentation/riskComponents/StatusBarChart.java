package org.arc42.dokumentation.view.components.documentation.riskComponents;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.PlotOptions;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ToolbarBuilder;
import com.github.appreciated.apexcharts.config.plotoptions.Bar;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.math.BigDecimal;
import org.arc42.dokumentation.view.components.documentation.arc42.AnalyseResult;

public class StatusBarChart extends VerticalLayout {
  ApexChartsBuilder bar;
  AnalyseResult result = new AnalyseResult();

  public StatusBarChart(int erledigt, int inBearbeitung, int unbearbeitet) {
    PlotOptions options = new PlotOptions();
    Bar barOption = new Bar();
    barOption.setColors(null);
    barOption.setHorizontal(true);
    barOption.setColumnWidth("55%");
    barOption.setDistributed(true);
    options.setBar(barOption);
    System.out.println("making chart for " + erledigt + " " + inBearbeitung + " " + unbearbeitet);
    this.bar =
        new ApexChartsBuilder()
            .withColors("#339CFF", "#45AE63", "#F3B247")
            .withChart(
                ChartBuilder.get()
                    .withType(Type.BAR)
                    .withToolbar(ToolbarBuilder.get().withShow(false).build())
                    .build())
            .withPlotOptions(options)
            .withLabels("Erledigt", "In Bearbeitung", "Unbearbeitet")
            .withDataLabels(DataLabelsBuilder.get().withEnabled(false).build())
            .withStroke(
                StrokeBuilder.get().withShow(true).withWidth(2.0).withColors("transparent").build())
            .withSeries(
                new Series<>(
                    "Status",
                    Double.valueOf(erledigt),
                    Double.valueOf(inBearbeitung),
                    Double.valueOf(unbearbeitet)))
            .withXaxis(
                XAxisBuilder.get()
                    .withMax(0.0 + result.getMaximumOfPrioCounts() + 2.0)
                    .withTickAmount(new BigDecimal(result.getMaximumOfPrioCounts() + 2.0))
                    .withRange(1.0)
                    .build())
            .withFill(FillBuilder.get().withOpacity(1.0).build());

    this.add(this.bar.build());
  }

  public StatusBarChart(Integer[] statusStats) {
    this(statusStats[0], statusStats[1], statusStats[2]);
  }

  public String getHtml() {
    return "hhaha";
  }
}
