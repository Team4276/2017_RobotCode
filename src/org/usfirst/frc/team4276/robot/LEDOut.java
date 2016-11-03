package org.usfirst.frc.team4276.robot;
import edu.wpi.first.wpilibj.DigitalOutput;

public class LEDOut {

    DigitalOutput bit1;
    DigitalOutput bit2;
    DigitalOutput bit3;
    DigitalOutput bit4;
    
    public LEDOut(int one, int two, int three, int four)
    {
        bit1 = new DigitalOutput(one);
        bit2 = new DigitalOutput(two);
        bit3 = new DigitalOutput(three);
        bit4 = new DigitalOutput(four);
    }
    
    void output()
    {
    	
    	int mode = 10;
        if((mode&8)==8)
        {
            bit4.set(true);
        }
        else {
            bit4.set(false);
        }
        if((mode&4)==4)
        {
            bit3.set(true);
        }
        else {
            bit3.set(false);
        }
        if((mode&2)==2)
        {
            bit2.set(true);
        }
        else {
            bit2.set(false);
        }
        if((mode&1)==1)
        {
            bit1.set(true);
        }
        else {
            bit1.set(false);
        }
        
    }
}