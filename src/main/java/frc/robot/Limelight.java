package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;

public class Limelight {
	public static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    public static DriverStation ds = DriverStation.getInstance();
    
	public static void testFeed(){
		double x = table.getEntry("tx").getDouble(0.0);
		double y = table.getEntry("ty").getDouble(0.0);		
	}
	
	public static double getX(){
		return table.getEntry("tx").getDouble(0.0);
	}
	
	public static double getY(){
		return table.getEntry("ty").getDouble(0.0);
	}
	
	public static double getA(){
		return table.getEntry("ta").getDouble(0.0);
	}
	
	public static boolean hasValidTargets(){
		if(table.getEntry("tv").getDouble(0.0) == 1){
			return true;
		}
		return false;
	}
	
	public static void changePipeline(int pipeline_num){
		NetworkTableEntry pipeline = table.getEntry("pipeline");
		pipeline.setValue(pipeline_num);
	}
	
	public static double getContourArea(){
		return table.getEntry("ta0").getDouble(0.0);
	}
	
	public static double getContourX(){
		return table.getEntry("ty1").getDouble(0.0);
	}

	public static void lineUp(){
		Limelight.testFeed();
		double target = DriveTrain.getAHRS() + Limelight.getX();
		DriveTrain.turnToAngle(target);
		System.out.println("TARGET: " + target);
		System.out.println("AHRS: " + DriveTrain.getAHRS());
		if(!ds.isEnabled()){
			DriveTrain.pidDisable();
		}
	}

	public static void dock(){
		double distance = Utils.distFrom(Utils.degToRad(Limelight.getX()),Utils.degToRad(Limelight.getY()));
		
		Limelight.lineUp();
		if(distance >= 50){
			Limelight.changePipeline(3);
		}

		if(distance <=20){
			Limelight.changePipeline(4);
			DriveTrain.drive(-Constants.LINEUP_FULL_SPEED, -Constants.LINEUP_HALF_SPEED * Constants.DRIVE_STRAIGHT_CONSTANT);
		}else{
			DriveTrain.drive(-Constants.LINEUP_FULL_SPEED, -Constants.LINEUP_FULL_SPEED * Constants.DRIVE_STRAIGHT_CONSTANT);
		}
	}

}
