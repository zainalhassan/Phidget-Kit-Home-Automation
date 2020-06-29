package mqtt.Publisher;

import com.phidget22.*;

public class MotorMover
{
	// Singleton implementation to allow multiple callbacks to the code
	static RCServo servo = null;
	private static MotorMover instance = null;

    public static RCServo getInstance() 
    {
	   System.out.println("In singleton constructor");
       if(servo == null) 
       {
          servo = MotorMover();
       }
       return servo;
    }
	
	private static  RCServo MotorMover() 
	{
		try 
		{
			System.out.println("Constructing MotorMover");
			servo = new RCServo();
			servo.open(2000);
		} catch (PhidgetException e) {
			e.printStackTrace();
		}
	        moveServoTo(0);
	        System.out.println("Motor initially positioned at: 0");
        return servo;
	}               

	public static void moveServoTo(double motorPosition) 
	{
        try 
        {
        	MotorMover.getInstance();	//get access to motor
        	System.out.println("moving to " + motorPosition + "\n");
        	servo.setMaxPosition(210.0);
			servo.setTargetPosition(motorPosition);
			servo.setEngaged(true);
		} 
        catch (PhidgetException pe) 
        {
			pe.printStackTrace();
		}
	}
}