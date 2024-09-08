package org.arc42.dokumentation.view.util;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxXmlUtils;
import java.awt.*;
import java.util.List;
import java.util.Stack;
import org.arc42.dokumentation.model.dto.documentation.QualityDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityScenarioDTO;
import org.arc42.dokumentation.view.components.customComponents.BadgeComponent;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.w3c.dom.Document;

public class GraphBuilder {

  static SimpleDirectedWeightedGraph<String, RelationshipEdge> g;
  private Stack<String[]> stack;

  public static SimpleDirectedWeightedGraph<String, RelationshipEdge> getG() {
    return g;
  }

  public void createGraph(List<QualityDTO> qualityGoalDTOS) {
    g = new SimpleDirectedWeightedGraph<>(RelationshipEdge.class);
    String utility = "Utility";
    g.addVertex(utility);
    stack = new Stack<>();

    for (QualityDTO quality : qualityGoalDTOS) {
      for (Object criteria : quality.getQualityCriteria()) {
        BadgeComponent badgeComponent = new BadgeComponent();
        this.stack.push(
            new String[] {
              (String) criteria,
              badgeComponent.getBadge((String) criteria).getStyle().get("background-color")
            });
        g.addVertex((String) criteria);
        g.addVertex(quality.toString());
        g.addEdge(utility, (String) criteria, new RelationshipEdge());
        RelationshipEdge relationshipEdge = new RelationshipEdge();
        if (quality instanceof QualityScenarioDTO) {
          g.setEdgeWeight(
              relationshipEdge,
              ((QualityScenarioDTO) quality).getWeight(((QualityScenarioDTO) quality).getRisk())
                  + ((QualityScenarioDTO) quality)
                      .getWeight(((QualityScenarioDTO) quality).getCurrentPriority()));
          if (((QualityScenarioDTO) quality).getQualityGoalDTO() != null) {
            g.addEdge(
                (String) criteria,
                ((QualityScenarioDTO) quality).getQualityGoalDTO().toString(),
                new RelationshipEdge());
            g.addEdge(
                ((QualityScenarioDTO) quality).getQualityGoalDTO().toString(),
                quality.toString(),
                relationshipEdge);
          } else {
            g.addEdge((String) criteria, (quality).toString(), relationshipEdge);
          }
        } else {
          g.setEdgeWeight(relationshipEdge, 1.0);
          g.addEdge((String) criteria, (quality).toString(), relationshipEdge);
        }
      }
    }
  }

  public String generateGraph(List<QualityDTO> qualityGoalDTOS) {
    createGraph(qualityGoalDTOS);
    JGraphXAdapter<String, RelationshipEdge> graphAdapter = new JGraphXAdapter<>(g);
    mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);

    Object[] nodes = graphAdapter.getChildCells(graphAdapter.getDefaultParent());

    while (!stack.isEmpty()) {
      String[] temp = stack.pop();
      for (Object o : nodes) {
        if (graphAdapter.getModel().isVertex(o) && graphAdapter.getLabel(o).equals(temp[0])) {
          mxCell vertex = (mxCell) o;
          graphAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, temp[1], new Object[] {vertex});
          graphAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR, "white", new Object[] {vertex});
          graphAdapter.setCellStyles(mxConstants.STYLE_STROKECOLOR, temp[1], new Object[] {vertex});
        } else if (graphAdapter.getModel().isVertex(o)
            && graphAdapter.getLabel(o).startsWith("QS")) {
          mxCell vertex = (mxCell) o;
          graphAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#5AFFA1", new Object[] {vertex});
          graphAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR, "black", new Object[] {vertex});
          graphAdapter.setCellStyles(
              mxConstants.STYLE_STROKECOLOR, "#5AFFA1", new Object[] {vertex});
        } else if (graphAdapter.getModel().isVertex(o)
            && graphAdapter.getLabel(o).startsWith("QZ")) {
          mxCell vertex = (mxCell) o;
          graphAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#4DDBE3", new Object[] {vertex});
          graphAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR, "black", new Object[] {vertex});
          graphAdapter.setCellStyles(
              mxConstants.STYLE_STROKECOLOR, "#4DDBE3", new Object[] {vertex});
        }
      }
    }

    layout.execute(graphAdapter.getDefaultParent());
    Document document =
        mxCellRenderer.createSvgDocument(graphAdapter, null, 1.6, Color.WHITE, null);
    return mxXmlUtils.getXml(document);
  }

  public static class RelationshipEdge extends DefaultWeightedEdge {
    public RelationshipEdge() {}

    @Override
    public String toString() {
      return "";
    }

    @Override
    public Object getTarget() {
      return super.getTarget();
    }

    @Override
    public double getWeight() {
      return super.getWeight();
    }
  }
}
