package com.paranormal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorModel implements Serializable {

    private ZonedDateTime timestamp;
    private int statusCode;
    private String errorCode;
    private String message;

}
