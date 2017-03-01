package parser;

public class Pair<T, V> {
    private T object1;
    private V object2;

    public Pair() {
        this.object1 = null;
        this.object2 = null;
    }

    public Pair(T object1, V object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

    public void setObject1(T object1) {
        this.object1 = object1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (object1 != null ? !object1.equals(pair.object1) : pair.object1 != null) return false;
        return object2 != null ? object2.equals(pair.object2) : pair.object2 == null;
    }

    @Override
    public int hashCode() {
        int result = object1 != null ? object1.hashCode() : 0;
        result = 31 * result + (object2 != null ? object2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String text = object1.toString() + ' ' + object2.toString();
        return text;
    }

    public void setObject2(V object2) {
        this.object2 = object2;
    }

    public T getObject1() {

        return object1;
    }

    public V getObject2() {
        return object2;
    }
}
