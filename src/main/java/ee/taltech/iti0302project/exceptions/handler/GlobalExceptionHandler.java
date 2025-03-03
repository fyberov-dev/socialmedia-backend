package ee.taltech.iti0302project.exceptions.handler;

import ee.taltech.iti0302project.exceptions.InvalidUserException;
import ee.taltech.iti0302project.exceptions.chat.AlreadyAddedToChatException;
import ee.taltech.iti0302project.exceptions.chat.ChatNoAccessException;
import ee.taltech.iti0302project.exceptions.chat.ChatNotFoundException;
import ee.taltech.iti0302project.exceptions.post.PostEditNoAccessException;
import ee.taltech.iti0302project.exceptions.post.PostNotFoundException;
import ee.taltech.iti0302project.exceptions.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<Object> handleInvalidUserException(InvalidUserException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessages.add(error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorMessages);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<List<String>> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.badRequest().body(List.of(ex.getMessage()));
    }

    @ExceptionHandler(ChatNotFoundException.class)
    public ResponseEntity<String> handleChatNotFoundException(ChatNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> handlePostNotFoundException(PostNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PostEditNoAccessException.class)
    public ResponseEntity<String> handlePostEditNoAccessException(PostEditNoAccessException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ChatNoAccessException.class)
    public ResponseEntity<String> handlePostNotAccessException(ChatNoAccessException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AlreadyAddedToChatException.class)
    public ResponseEntity<String> handleAlreadyAddedToChatException(AlreadyAddedToChatException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}