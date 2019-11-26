package co.brick.kszerlag.jmovie.fault;

import java.util.NoSuchElementException;

public class NoSuchMovieException extends NoSuchElementException {

    public NoSuchMovieException(String message) {
        super(message);
    }
}
