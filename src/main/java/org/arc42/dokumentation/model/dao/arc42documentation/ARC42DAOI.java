package org.arc42.dokumentation.model.dao.arc42documentation;

import java.util.List;

public interface ARC42DAOI<T, I> {
  T save(T t);

  Boolean delete(T t);

  void update(T t);

  List<T> findAll(String url);

  T findById(I id);

  default T createRelationship(T dto) {
    return dto;
  }
}
