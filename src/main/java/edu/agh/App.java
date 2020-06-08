package edu.agh;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CmdExecutor executor=new CmdExecutor();
        CmdReceiver receiver=new CmdReceiver(executor.getPossibleCommands());
        executor.execute("clear");
        while(true)
        {
            try
            {
                executor.execute(receiver.receiveCommand());
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
