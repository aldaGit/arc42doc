package org.arc42.dokumentation.view.util.methods;

import java.util.ArrayList;

public class Util {
  public ArrayList<String> convertInput(ArrayList<String> list) {
    for (int i = 0; i < list.size(); i++) {
      String j = list.get(i).replace("\"", "");
      list.set(i, j);
    }
    return list;
  }
}
