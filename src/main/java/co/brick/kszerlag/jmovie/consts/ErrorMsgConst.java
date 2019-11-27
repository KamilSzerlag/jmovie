package co.brick.kszerlag.jmovie.consts;

public class ErrorMsgConst {

    public static final String INVALID_PARAMETERS = "Invalid parameters";

    public static final String INVALID_DESCRIPTION_MIN_SIZE = "Minimum description size is 50 chars.";
    public static final String INVALID_DESCRIPTION_MAX_SIZE = "Maximum description size is 300 chars.";
    public static final String INVALID_MOVIE_NAME = "Movie name can't be empty";

    public static final String MOVIE_ALREADY_EXISTS = "Movie with inserted name and description already exists";
    public static final String UPDATE_MOVIE_WITH_THIS_ID_NOT_EXISTS = "There is no movie under id:";
    public static final String IMAGE_UPDATE_MOVIE_WITH_THIS_ID_NOT_EXISTS = "Image can't be uploaded, there is no movie under id:";

    public static final String MOVIE_SAVE_FAILED = "Save movie operation failed";
    public static final String MOVIE_UPDATE_FAILED = "Update movie operation failed";
    public static final String MOVIE_DELETE_FAILED = "Delete movie operation failed";

    public static final String INVALID_URI = "Created URI is incorrect";
    public static final String UNEXPECTED_ERROR = "An unexpected error has occurred";
}
