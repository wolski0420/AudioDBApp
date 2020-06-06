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
        executor.execute("add_song title pop artist artist2");
    }
}
