package ru.skillbox.diplom.alpha.microservice.post.exception;

import org.apache.commons.lang3.exception.ContextedException;
import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import ru.skillbox.diplom.alpha.microservice.post.response.ErrorPostRs;

import java.net.ConnectException;
import java.util.NoSuchElementException;

/**
 * PostException
 *
 * @author Ruslan Akbashev
 */
//@ControllerAdvice
public class PostException {
    private final String ERROR_INPUT_DATA = "error input data";

    private final String ERROR_CONNECT = "error connect account-service";

    @ExceptionHandler({NullPointerException.class})
    public final ResponseEntity<ErrorPostRs> nullPointerException(Exception ex, WebRequest request) {
        ErrorPostRs errorPostRs = new ErrorPostRs();
        errorPostRs.setError("NullPointer");
        errorPostRs.setErrorDescription("The specified value is out of range or does not exist");
        return ResponseEntity.badRequest().body(errorPostRs);
    }

    @ExceptionHandler(ContextedException.class)
    public final ResponseEntity<ErrorPostRs> contextException(Exception ex, WebRequest request) {
        ErrorPostRs errorPostRs = new ErrorPostRs();
        errorPostRs.setError("ContextPointer");
        errorPostRs.setErrorDescription(ex.getMessage());
        return ResponseEntity.badRequest().body(errorPostRs);
    }

    @ExceptionHandler(ExceptionInInitializerError.class)
    public final ResponseEntity<ErrorPostRs> errorException(Exception ex, WebRequest request) {
        ErrorPostRs errorPostRs = new ErrorPostRs();
        errorPostRs.setError(ERROR_INPUT_DATA);
        errorPostRs.setErrorDescription(ex.getLocalizedMessage().substring(
                ex.getLocalizedMessage().lastIndexOf(": ") + 1));
        return ResponseEntity.badRequest().body(errorPostRs);
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public final ResponseEntity<ErrorPostRs> indexOutOfBoundException(Exception ex, WebRequest request) {
        ErrorPostRs errorPostRs = new ErrorPostRs();
        errorPostRs.setError("indexOutOfBoundException");
        errorPostRs.setErrorDescription("Index in database not found.");
        return ResponseEntity.badRequest().body(errorPostRs);
    }

    @ExceptionHandler(PropertyValueException.class)
    public final ResponseEntity<ErrorPostRs> propertyValueException(Exception ex, WebRequest request, String string) {
        ErrorPostRs errorPostRs = new ErrorPostRs();
        errorPostRs.setError(ERROR_INPUT_DATA);
        errorPostRs.setErrorDescription(ex.getLocalizedMessage().substring(
                ex.getLocalizedMessage().lastIndexOf(".") + 1));
        return ResponseEntity.badRequest().body(errorPostRs);
    }


    @ExceptionHandler(ConnectException.class)
    public final ResponseEntity<ErrorPostRs> connectionException(Exception ex, WebRequest request) {
        ErrorPostRs errorPostRs = new ErrorPostRs();
        errorPostRs.setError(ERROR_CONNECT);
        errorPostRs.setErrorDescription(ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(errorPostRs);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<ErrorPostRs> noSuchElementException(Exception ex, WebRequest request) {
        ErrorPostRs errorPostRs = new ErrorPostRs();
        errorPostRs.setError(ERROR_INPUT_DATA);
        errorPostRs.setErrorDescription(ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(errorPostRs);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ErrorPostRs> illegalArgumentException(Exception ex, WebRequest request) {
        ErrorPostRs errorPostRs = new ErrorPostRs();
        errorPostRs.setError(ERROR_INPUT_DATA);
        errorPostRs.setErrorDescription(ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(errorPostRs);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public final ResponseEntity<ErrorPostRs> httpException(Exception ex, WebRequest request) {
        ErrorPostRs errorPostRs = new ErrorPostRs();
        errorPostRs.setError("Account not found");
        errorPostRs.setErrorDescription(String.valueOf(ex.getMessage()));
        return ResponseEntity.badRequest().body(errorPostRs);
    }
}
