package com.example.geektrust.model;

import com.example.geektrust.repository.UserRepository;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.lang.Math;

public class DebtGraph {

    private static DebtGraph instance;

    private DebtGraph(){}

    public static DebtGraph getInstance(){
        if(instance == null){
            instance = new DebtGraph();
        }
        return instance;
    }

    Map<String, Map<String, Integer>> graph = new HashMap();

    public void addNode(String node){
        Map<String, Integer> edges = new HashMap<String, Integer>();
        graph.putIfAbsent(node, edges);
    }

    public void removeNode(String node){
        graph.remove(node);
        for (Map.Entry<String, Map<String, Integer>> edge : graph.entrySet()) {
            if(edge.getValue().containsKey(node)){
                edge.getValue().remove(node);
            }
        }
    }

    public void addEdge(String source, String destination, int weight){
        Map<String, Integer> edges = graph.get(source);
        if(edges.containsKey(destination)){
            int edgeWeight = edges.get(destination);
            edges.put(destination, edgeWeight + weight);
        }
        else{
            edges.put(destination, weight);
        }
    }

    public void addDebt(String source, String destination, int weight){
        Map<String, Integer> edges = graph.get(destination);
        if(edges.isEmpty()){
            addEdge(source, destination, weight);
        }
        else{
            if(edges.containsKey(source)){
                int edgeWeight = edges.get(source);
                int diff = edgeWeight - weight;
                if(diff >= 0) {
                    edges.put(source, diff);
                }else if(diff < 0){
                    addEdge(source, destination, Math.abs(diff));
                }else{
                    edges.remove(destination);
                }
            }
            else{
                addEdge(source, destination, weight);
            }
        }

    }

    public void simplifyGraph() {
        ArrayList<String> vertexes = new ArrayList<String>(graph.keySet());
        int i = 0;
        while (i < vertexes.size()) {
            String currNode = vertexes.get(i);
            Map<String, Integer> edges = graph.get(currNode);
            if (edges.isEmpty()) {
                i++;
                continue;
            } else {
                for (Map.Entry<String, Integer> edge : edges.entrySet()) {
                    if(graph.get(edge.getKey()).isEmpty()){
                        continue;
                    }
                    else{
                        for (Map.Entry<String, Integer> edge1 : graph.get(edge.getKey()).entrySet()) {
                            if (edges.containsKey(edge1.getKey())) {
                                int sum = edges.get(edge1.getKey()) + edge.getValue();
                                int diff = edge1.getValue() - edge.getValue();
                                edges.put(edge1.getKey(), sum);
                                graph.get(edge.getKey()).put(edge1.getKey(), diff);
                                edges.put(edge.getKey(), 0);
                            }
                        }
                    }
                }
                i++;
            }
        }
    }

    public Map<String, Map<String, Integer>> getGraph() {
        return graph;
    }
}
