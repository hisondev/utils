package io.github.hison.utils;
/**
 * The {@code UtilsException} class represents runtime exceptions raised by the
 * {@link Utils} utility methods. It extends {@link RuntimeException} to signal
 * failures (e.g. invalid arguments or property-loading errors) without forcing
 * checked-exception handling.
 *
 * <p>This exception can be used to wrap other exceptions, providing a higher-level explanation
 * of what went wrong during a utility operation.</p>
 *
 * @author Hani son
 * @version 2.0.1
 */
public class UtilsException extends RuntimeException {
    /**
     * Constructs a new {@code UtilsException} with the specified detail message.
     * 
     * @param message the detail message
     */
    public UtilsException(String message) {
        this(message, null);
    }

    /**
     * Constructs a new {@code UtilsException} with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception (a {@code null} value is permitted, and indicates that the cause is nonexistent or unknown)
     */
    public UtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code UtilsException} with the specified cause and a detail message of {@code (cause==null ? null : cause.toString())}.
     * 
     * @param cause the cause of the exception (a {@code null} value is permitted, and indicates that the cause is nonexistent or unknown)
     */
    public UtilsException(Throwable cause) {
        super(cause.toString(), cause);
    }

    /**
     * Constructs a new {@code UtilsException} using another {@code UtilsException} as its cause.
     * 
     * @param cause the cause of the exception
     */
    public UtilsException(UtilsException cause) {
        super(cause.toString(), cause);
    }
}
