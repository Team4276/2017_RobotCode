/**
 *
 * @author viking
 */
public class JVisionSystemReceiverRunnable implements Runnable 
{
    boolean m_continueRunning;

    JReceiver m_visionSystemReceiver;
    JTargetInfo m_visionSystemTargetInfo;

    @Override
    public void run() {
        m_visionSystemReceiver = new JReceiver();
        m_visionSystemTargetInfo = new JTargetInfo();
        m_visionSystemReceiver.init();

        String textInput;
        m_continueRunning = true;
        while(m_continueRunning) 
        {
            textInput = m_visionSystemReceiver.getOneLineFromSocket();
            if(textInput != null)
            {
                Robot.g_nSequenceVisionSystem++;
                m_visionSystemTargetInfo.initTargetInfoFromText(textInput);
                //System.out.println(textInput);
                Robot.g_isVisionSystemGoalDetected = m_visionSystemTargetInfo.m_isUpperGoalFound;
                Robot.g_visionSystemAngleRobotToGoal = m_visionSystemTargetInfo.m_angleFromStraightAheadToUpperGoal;
                
                SmartDashboard.putBoolean("isVisionSystemGoalDetected",Robot.g_isVisionSystemGoalDetected); 
                SmartDashboard.putNumber("visionSystemAngleRobotToGoal",Robot.g_visionSystemAngleRobotToGoal);                
            }
            else
            {
                Robot.g_isVisionSystemGoalDetected = false;
            }
        }
    }
}