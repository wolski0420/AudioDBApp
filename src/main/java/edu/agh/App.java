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
        executor.execute("get_recommendation_by_category");
    }
}
