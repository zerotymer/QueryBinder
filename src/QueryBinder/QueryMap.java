package QueryBinder;

import QueryBinder.Annotation.QueryRequest;
import QueryBinder.Annotation.QueryRequestParam;

public class BindingMap extends java.util.HashMap<String, Object> {
    /// FIELDs
    /// CONSTRUCTORs
    public BindingMap() {
        super();
    }
    public BindingMap(int initialCapacity) {
        super(initialCapacity);
    }
    public BindingMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }
    public BindingMap() {

    }

    /// METHODs
    @Override
    public String toString() {
        return "BindingMap{}";
    }
}
