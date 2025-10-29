package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class PracticeRobotHardware {

    /* Declare OpMode members. */
    private LinearOpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    // Define Motor and Servo objects  (Make them private so they can't be accessed externally)
    private DcMotor backLeft   = null;
    private DcMotor frontLeft  = null;
    private DcMotor backRight   = null;
    private DcMotor frontRight  = null;

    private DcMotor launchLeft = null;
    private DcMotot launchRight = null;

    private Servo frontPassthrough = null;
    private Servo backPassthrough = null;

    private Servo flipper = null;

    // Define Drive constants.  Make them public so they CAN be used by the calling OpMode
    public static final double flipperPos = 0.5 ;
    public static final double backPos = 0.5 ;
    public static final double frontPos = 0.5 ;

    // Define a constructor that allows the OpMode to pass a reference to itself.
    public PracticeRobotHardware (LinearOpMode opmode) {
        myOpMode = opmode;
    }

    /**
     * Initialize all the robot's hardware.
     * This method must be called ONCE when the OpMode is initialized.
     * <p>
     * All of the hardware devices are accessed via the hardware map, and initialized.
     */
    public void init()    {
        // Define and Initialize Motors (note: need to use reference to actual OpMode).
        backLeft  = myOpMode.hardwareMap.get(DcMotor.class, "back_left");
        backRight = myOpMode.hardwareMap.get(DcMotor.class, "back_right");
        frontLeft = myOpMode.hardwareMap.get(DcMotor.class, "front_left");
        frontRight = myOpMode.hardwareMap.get(DcMotor.class, "front_right");

        launchLeft = myOpmode.hardwareMap.get(DcMotor.class, "launch_left");
        launchRight = myOpmode.hardwareMap.get(DcMotor.class, "launch_right");

        frontPassthough = myOpmode.hardwareMap.get(Servo.class, "front_passthrough");
        backPassthough = myOpmode.hardwareMap.get(Servo.class, "back_passthrough");

        flipper = myOpmode.hardwareMap.get(Servo.class, "flipper");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);

        launchLeft.setDirection(DcMotor.Direction.REVERSED);
        launchRight.setDirection(DcMotor.Direction.FORWARD);

        flipper.setPosition(flipperPos);
        frontPassthrough.setPosition(frontPos);
        backPassthrough.setPosition(backPos);

        myOpMode.telemetry.addData(">", "Hardware Initialized");
        myOpMode.telemetry.update();
    }

    /**
     * Calculates the left/right motor powers required to achieve the requested
     * robot motions: Drive (Axial motion) and Turn (Yaw motion).
     * Then sends these power levels to the motors.
     *
     * @param Drive     Fwd/Rev driving power (-1.0 to 1.0) +ve is forward
     * @param Turn      Right/Left turning power (-1.0 to 1.0) +ve is CW
     */
    public void driveRobot(double Forward, double Rotation, double Strafe) {
        // Combine drive and turn for blended motion.
        double FL_Drive  =   Forward - Rotation - Strafe;
        double FR_Drive  =   Forward + Rotation + Strafe;
        double BL_Drive  =   Forward - Rotation + Strafe;
        double BR_Drive  =   Forward + Rotation - Strafe;

        // Scale the values so neither exceed +/- 1.0
        double max = Math.max(Math.abs(FL_Drive), Math.abs(FR_Drive));
               max = Math.max(max, Math.abs(BL_Drive));
               max = Math.max(max, Math.abs(BR_Drive));
               
        double min = Math.min(Math.abs(FL_Drive), Math.abs(FR_Drive));
               min = Math.min(min, Math.abs(BL_Drive));
               min = Math.min(min, Math.abs(BR_Drive));
               
               
        if (max > 1.0)
        {
            FL_Drive /= max;
            FR_Drive /= max;
            BL_Drive /= max;
            BR_Drive /= max;
        }
        
        if (min < 1.0)
        {
            FL_Drive /= min;
            FR_Drive /= min;
            BL_Drive /= min;
            BR_Drive /= min;
        }

        frontLeft.setPower(FL_Drive);
        frontRight.setPower(FR_Drive);
        backLeft.setPower(BL_Drive);
        backRight.setPower(BR_Drive);
        
    }   



    public void flipperToggle() {
        switch(Power) {
            case "On":
                flipper.setPosition(0.5);
                break;
            case "Off":
                flipper.setPosition(0.0);
                break;
        }
    }

    public void launchFeeder(){
        frontPassthrough.setPosition(0.25); //backward movement
        backPassthrough.setPosition(0);
        wait(500); //buffer
        frontPassthrough.setPosition(1); //push
        wait(500); //buffer
        frontPassthrough.setPosition(frontPos); //back to start
        backPassthrough.setPosition(backPos);
    }

    public void expellFeeder(){
        frontPassthrough.setPosition(0.25); //backward movement
        backPassthrough.setPosition(0);
        wait(500); //buffer
        frontPassthrough.setPosition(1); //push
        wait(500); //buffer
        frontPassthrough.setPosition(frontPos); //back to start
        backPassthrough.setPosition(backPos);
    }


                
    // public void spinLaunch() {
    //     switch(Power) {
    //         case "On":
    //             launchLeft.setPower(1.0);
    //             launchRight.setPower(1.0);
    //             break;
    //         case "Off":
    //             launchLeft.setPower(0);
    //             launchRight.setPower(0);
    //             break;  
    //     }
    // }
}
