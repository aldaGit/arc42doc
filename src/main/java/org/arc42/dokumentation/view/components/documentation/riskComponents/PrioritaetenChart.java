package org.arc42.dokumentation.view.components.documentation.riskComponents;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PrioritaetenChart extends VerticalLayout {
  ApexChartsBuilder pie;

  public PrioritaetenChart(
      int keine, int niedrig, int mittel, int hoch, int sehrHoch, int nichterfasst) {

    this.pie =
        new ApexChartsBuilder()
            .withChart(ChartBuilder.get().withType(Type.PIE).build())
            .withLabels("keine", "niedrig", "mittel", "hoch", "sehr hoch", "nicht erfasst")
            .withColors("#C70039", "#FF5733", "#FFC300", "#2F5FF5", "#9FF781", "#E343DE")
            .withLegend(
                LegendBuilder.get().withPosition(Position.BOTTOM).withFontSize("10px").build())
            .withSeries(
                Double.valueOf(keine),
                Double.valueOf(niedrig),
                Double.valueOf(mittel),
                Double.valueOf(hoch),
                Double.valueOf(sehrHoch),
                Double.valueOf(nichterfasst))
            .withResponsive(
                ResponsiveBuilder.get()
                    .withBreakpoint(480.0)
                    .withOptions(
                        OptionsBuilder.get()
                            .withLegend(LegendBuilder.get().withPosition(Position.BOTTOM).build())
                            .build())
                    .build());
    this.add(this.pie.build());
  }

  public PrioritaetenChart(Integer[] prioritaeten) {
    this(
        prioritaeten[0],
        prioritaeten[1],
        prioritaeten[2],
        prioritaeten[3],
        prioritaeten[4],
        prioritaeten[5]);
  }

  public String getHtml() {
    return "hhaha";
  }
}
