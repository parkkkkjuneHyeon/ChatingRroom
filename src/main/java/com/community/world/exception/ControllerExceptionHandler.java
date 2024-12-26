package com.community.world.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ControllerExceptionHandler {

//    @ExceptionHandler(MemberException.class)
//    public ResponseEntity<ErrorResponseDto> handleMemberException(
//            MemberException e
//    ) {
//        return new ResponseEntity<>(
//                new ErrorResponseDto(e.getMessage())
//                , HttpStatus.BAD_REQUEST\

//        );
//    }
    @ExceptionHandler(MemberException.class)
    public ModelAndView handleMemberException(
            MemberException e
    ) {
        return new ModelAndView("login")
                .addObject("error",e.getDescription());
    }
    @ExceptionHandler(ChatingRoomException.class)
    public ModelAndView handleChatingRoomException(
            ChatingRoomException e
    ) {
        return new ModelAndView("home")
                .addObject("error",e.getDescription());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Exception> handleExceptionAll(
            Exception e
    ) {
        return new ResponseEntity<>(
                new Exception(e.getMessage())
                , HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
