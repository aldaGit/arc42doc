package org.arc42.dokumentation.view.components.documentation.riskComponents;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class SWOTPieLayout extends VerticalLayout {
  ApexChartsBuilder pie;

  public SWOTPieLayout(int strengths, int weaknesses, int opportunities, int threats) {

    this.pie =
        new ApexChartsBuilder()
            .withChart(ChartBuilder.get().withType(Type.PIE).build())
            .withLabels("Strenghts", "Weaknesses", "Opportunities", "Threats")
            .withLegend(
                LegendBuilder.get().withPosition(Position.BOTTOM).withFontSize("10px").build())
            .withDataLabels(DataLabelsBuilder.get().withEnabled(true).build())
            .withSeries(
                Double.valueOf(strengths),
                Double.valueOf(weaknesses),
                Double.valueOf(opportunities),
                Double.valueOf(threats))
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

  public SWOTPieLayout(Integer[] swotStats) {
    this(swotStats[0], swotStats[1], swotStats[2], swotStats[3]);
  }

  public void update(int strengths, int weaknesses, int opportunities, int threats) {
    if (this.pie == null) {
      return;
    }
    pie.withSeries(
        Double.valueOf(strengths),
        Double.valueOf(weaknesses),
        Double.valueOf(opportunities),
        Double.valueOf(threats));
    this.removeAll();
    this.add(pie.build());
  }

  public String getHtml() {
    return "hhaha";
  }
}
