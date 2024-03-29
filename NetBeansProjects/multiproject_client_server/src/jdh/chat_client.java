package jdh;

import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;

class chatClient extends Frame implements Runnable
{
    Socket soc;    
    TextField tf;
    TextArea ta;
    Button btnSend,btnClose;
    String sendTo;
    String LoginName;
    Thread t=null;
    DataOutputStream dout;
    DataInputStream din;
   public chatClient(String LoginName,String chatwith) throws Exception
    {
        super(LoginName);
        this.LoginName=LoginName;
        sendTo=chatwith;
        tf=new TextField(50);
        ta=new TextArea(50,50);
        btnSend=new Button("Send");
        btnClose=new Button("Close");
        soc=new Socket("127.0.0.1",12000);

        din=new DataInputStream(soc.getInputStream()); 
        dout=new DataOutputStream(soc.getOutputStream());        
        dout.writeUTF(LoginName);

        t=new Thread(this);
        t.start();

    }
    void setup()
    {
        setSize(600,400);
        setLayout(new GridLayout(2,1));

        add(ta);
        Panel p=new Panel();
        
        p.add(tf);
        p.add(btnSend);
        p.add(btnClose);
        add(p);
       // show();        
    }
    public boolean action(Event e,Object o)
    {
        if(e.arg.equals("Send"))
        {
            try
            {
                dout.writeUTF(sendTo + " "  + "DATA" + " " + tf.getText().toString());            
                ta.append("\n" + LoginName + " Says:" + tf.getText().toString());    
                tf.setText("");
            }
            catch(Exception ex)
            {
            }    
        }
        else if(e.arg.equals("Close"))
        {
            try
            {
                dout.writeUTF(LoginName + " LOGOUT");
                System.exit(1);
            }
            catch(Exception ex)
            {
            }
            
        }
        
        return super.action(e,o);
    }
    public static void main(String args[]) throws Exception
    {
        chatClient Client1=new chatClient(args[0],args[1]);
        Client1.setup();                
    }    
    public void run()
    {        
        while(true)
        {
            try
            {
                ta.append( "\n" + sendTo + " Says :" + din.readUTF());
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}