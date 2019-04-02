import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.jfree.chart.plot.ThermometerPlot;

import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class ServerRobot {
	int speed = 100;
	int acceration = 100;
	int portNumber = 5000;
	 Wheel leftWheel = WheeledChassis.modelWheel(Motor.C, 56).offset(-53.2);
	 Wheel rigthWheel = WheeledChassis.modelWheel(Motor.D, 56).offset(53.2);
	 MovePilot pilot;
	 boolean newComand = false;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerRobot serverRobot = new ServerRobot();
		serverRobot.setup();

	}
	
	public void setup()
	{
		Chassis chassis = new WheeledChassis(new Wheel[] { leftWheel, rigthWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		 pilot = new MovePilot(chassis);
		 pilot.setLinearAcceleration(acceration);
		 pilot.setLinearSpeed(speed);
		try {
			ServerSocket server = new ServerSocket(portNumber);
		
			
			System.out.println("connecting");
			Socket s = server.accept();
			System.out.println("Connected");
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			
			boolean done = false;
			while(!done)
			{
				
				String message = dis.readUTF();
				newComand = true;
				/*dos.writeUTF(message);
				dos.flush();
				if(message.toLowerCase().equals("quit"))
				{
					System.exit(0);
				}
				while (message.equals("forward"))*/
				
				switch(message.toLowerCase())
				{
				case "forward": forward();
					break;
				case "backward": backwards();
				break;
				case "left": left();
				break;
				case "right": right();
				break;
				case "stop": stop();
				break;
					}
				
				
			    }
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	public void forward () {
		pilot.forward();
		while(!newComand) {
			Thread.yield();
		
		}
			
	}
	public void backwards () {
		pilot.backward();
		while(!newComand) {
			Thread.yield();
		
		}
	}
	public void left () {
		pilot.rotateLeft();
		while(!newComand) {
			Thread.yield();
		
		}
	}
	public void right () {
		//pilot.rotateRight();
		while(!newComand) {
			pilot.rotate(5);
			Thread.yield();
		
		}
	}
	public void stop () {
		System.exit(0);
		
		}
			
	

}
