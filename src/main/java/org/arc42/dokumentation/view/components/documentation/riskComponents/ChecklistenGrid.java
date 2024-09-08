package org.arc42.dokumentation.view.components.documentation.riskComponents;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO;

public class ChecklistenGrid {
  RisikoDTO risikenDTO;

  public ChecklistenGrid() {
    if (risikenDTO == null) {
      risikenDTO = new RisikoDTO();
    }
  }

  public Grid<RisikoDTO> generateGrid() {
    Grid<RisikoDTO> risikenGrid = new Grid<>(RisikoDTO.class, false);
    risikenGrid.addDragEndListener(e -> risikenDTO = null);
    risikenGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

    risikenGrid.setAllRowsVisible(true);
    risikenGrid.addColumn(RisikoDTO::getId).setHeader("Risiko-ID").setFrozen(true);
    risikenGrid
        .addColumn(RisikoDTO::getAnforderung)
        .setHeader("Risikobeschreibung")
        .setFrozen(true);
    risikenGrid.addColumn(RisikoDTO::getSchadenshoehe).setHeader("Schadenshöhe").setFrozen(true);
    risikenGrid.addColumn(RisikoDTO::ewk).setHeader("Eintrittswahrscheinlichkeit").setFrozen(true);
    risikenGrid.addColumn(RisikoDTO::getStatus).setHeader("Status").setFrozen(true);
    risikenGrid.setWidthFull();

    risikenGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

    return risikenGrid;
  }

  public void setDrop(Grid<RisikoDTO> risikenGrid, GridListDataView<RisikoDTO> allGridData) {

    risikenGrid.setRowsDraggable(true);
    risikenGrid.setDropMode(GridDropMode.BETWEEN);

    risikenGrid.addDragStartListener(e -> risikenDTO = e.getDraggedItems().get(0));

    risikenGrid.addDropListener(
        e -> {
          RisikoDTO targetPerson = e.getDropTargetItem().orElse(null);
          GridDropLocation dropLocation = e.getDropLocation();

          boolean personWasDroppedOntoItself = risikenDTO.equals(targetPerson);

          if (targetPerson == null || personWasDroppedOntoItself) return;

          allGridData.removeItem(risikenDTO);

          if (dropLocation == GridDropLocation.BELOW) {
            allGridData.addItemAfter(risikenDTO, targetPerson);
          } else {
            allGridData.addItemBefore(risikenDTO, targetPerson);
          }
        });
  }
}
