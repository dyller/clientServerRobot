import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;

public class ServerNavi {
	int speed = 100;
	int acceration = 100;
	int portNumber = 5000;
	 Wheel leftWheel = WheeledChassis.modelWheel(Motor.C, 56).offset(-53.2);
	 Wheel rigthWheel = WheeledChassis.modelWheel(Motor.D, 56).offset(53.2);
	 MovePilot pilot;
	 boolean newComand = false;
	 Navigator navi;
	 DataInputStream dis;
	 DataOutputStream dos;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerNavi serverRobot = new ServerNavi();
		serverRobot.setup();

	}
	
	public void setup()
	{
		Chassis chassis = new WheeledChassis(new Wheel[] { leftWheel, rigthWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		 pilot = new MovePilot(chassis);
		 pilot.setLinearSpeed(speed);
		 pilot.setAngularSpeed(speed);
		 navi = new Navigator(pilot);
		
		try {
			ServerSocket server = new ServerSocket(portNumber);
		
			
			LCD.drawString("connecting", 0, 0);
			Sound.beep();
			Socket s = server.accept();
			LCD.drawString("Connected", 0, 1);
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			
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
				case "forward": start();
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
	

	private void start() {
		navi.addWaypoint(500, 0);
		navi.addWaypoint(500, 500);
		navi.addWaypoint(0, 500);
		navi.addWaypoint(0, 0);
		navi.followPath();
		while(!navi.pathCompleted())
		{
			Thread.yield();
			try {
				float x = navi.getPoseProvider().getPose().getX();
				float y = navi.getPoseProvider().getPose().getY();
				LCD.drawString(x+"", 0, 3);
				LCD.drawString(y+"", 0, 4);
				dos.writeUTF(x+" "+y);
			    dos.flush();
			} catch ( NullPointerException |IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
		// TODO Auto-generated method stub
		
	}

	public void stop () {
		navi.stop();
		System.exit(0);
		
		}
			
	
}
