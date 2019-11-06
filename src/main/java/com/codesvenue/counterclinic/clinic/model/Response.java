package com.codesvenue.counterclinic.clinic.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@Log4j
public class Response<T> {
    private T data;
    private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    public Response(T data) {
        this.data = data;
    }

    public static Response newInstance() {
        return new Response<>();
    }

    public static Response newInstance(Object data) {
        return new Response<>(data);
    }

    public Response<T> data(T data) {
        this.data = data;
        return this;
    }
}
