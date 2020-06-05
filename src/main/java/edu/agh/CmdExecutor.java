package edu.agh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdExecutor {
    private static final String ADD_SONG_CMD="add_song";
    private static final String ADD_ARTIST_CMD="add_artist";
    private static final String ADD_LISTENER_CMD="add_listener";
    private static final String GET_RECOMMENDATION_CMD="get_recommendation";

    void execute(String cmd)
    {
        String[] words=cmd.split("\\s+");
        if(words[0].equalsIgnoreCase(ADD_SONG_CMD))
        {

        }
        else if(words[0].equalsIgnoreCase(ADD_ARTIST_CMD))
        {

        }
        else if(words[0].equalsIgnoreCase(ADD_LISTENER_CMD))
        {

        }
        else if(words[0].equalsIgnoreCase(GET_RECOMMENDATION_CMD))
        {

        }
        else throw new IllegalArgumentException("No such command");

    }

    public List<String> getPossibleCommands()
    {
        return new ArrayList<String>(Arrays.asList(ADD_ARTIST_CMD, ADD_SONG_CMD, ADD_LISTENER_CMD, GET_RECOMMENDATION_CMD));
    }

    public void getDataFromJson(final String path)
    {

    }
}
