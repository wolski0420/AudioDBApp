package edu.agh;

public final class Commands {
    public static final String CLEAR="clear";
    public static final String ADD_SONG_CMD="add_song";
    public static final String ADD_SONG_CMD_WITH_OPTIONS =ADD_SONG_CMD+" [title] [category] [artists_names...]";

    public static final String ADD_ARTIST_CMD="add_artist";
    public static final String ADD_ARTIST_CMD_WITH_OPTIONS=ADD_ARTIST_CMD+" [name]";

    public static final String ADD_LISTENER_CMD="add_listener";
    public static final String ADD_LISTENER_CMD_WITH_OPTIONS=ADD_LISTENER_CMD+" [name]";

    public static final String GET_RECOMMENDATION_BY_CATEGORY_CMD="get_recommendation_by_category";
    public static final String GET_RECOMMENDATION_BY_CATEGORY_CMD_WITH_OPTIONS=GET_RECOMMENDATION_BY_CATEGORY_CMD+ " [listener_name]";

    public static final String GET_RECOMMENDATION_BY_ARTIST_CMD="get_recommendation_by_artist";
    public static final String GET_RECOMMENDATION_BY_ARTIST_CMD_WITH_OPTIONS=GET_RECOMMENDATION_BY_ARTIST_CMD+ " [listener_name]";

    public static final String GET_RECOMMENDATION_BY_SIMILAR_LISTENERS_CMD ="get_recommendation_by_similar_listeners";
    public static final String GET_RECOMMENDATION_BY_SIMILAR_LISTENERS_CMD_WITH_OPTIONS=GET_RECOMMENDATION_BY_SIMILAR_LISTENERS_CMD+ " [listener_name]";

    public static final String LISTENER_LIKE_SONG_CMD ="listener_like_song";
    public static final String LISTENER_LIKE_SONG_CMD_WITH_OPTIONS=LISTENER_LIKE_SONG_CMD+" [listener_name] [song_name]";

    public static final String LISTENER_VIEWED_SONG_CMD ="listener_viewed_song";
    public static final String LISTENER_VIEWED_SONG_CMD_WITH_OPTIONS=LISTENER_VIEWED_SONG_CMD+ " [listener_name] [song_name]";

    public static final String FIND_ARTISTS_BY_SONG_CMD = "find_artists_by_song";
    public static final String FIND_ARTISTS_BY_SONG_CMD_WITH_OPTIONS = FIND_ARTISTS_BY_SONG_CMD + " [song_name]";

    public static final String FIND_SONGS_BY_ARTIST_CMD = "find_songs_by_artist";
    public static final String FIND_SONGS_BY_ARTIST_CMD_WITH_OPTIONS = FIND_SONGS_BY_ARTIST_CMD + " [artist_name]";

    public static final String TEST_CMD ="test";
    public static final String QUIT_CMD ="quit";
}
