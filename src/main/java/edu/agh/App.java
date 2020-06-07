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
            executor.execute(receiver.receiveCommand());
        }
    }
}
