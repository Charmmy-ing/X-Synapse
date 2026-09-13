package com.charmmy.x_synapse.pojo.DTO;

import com.charmmy.x_synapse.anno.State;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long userId;
    @NotEmpty
    private String content;
    @State
    private Integer state;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
