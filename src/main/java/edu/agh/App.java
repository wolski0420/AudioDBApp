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
        executor.execute("clear");
        executor.execute("add_song title pop artist artist2");
        executor.execute("add_listener jan");
        executor.execute("add_song title2 rock artist2");
        executor.execute("get_recommendation_by_artist");
    }
}
