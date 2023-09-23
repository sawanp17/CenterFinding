import io.jbotsim.core.*;
import io.jbotsim.core.Link;
import io.jbotsim.core.Message;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import java.util.List;
import java.util.Scanner;

public class HelloWorld {
    public Topology tp;
    public static void main(String[] args){
        // Create a topology
        Topology topology = new Topology();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of nodes: ");
        System.out.println("Please note node values are from 0 to n-1. Enter edges accordingly. ");
        int numberOfNodes = scanner.nextInt();
        MyNewNode[] nodes = new MyNewNode[numberOfNodes];
        int n = numberOfNodes; // Number of vertices (adjust as needed)
        double radius = 200.0;
        for (int i=0;i<numberOfNodes;i++){
            MyNewNode n1 = new MyNewNode();
            n1.setState(States.Available);
            n1.setColor(Color.BLUE);
            nodes[i] =  n1;

            double x = radius * Math.cos(2 * Math.PI * i / n);
            double y = radius * Math.sin(2 * Math.PI * i / n);

            topology.addNode(x+500,y+500,n1);
            topology.addNode(n1);
        }

        System.out.println("Enter the number of edges: ");
        int numberOfEdges = scanner.nextInt();
        for (int i=0;i<numberOfEdges;i++){
            System.out.print("Enter the first node of link: ");
            int first = scanner.nextInt();

            System.out.print("Enter the second node of link: ");
            int second = scanner.nextInt();

            Link link = new Link(nodes[first], nodes[second]);
            topology.addLink(link);

        }






        // Create a viewer to visualize the topology
        topology.start();
        new JViewer(topology);

        System.out.println(nodes[1].getNeighbors().get(0).getClass());

        //pass initiator to this function
        saturation(nodes[0]);
//        for (MyNewNode node: nodes){
//            System.out.println(node.maxValue + " " + node.maxValue2);
//        }
    }

    public static void saturation(MyNewNode initiator){
        initiator.onSelection();
    }

}
