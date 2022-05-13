package com.ssafy.forestkeeper.application.dto.chat;

import com.ssafy.forestkeeper.domain.enums.MessageCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ApiModel("ChatMessageDTO")
@Builder
@Getter
@ToString
public class ChatMessageDTO {

    @ApiModelProperty(name = "채팅방 ID")
    private String roomId;

    @ApiModelProperty(name = "메시지 인덱스")
    private long index;

    @ApiModelProperty(name = "메시지 코드")
    private MessageCode messageCode;

    @ApiModelProperty(name = "사용자 ID")
    private String userId;

    @ApiModelProperty(name = "사용자 닉네임")
    private String nickname;

    @ApiModelProperty(name = "메시지 내용")
    private String content;

    @ApiModelProperty(name = "메시지 보낸 시간")
    private LocalDateTime sendTime;

    @ApiModelProperty(name = "메시지 삭제 여부")
    private boolean delete;

}
