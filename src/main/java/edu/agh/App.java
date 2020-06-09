package edu.agh;

import edu.agh.cmdUtils.CmdExecutor;
import edu.agh.cmdUtils.CmdReceiver;

public class App
{
    public static void main( String[] args )
    {
        CmdExecutor executor=new CmdExecutor();
        CmdReceiver receiver=new CmdReceiver(executor.getPossibleCommands());
        executor.execute("clear");
        executor.getDataFromJson("resources/db_small/");
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
