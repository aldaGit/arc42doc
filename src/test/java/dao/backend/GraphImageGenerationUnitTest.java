package dao.backend;

import java.io.File;
import java.io.IOException;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.BeforeEach;

public class GraphImageGenerationUnitTest {
  static DefaultDirectedGraph<String, DefaultEdge> g;

  @BeforeEach
  public void createGraph() throws IOException {
    File imgFile = new File("/Users/klarag/Documents/lokal/test1.png");
    imgFile.createNewFile();
    g = new DefaultDirectedGraph<>(DefaultEdge.class);
    String x1 = "x1";
    String x2 = "x2";
    String x3 = "x3";
    g.addVertex(x1);
    g.addVertex(x2);
    g.addVertex(x3);
    g.addEdge(x1, x2);
    g.addEdge(x2, x3);
    g.addEdge(x3, x1);
  }
/*
 * 
 @Test
 public void givenAdaptedGraph_whenWriteBufferedImage_ThenFileShouldExist() throws IOException {
   
   JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(g);
   mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
   layout.execute(graphAdapter.getDefaultParent());
   File imgFile = new File("doc/tfelle2s/Dokumentation/Kontextansicht.png");
   BufferedImage image =
   mxCellRenderer.createBufferedImage(graphAdapter, null, 3, Color.WHITE, true, null);
   ImageIO.write(image, "PNG", imgFile);
   assertTrue(imgFile.exists());
  }
  */
}
