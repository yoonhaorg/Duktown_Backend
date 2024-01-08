package com.duktown.domain.repairApply.dto;

import com.duktown.domain.repairApply.entity.RepairApply;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.HallName;
import com.duktown.global.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RepairApplyDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepairApplyRequest{
        @NotNull(message = "카테고리는 필수 입력값입니다.")
        @Min(value = 0, message = "카테고리는 0에서 2사이의 정수 값입니다.")
        @Max(value = 2, message = "카테고리는 0에서 2사이의 정수 값입니다.")
        private Integer hallName;

        private String roomNumber;

        @NotBlank(message = "내용은 필수 입력값입니다")
        private String content;

        // DTO -> Entity
        public RepairApply toEntity(User user, HallName hallName){
            return RepairApply.builder()
                    .user(user)
                    .hallName(hallName)
                    .roomNumber(roomNumber)
                    .content(content)
                    .checked(false)
                    .solved(false)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class RepairApplyResponse{

        private Long id;
        private Long userId;
        private Integer hallName;
        private String roomNumber;
        private String content;
        private Boolean checked;
        private Boolean solved;
        //private String datetime;

        public RepairApplyResponse(RepairApply apply){
            this.id = apply.getId();
            this.userId = apply.getUser().getId();
            this.hallName = apply.getHallName().getValue();
            this.roomNumber = apply.getRoomNumber();
            this.content = apply.getContent();
            this.checked = apply.getChecked();
            this.solved = apply.getSolved();
            //this.datetime = DateUtil.convert(apply.getCreatedAt());
        }
    }

    @Getter
    public static class RepairApplyListResponse{
        private List<RepairApply> content;

        public RepairApplyListResponse(List<RepairApply> content){
            this.content = content;
        }
    }

}
