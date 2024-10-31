package me.datafox.borg_runner.exception;

/**
 * @author datafox
 */
public class NonZeroExitCodeException extends Exception {
    public NonZeroExitCodeException(String s) {
        super(s);
    }
}
