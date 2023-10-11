package tech.jhipster.lite.module.domain.javaproperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import tech.jhipster.lite.shared.collection.domain.JHipsterCollections;
import tech.jhipster.lite.shared.error.domain.Assert;

public final class PropertyValue<T> {

  private final Collection<T> values;

  private PropertyValue(T[] values) {
    this(List.of(values));
  }

  private PropertyValue(Collection<T> values) {
    Assert.field("values", values).noNullElement();

    this.values = JHipsterCollections.immutable(values);
  }

  public static PropertyValue<String> of(String[] values) {
    return new PropertyValue<>(values);
  }

  public static PropertyValue<Boolean> of(Boolean[] values) {
    return new PropertyValue<>(values);
  }

  public static PropertyValue<Number> of(Number[] values) {
    return new PropertyValue<>(values);
  }

  public Collection<T> get() {
    return values();
  }

  public static <T> PropertyValue<T> merge(PropertyValue<T> v1, PropertyValue<T> v2) {
    Collection<T> mergedValues = new ArrayList<>();
    mergedValues.addAll(v1.get());
    mergedValues.addAll(v2.get());

    return new PropertyValue<>(mergedValues);
  }

  public Collection<T> values() {
    return values;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (PropertyValue<?>) obj;
    return Objects.equals(this.values, that.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(values);
  }

  @Override
  public String toString() {
    return "PropertyValue[" + "values=" + values + ']';
  }
}
