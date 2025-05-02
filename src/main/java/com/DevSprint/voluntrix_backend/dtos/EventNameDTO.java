package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventNameDTO implements Serializable {

    private Long eventId;
    private String eventTitle;

}
