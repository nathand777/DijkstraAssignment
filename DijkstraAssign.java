//Author: Nathan Dimla 250838691
import java.util.*;
import java.io.*;

class Heap{

  private int [] A; //orignal array
  private int [] H; //heap of indicies
  private boolean [] visited; //array to keep track of which vertex is in the heap
  private int size; //initial number of elements
  private int currentSize; //current number of elements

  public Heap(int [] keys, int n){

    //initialize A and H
    this.A = new int [n + 1];
    this.H = new int [2*n];
    this.size = n;
    visited = new boolean[n+1];
	currentSize = n;

    A[0] = Integer.MAX_VALUE; //set A[0] to inf (or really large number)

    //copy keys into A
    for(int i = 0; i < size; i++){
      A[i+1] = keys[i];
      visited[i] = false;
    }

    heapification(); //heapify
  }

  //HEAPIFCATION algorithm as described in class notes
  private void heapification(){
    for(int i = size; i < 2*size; i++)
  		H[i] = i - size + 1;

    for(int i = size - 1; i > 0; i--){
      if (A[H[2*i]] < A[H[2*i + 1]])
        H[i] = H[2*i];
      else
        H[i] = H[2*i + 1];
    }
  }

  //function to check if heap is isEmpty
  public boolean isEmpty(){
    return (currentSize <= 0);
  }

  //check if id is in heap
  public boolean in_heap(int id){
    return !visited[id];
  }

  //return minimum value
  public int min_key(){
    return A[H[1]];
  }

  //return index of minimum value
  public int min_id(){
    return H[1];
  }

  //return key
  public int key(int id){
    return A[id];
  }

  //function to decrease priority of key
  public void decrease_key(int id, int v){
    A[id] = v;
    int i = (id + size - 1) / 2;

    while (i > 0){
      if (A[H[2*i]] < A[H[2*i + 1]])
        H[i] = H[2*i];
      else
        H[i] = H[2*i + 1];
      i /= 2;
    }
  }

  //removes the smallest value from the heap
  public int delete_min() {
    A[0] = Integer.MAX_VALUE;

    H[H[1] + size - 1] = 0;
    int v = H[1];
    visited[H[1]] = true;
    currentSize--;

    int i = (H[1] + size - 1) / 2;
    while (i > 0) {
        if (A[H[2 * i]] < A[H[2 * i + 1]])
          H[i] = H[2 * i];
        else
          H[i] = H[2 * i + 1];

        i /= 2;
    }
    return v;
  }
}

public class asn3{

  private static int numVert; //number of vertices in graph;
  private static int [] [] graph; //adjacency matrix representation of graph
  private static int [] d; //upper bound on weieght to v
  private static int [] p;// shortest path to v

  public static void readGraph (){
    String [] s;
	  String str;
    Scanner sc = new Scanner(System.in);

    //scan first line
    str = sc.nextLine();
    numVert = Integer.parseInt(str);

    graph = new int [numVert][numVert]; //initialize graph
    d = new int[numVert];
    p = new int [numVert];

    //read in rest of input and save to matrix
    while(sc.hasNextLine()){
      str = sc.nextLine();
	    s = str.split("\\s+");
      graph[Integer.parseInt(s[0])-1][Integer.parseInt(s[1])-1] = Integer.parseInt(s[2]);
    }
  }

  //function that prints out graph in list form
  public static void printGraph(){

    System.out.println("Graph: ");

    for (int i = 0; i < numVert; i++){
      System.out.print((i+1) + " -> ");
      for (int j = 0; j < numVert; j++){
        if(graph[i][j] > 0)
          System.out.print((j+1) + ",");
      }
      System.out.println();
    }
    System.out.println("\n---------------\n");
  }

  //INITIALIZE_SINGLE_SOURCE implemented from notes
  private static void initialize_single_source(int s){
    for(int v = 0; v < numVert; v++)
      d[v] = Integer.MAX_VALUE;
    d[s] = 0;
  }

  //RELAX function implemented from notes
  private static void relax(int u, int v, int w){
    if(d[v] > d[u] + w){
      d[v] = d[u] + w;
      p[v] = u;
    }
  }

  private static void dijkstra(int s){

    initialize_single_source(s); //initialize all values and set d[s] to 0

    int u; //used for getting current vertex
    ArrayList<Integer> S = new ArrayList<Integer>(); //stores vertices that algorithm visits in order
    Heap Q = new Heap(d, numVert); //make new heap with d[v] as keys

    System.out.println("Algorithm Output: ");
    while (!Q.isEmpty()){
      u = Q.delete_min() - 1; //extract min from heap

      System.out.print("d[" + (u+1) + "]: " + d[u]);
      System.out.println(" p[" + (u+1) + "]: " + p[u]);

      S.add(u+1); //add u to path

      //check every adjacent vertex to u
      for(int v = 0; v < numVert; v++){
        //if there is an edge, relax it
        if(graph[u][v] > 0){
          relax(u, v, graph[u][v]);
          Q.decrease_key(v + 1, d[v]); //decrease priority
        }
      }
    }
    System.out.println("\n---------------\n");

    //print path
    System.out.println("Path: " + S);
  }

  public static void main(String [] args){
    readGraph(); //read graph from file
    printGraph(); //output graph
    dijkstra(0); //begin dijkstra at vertex 1
  }
}
