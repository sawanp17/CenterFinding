import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.util.List;
import java.util.Map;

public class MyNewNode extends Node {
    public States state;

    public MyNewNode parent;


    List<Node> mutableNeighbor;

    public int maxValue = 0;
    public int maxValue2 = 0;

    public MyNewNode maxNeighbor;

    public States getState() {
        return state;
    }

    public void setState(final States state) {
        this.state = state;
    }

    public MyNewNode getParent() {
        return parent;
    }

    public void setParent(final MyNewNode parent) {
        this.parent = parent;
    }

    //form a constructor


    public void initalize(){
        mutableNeighbor = getNeighbors();
        maxValue=0;
        maxValue2=0;
    }

    @Override
    public void onSelection(){
        Message message = new Message("Activate");
        sendAll(message);

        initalize();
//        state = States.Active;


        //if Leaf node
        if (getNeighbors().size() == 1){
            Message messageM = new Message("Center" + (maxValue + 1));
            parent = (MyNewNode) getNeighbors().get(0);
            System.out.println("NODE " + getID() + " SELECTS " + parent.getID() + " AS PARENT.");
            send(parent,messageM);
            state = States.Processing;
            this.setColor(Color.YELLOW);

        }
        else {
            state = States.Active;
            this.setColor(Color.GREEN);

        }
    }



    @Override
    public void onMessage(Message message){
        System.out.println("Node" + this.getID() + "Received Message (onMessage) from " + message.getSender().getID() + " of type " + message.getContent().toString());
        if (message.getContent().toString().equals("Activate")){
            if (state == States.Available){
                Message newMsg = new Message("Activate");
                sendAll(newMsg);

                initalize();

                //if Leaf node
                if (getNeighbors().size() == 1){
                    Message messageM = new Message("Center" + (maxValue + 1));
                    parent = (MyNewNode) getNeighbors().get(0);
                    send(parent,messageM);
                    state = States.Processing;
                    this.setColor(Color.YELLOW);

                }
                else {
                    state = States.Active;
                    this.setColor(Color.GREEN);

                }
            }

        }
        else if (message.getContent().toString().substring(0,"Center".length()).equals("Center")){

            if (state == States.Active){
                processMsg(message);

                if (mutableNeighbor.size() > 1) mutableNeighbor.remove(message.getSender());
                if (mutableNeighbor.size()==1){
                    parent = (MyNewNode) mutableNeighbor.get(0);
                    System.out.println("NODE " + getID() + " SELECTS " + parent.getID() + " AS PARENT.");

                    Message messageRes = new Message("Center" + (maxValue + 1));

                    send(parent,messageRes);
                    state = States.Processing;
                    this.setColor(Color.YELLOW);
                }


            }

            else if (state == States.Processing){
                System.out.println("Adding node " + getID() + " to Saturated, Recvd M from " + message.getSender().getID());
                processMsg(message);
                resolve();
            }
        }
    }

    public Message prepareMsg(){
        Message m = new Message("Saturation");
        return m;
    }
    public void processMsg(Message message){
        int recvdValue = Integer.parseInt(message.getContent().toString().substring("Center".length()));
        if (maxValue < recvdValue){
            maxValue2 = maxValue;
            maxValue = recvdValue;
            maxNeighbor = (MyNewNode) message.getSender();
        }
        else {
            if (maxValue2 < recvdValue){
                maxValue2 = recvdValue;
            }
        }

    }
    public void resolve(){
//        System.out.println(getID() +  " " + maxValue + " " + maxValue2 + " " + maxNeighbor.getID() + " " + parent.getID());

        if (maxValue - maxValue2 == 1){
            if (maxNeighbor != parent){
                Message message = new Message("Center" + (maxValue2+1));
                send(maxNeighbor,message);
            }
            System.out.println("Node " + getID() + " IS CENTER");

            state = States.Center;
            this.setColor(Color.BLACK);
        }
        else {
            if (maxValue - maxValue2 > 1){
                Message message = new Message("Center" + (maxValue2+1));
                send(maxNeighbor,message);
            }
            else {
                System.out.println("Node " + getID() + " IS CENTER");

                state = States.Center;
                this.setColor(Color.BLACK);
            }
        }
//        System.out.println(getID() +  " " + maxValue + " " + maxValue2 + " " + maxNeighbor.getID() + " " + parent.getID());


//        state = States.Saturated;
//        this.setColor(Color.RED);

    }
}
