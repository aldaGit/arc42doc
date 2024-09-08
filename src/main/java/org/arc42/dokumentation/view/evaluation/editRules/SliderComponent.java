package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.addons.PaperSlider;

public class SliderComponent extends VerticalLayout {

  private final PaperSlider slider;

  public SliderComponent(String header, String width, int min, int max, int value) {
    VerticalLayout sliderVL = new VerticalLayout();
    Span headerS = new Span(header);
    HorizontalLayout sliderHL = new HorizontalLayout();
    Span minS = new Span(String.valueOf(min));
    Span maxS = new Span(String.valueOf(max));
    PaperSlider slider = new PaperSlider(value, min, max);
    slider.setWidth(width);
    slider.showValueWhenSliding();
    slider.setStep(1);
    slider.setPrimaryColor("var(--lumo-primary-text-color)");
    sliderHL.add(minS, slider, maxS);
    sliderVL.add(headerS, sliderHL);
    this.slider = slider;
    add(sliderVL);
  }

  public PaperSlider getSlider() {
    return slider;
  }
}
