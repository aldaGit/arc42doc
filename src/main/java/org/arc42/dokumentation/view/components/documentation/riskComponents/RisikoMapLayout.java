package org.arc42.dokumentation.view.components.documentation.riskComponents;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.Grid;
import com.github.appreciated.apexcharts.config.Legend;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ToolbarBuilder;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.grid.Xaxis;
import com.github.appreciated.apexcharts.config.grid.xaxis.Lines;
import com.github.appreciated.apexcharts.config.yaxis.AxisTicks;
import com.github.appreciated.apexcharts.config.yaxis.Labels;
import com.github.appreciated.apexcharts.config.yaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.config.yaxis.title.builder.StyleBuilder;
import com.github.appreciated.apexcharts.helper.Formatter;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.ArrayList;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Schadenshoehe;

public class RisikoMapLayout extends VerticalLayout {

  ApexChartsBuilder riskMap;
  ApexCharts savedApex;
  Button generate = new Button("Generiere Map");
  private ArrayList<Series<Double>> existingSeries = new ArrayList<>();
  int lastRenderHash = 10;
  private boolean globalPadding = true;

  public RisikoMapLayout(boolean globalPadding) {

    this.globalPadding = globalPadding;

    Grid funkyGrid = new Grid();
    Xaxis xaxis = new Xaxis();

    Lines lines = new Lines();

    xaxis.setLines(lines);

    com.github.appreciated.apexcharts.config.grid.Yaxis yaxis =
        new com.github.appreciated.apexcharts.config.grid.Yaxis();
    com.github.appreciated.apexcharts.config.grid.yaxis.Lines ylines =
        new com.github.appreciated.apexcharts.config.grid.yaxis.Lines();
    ylines.setShow(true);
    yaxis.setLines(ylines);

    funkyGrid.setXaxis(xaxis);
    funkyGrid.setYaxis(yaxis);

    funkyGrid.getXaxis().getLines().setShow(true);
    funkyGrid.getYaxis().getLines().setShow(true);
    // this.existingSeries = new ArrayList<>();

    Formatter labelFormatter =
        new Formatter() {
          @Override
          public String getString() {
            return "function (val) {if (val == 0) { return 'Keine';} else if (val == 1) { return"
                + " 'Gering';} else if (val == 2) { return 'Mittel';} else if (val == 3) {"
                + " return 'Hoch';} else if (val == 4) { return ['Sehr', 'hoch'];} return '';}";
          }
        };

    AxisTicks at = new AxisTicks();
    Labels occurrenceLabels = new Labels();
    occurrenceLabels.setFormatter(labelFormatter.getString());
    //
    occurrenceLabels.setPadding(35.0);
    occurrenceLabels.setMinWidth(40.0);
    occurrenceLabels.setOffsetX(-10.0);
    occurrenceLabels.setShow(true);
    at.setWidth(3.0);
    com.github.appreciated.apexcharts.config.legend.Labels labels =
        new com.github.appreciated.apexcharts.config.legend.Labels();

    Legend legend = new Legend();
    legend.setLabels(labels);
    legend.setShow(true);
    legend.setShowForSingleSeries(true);
    legend.setShowForNullSeries(false);
    new com.github.appreciated.apexcharts.config.yaxis.Title();
    com.github.appreciated.apexcharts.config.xaxis.Title schadenshoehe =
        new com.github.appreciated.apexcharts.config.xaxis.Title();
    schadenshoehe.setText("Schadenshöhe");
    schadenshoehe.setStyle(
        com.github.appreciated.apexcharts.config.xaxis.title.builder.StyleBuilder.get()
            .withFontSize("15px")
            .build());

    // eintritt.setRotate(90);
    // eintritt.setOffsetX(-2.0);
    this.riskMap =
        new ApexChartsBuilder()
            .withChart(
                ChartBuilder.get()
                    .withType(Type.SCATTER)
                    .withToolbar(ToolbarBuilder.get().withShow(false).build())
                    .withZoom(ZoomBuilder.get().withEnabled(false).build())
                    .build())
            .withSeries(new Series<>(" "))
            .withXaxis(XAxisBuilder.get().withTitle(schadenshoehe).withMax(5.0).build())
            .withYaxis(
                YAxisBuilder.get()
                    .withAxisTicks(at)
                    .withTickAmount(5.0)
                    .withLabels(occurrenceLabels)
                    .withTitle(
                        TitleBuilder.get()
                            .withText("Eintrittswahrscheinlichkeit")
                            .withRotate(-90.0)
                            .withOffsetX(20.0)
                            .withStyle(StyleBuilder.get().withFontSize("15px").build())
                            .build())
                    .withShowAlways(true)
                    .withMin(0.0)
                    .withMax(5.0)
                    .build())
            .withLegend(legend)
            .withGrid(funkyGrid)
            .withLabels(" ", "gering", "mittel", "hoch", "sehr hoch");
    savedApex = this.riskMap.build();

    if (globalPadding) {
      savedApex.getElement().getStyle().set("padding", "10px");
    }
    this.removeAll();
    this.add(savedApex);
    savedApex.render();
    clearSeries();

    savedApex.render();
  }

  public void clearSeries() {
    this.existingSeries = new ArrayList<>();
  }

  public void update(String label, int eintrittswahrscheinlichkeit, int schadenshoehe) {
    if (this.riskMap == null) {
      return;
    }

    this.removeAll();
    savedApex = this.riskMap.build();
    if (this.globalPadding) {

      savedApex.getElement().getStyle().set("padding", "90px");
    }
    switch (schadenshoehe) {
        // 0 - 4
        // todo save rest of data
      case -1:

      case 0:
        this.existingSeries.add(
            new Series<>(label, null, 0.0 + eintrittswahrscheinlichkeit, null, null, null, null));
        savedApex.updateSeries(this.existingSeries.toArray(Series[]::new));

        break;
      case 1:
        this.existingSeries.add(
            new Series<>(label, null, null, 0.0 + eintrittswahrscheinlichkeit, null, null, null));
        savedApex.updateSeries(this.existingSeries.toArray(Series[]::new));
        break;
      case 2:
        this.existingSeries.add(
            new Series<>(label, null, null, null, 0.0 + eintrittswahrscheinlichkeit, null, null));
        savedApex.updateSeries(this.existingSeries.toArray(Series[]::new));
        break;
      case 3:
        this.existingSeries.add(
            new Series<>(label, null, null, null, null, 0.0 + eintrittswahrscheinlichkeit, null));
        savedApex.updateSeries(this.existingSeries.toArray(Series[]::new));
        break;
      case 4:
        this.existingSeries.add(
            new Series<>(label, null, null, null, null, null, 0.0 + eintrittswahrscheinlichkeit));
        savedApex.updateSeries(this.existingSeries.toArray(Series[]::new));
        break;
      default:
        break;
    }
    savedApex.render();
    this.add(savedApex);
  }

  public void renderIfDataChanged() {

    // change detected: render
    if (this.existingSeries.hashCode() != lastRenderHash) {
      this.removeAll();
      this.riskMap.withSeries(this.existingSeries.toArray(Series[]::new));
      savedApex = this.riskMap.build();
      if (globalPadding) {

        savedApex.getElement().getStyle().set("padding", "90px");
      }
      this.add(savedApex);
      savedApex.render();
      lastRenderHash = this.existingSeries.hashCode();
    }
  }

  public ApexChartsBuilder getApexChartsBuilder() {
    return this.riskMap;
  }

  public static int convertEintrittswToInt(String s) {
    if (s == null) {
      return -1;
    }
      return switch (s.trim()) {
          case "niedrig", "gering" -> 1;
          case "mittel" -> 2;
          case "hoch" -> 3;
          case "sehr hoch" -> 4;
          case "keine" -> 0;
          default -> -1;
      };
  }

  public static int convertSchadenshoeheToInt(Schadenshoehe s) {
    if (s == null) {
      return -1;
    }
      return switch (s) {
          case GERING -> 1;
          case MITTEL -> 2;
          case HOCH -> 3;
          case SEHRHOCH -> 4;
          default -> -1;
      };
  }
}
