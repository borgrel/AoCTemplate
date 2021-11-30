package aoc.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WGragh<V, U extends Comparable<? super U>> {
    static class Edge<V, U> {
        U weight;
        Node<V, U> from;
        Node<V, U> to;
    }
    static class Node<V, U> {
        private V value;
        Map<V, U> links;

        Node(V value) {
            this.value = value;
            links = new TreeMap<>();
        }
    }
    Map<V, Node<V, U>> nodes;

    WGragh() {
        nodes = new HashMap<>();
    }
    public void add(V value) {
        Node<V, U> check = nodes.get(value);
        if (check != null) return;
        nodes.put(value, new Node<>(value));
    }
    public void link(V from, U weight, V to) {

    }
}
