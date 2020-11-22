package mergeSort;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MergeSortMoreNodes {
  /**
   * Thread that declares the lambda and then initiates the work
   */

  public static int message_id = 0;

  public static JSONObject init(int[] array) {
    JSONArray arr = new JSONArray();
    for (var i : array) {
      arr.put(i);
    }
    JSONObject req = new JSONObject();
    req.put("method", "init");
    req.put("data", arr);
    return req;
  }

  public static JSONObject peek() {
    JSONObject req = new JSONObject();
    req.put("method", "peek");
    return req;
  }

  public static JSONObject remove() {
    JSONObject req = new JSONObject();
    req.put("method", "remove");
    return req;
  }
  
  public static void Test(int port) {
    int[] a = { 5, 1, 6, 2, 3, 4, 10,634,34,23,653, 23,2 ,6 };
    JSONObject response = NetworkUtils.send(port, init(a));
    
    System.out.println(response);
    response = NetworkUtils.send(port, peek());
    System.out.println(response);

    while (true) {
      response = NetworkUtils.send(port, remove());

      if (response.getBoolean("hasValue")) {
        System.out.println(response);;
 
      } else{
        break;
      }
    }
  }

  //test shorter array
  public static void Test2(int port) {
    int[] a = { 5, 1, 6,634,34,23,653};
    JSONObject response = NetworkUtils.send(port, init(a));
    
    System.out.println(response);
    response = NetworkUtils.send(port, peek());
    System.out.println(response);

    while (true) {
      response = NetworkUtils.send(port, remove());

      if (response.getBoolean("hasValue")) {
        System.out.println(response);;
 
      } else{
        break;
      }
    }
  }

  //test longer array
  public static void Test3(int port) {
    int[] a = { 5, 1, 6, 2, 3, 4, 10, 634, 34, 23,653, 23, 2 ,6, 18, 235, 634, 56, 80, 45, 5};
    JSONObject response = NetworkUtils.send(port, init(a));
    
    System.out.println(response);
    response = NetworkUtils.send(port, peek());
    System.out.println(response);

    while (true) {
      response = NetworkUtils.send(port, remove());

      if (response.getBoolean("hasValue")) {
        System.out.println(response);;
 
      } else{
        break;
      }
    }
  }

  public static void main(String[] args) {
    // all the listening ports in the setup
    ArrayList<Integer> ports = new ArrayList<>(Arrays.asList(8000, 8001, 8002, 8003, 8004, 8005, 8006, 8007, 8008));

    // setup each of the nodes
    //       0
    //   1       2
    // 3 4 7   5 6 8

    
    new Thread(new Branch(ports.get(0), ports.get(1), ports.get(2))).start();
    


    // long startTime2 = System.nanoTime();
    new Thread(new Branch(ports.get(1), ports.get(3), ports.get(4))).start();
    new Thread(new Sorter(ports.get(3))).start();
    new Thread(new Sorter(ports.get(4))).start();
    new Thread(new Sorter(ports.get(7))).start();
    // long endTime1 = System.nanoTime();

    // long startTime3 = System.nanoTime();
    new Thread(new Branch(ports.get(2), ports.get(5), ports.get(6))).start();
    new Thread(new Sorter(ports.get(5))).start();
    new Thread(new Sorter(ports.get(6))).start();
    new Thread(new Sorter(ports.get(8))).start();
    // long endTime1 = System.nanoTime();


    // make sure we didn't hang
    System.out.println("started");
    // One Sorter
    long startTime1 = System.nanoTime();
    Test(ports.get(3));
    long endTime1 = System.nanoTime();

    long duration1 = (endTime1 - startTime1) / 1000000; //divide by 1000000 to get milliseconds
    System.out.println("One Sorter time: " + startTime1);
    System.out.println("One Sorter time: " + endTime1);
    System.out.println("One Sorter time: " + duration1 + "ms");
    System.out.println("\n\n");

    // One Branch / Three Sorters
    startTime1 = System.nanoTime();
    Test(ports.get(2));
    endTime1 = System.nanoTime();

    duration1 = (endTime1 - startTime1) / 1000000; //divide by 1000000 to get milliseconds
    System.out.println("One Branch/Three Sorters time: " + startTime1);
    System.out.println("One Branch/Three Sorters time: " + endTime1);
    System.out.println("One Branch/Three Sorters time: " + duration1 + "ms");
    System.out.println("\n\n");

    // Three Branch / Six Sorters
     startTime1 = System.nanoTime();
    Test(ports.get(0));
     endTime1 = System.nanoTime();

     duration1 = (endTime1 - startTime1) / 1000000; //divide by 1000000 to get milliseconds
    System.out.println("Three Branch/Six Sorters time: " + startTime1);
    System.out.println("Three Branch/Six Sorters time:  " + endTime1);
    System.out.println("Three Branch/Six Sorters time:  " + duration1 + "ms");
    System.out.println("\n\n");

   // One Sorter
    startTime1 = System.nanoTime();
    Test2(ports.get(3));
    endTime1 = System.nanoTime();

    duration1 = (endTime1 - startTime1) / 1000000; //divide by 1000000 to get milliseconds
    System.out.println("One Sorter time: " + startTime1);
    System.out.println("One Sorter time: " + endTime1);
    System.out.println("One Sorter time: " + duration1 + "ms");
    System.out.println("\n\n");

    // One branch / Two Sorters
    startTime1 = System.nanoTime();
    Test2(ports.get(2));
    endTime1 = System.nanoTime();

    duration1 = (endTime1 - startTime1) / 1000000; //divide by 1000000 to get milliseconds
    System.out.println("One Branch/Three Sorters time: " + startTime1);
    System.out.println("One Branch/Three Sorters time: " + endTime1);
    System.out.println("One Branch/Three Sorters time: " + duration1 + "ms");
    System.out.println("\n\n");

    // Three Branch / Four Sorters
     startTime1 = System.nanoTime();
    Test2(ports.get(0));
     endTime1 = System.nanoTime();

     duration1 = (endTime1 - startTime1) / 1000000; //divide by 1000000 to get milliseconds
    System.out.println("Three Branch/Six Sorters time: " + startTime1);
    System.out.println("Three Branch/Six Sorters time:  " + endTime1);
    System.out.println("Three Branch/Six Sorters time:  " + duration1 + "ms");
    System.out.println("\n\n");

  // One Sorter
    startTime1 = System.nanoTime();
    Test3(ports.get(3));
    endTime1 = System.nanoTime();

    duration1 = (endTime1 - startTime1) / 1000000; //divide by 1000000 to get milliseconds
    System.out.println("One Sorter time: " + startTime1);
    System.out.println("One Sorter time: " + endTime1);
    System.out.println("One Sorter time: " + duration1 + "ms");
    System.out.println("\n\n");

    // One branch / Two Sorters
    startTime1 = System.nanoTime();
    Test3(ports.get(2));
    endTime1 = System.nanoTime();

    duration1 = (endTime1 - startTime1) / 1000000; //divide by 1000000 to get milliseconds
    System.out.println("One Branch/Three Sorters time: " + startTime1);
    System.out.println("One Branch/Three Sorters time: " + endTime1);
    System.out.println("One Branch/Three Sorters time: " + duration1 + "ms");
    System.out.println("\n\n");

    // Three Branch / Four Sorters
     startTime1 = System.nanoTime();
    Test3(ports.get(0));
     endTime1 = System.nanoTime();

     duration1 = (endTime1 - startTime1) / 1000000; //divide by 1000000 to get milliseconds
    System.out.println("Three Branch/Six Sorters time: " + startTime1);
    System.out.println("Three Branch/Six Sorters time:  " + endTime1);
    System.out.println("Three Branch/Six Sorters time:  " + duration1 + "ms");
    System.out.println("\n\n");


  }
}
