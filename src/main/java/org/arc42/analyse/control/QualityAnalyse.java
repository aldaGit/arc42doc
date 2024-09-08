package org.arc42.analyse.control;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.arc42.analyse.control.service.Arc42AnalyseService;
import org.arc42.dokumentation.model.dto.documentation.QualityDTO;
import org.arc42.dokumentation.view.util.GraphBuilder;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

public class QualityAnalyse {

  private static final ArrayList<String> kategorien =
      new ArrayList<>(
          Arrays.asList(
              "Funktionalität",
              "Effizienz",
              "Kompatibilität",
              "Benutzbarkeit",
              "Zuverlässigkeit",
              "Sicherheit",
              "Wartbarkeit",
              "Portabilität"));
  private final List<QualityDTO> list;
  private SimpleDirectedWeightedGraph<String, GraphBuilder.RelationshipEdge> g;
  private Set<String> nonExisitingRelation = new LinkedHashSet<>();

  public QualityAnalyse(Integer arcId) {
    Arc42AnalyseService service = Arc42AnalyseService.getInstance();
    list = service.getAllQualityGoals(arcId.toString());
  }

  public Set<String> getNonExisitingRelation() {
    return nonExisitingRelation;
  }

  public Map<String, Double> startAnalyse() {
    GraphBuilder graphBuilder = new GraphBuilder();
    graphBuilder.createGraph(list);
    this.g = GraphBuilder.getG();
    MyListener traversalListener = new MyListener(g);
    for (Object kategorie : g.vertexSet()) {
      if (kategorien.contains(kategorie)) {
        GraphIterator<String, GraphBuilder.RelationshipEdge> iterator =
            new DepthFirstIterator(g, kategorie);
        iterator.addTraversalListener(traversalListener);
        while (iterator.hasNext()) {
          iterator.next();
        }
      }
    }
    this.nonExisitingRelation = traversalListener.getNonExisitingRelation();
    return traversalListener.getWeightMap();
  }

  public List<String> getNonExistingCategories() {
    List<String> notExist = new ArrayList<>();

    for (String kategorie : kategorien) {
      if (!g.vertexSet().contains(kategorie)) notExist.add(kategorie);
    }

    return notExist;
  }

  public Map<String, Double> getWeightMapCategory() {
    return startAnalyse().entrySet().stream()
        .filter(value -> kategorien.contains(value.getKey()))
        .sorted(Comparator.comparingDouble(Map.Entry::getValue))
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (l, r) -> l, LinkedHashMap::new));
  }

  public Double getTotalWeight(Map<String, Double> map) {
    return map.values().stream().mapToDouble(d -> d).sum();
  }

  static class MyListener extends TraversalListenerAdapter<String, GraphBuilder.RelationshipEdge> {
    final SimpleDirectedGraph<String, GraphBuilder.RelationshipEdge> g;
    private final Map<String, Double> weightMap = new LinkedHashMap<>();
    private final Set<String> nonExisitingRelation = new LinkedHashSet<>();
    private boolean newComponent;
    private String reference;
    private double totalWeight = 0;

    public MyListener(SimpleDirectedGraph<String, GraphBuilder.RelationshipEdge> g) {
      this.g = g;
    }

    public Set<String> getNonExisitingRelation() {
      return nonExisitingRelation;
    }

    @Override
    public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
      newComponent = true;
    }

    @Override
    public void vertexTraversed(VertexTraversalEvent<String> e) {
      String vertex = e.getVertex();

      if (newComponent) {
        reference = vertex;
        newComponent = false;
      }

      List<GraphBuilder.RelationshipEdge> edgeList =
          DijkstraShortestPath.findPathBetween(g, reference, vertex).getEdgeList();
      if ((edgeList.size() == 1 && edgeList.get(0).getTarget().toString().contains("QS"))
          || (edgeList.size() > 1)) {
        totalWeight += DijkstraShortestPath.findPathBetween(g, reference, vertex).getWeight();
        weightMap.putIfAbsent(
            (String) edgeList.get(edgeList.size() - 1).getTarget(),
            edgeList.get(edgeList.size() - 1).getWeight());
      } else if ((edgeList.size() == 1 && edgeList.get(0).getTarget().toString().contains("QZ"))) {
        totalWeight += DijkstraShortestPath.findPathBetween(g, reference, vertex).getWeight();
        weightMap.putIfAbsent(
            (String) edgeList.get(edgeList.size() - 1).getTarget(),
            edgeList.get(edgeList.size() - 1).getWeight());
        if (g.outDegreeOf(edgeList.get(0).getTarget().toString()) == 0) {
          nonExisitingRelation.add(edgeList.get(0).getTarget().toString());
        }
      }
    }

    @Override
    public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
      weightMap.putIfAbsent(reference, totalWeight);
      totalWeight = 0.0;
      super.connectedComponentFinished(e);
    }

    public Map<String, Double> getWeightMap() {
      return weightMap;
    }
  }
}