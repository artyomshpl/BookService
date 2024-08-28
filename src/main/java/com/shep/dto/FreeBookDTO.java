package com.shep.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FreeBookDTO {
    private Long id;
    private Long bookId;
    private LocalDateTime borrowedTime = LocalDateTime.now();
    private LocalDateTime returnTime;
}
