package com.ssafy.forestkeeper.domain.dao.chat;

import com.ssafy.forestkeeper.domain.dao.BaseEntity;
import com.ssafy.forestkeeper.domain.dao.user.User;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomUser extends BaseEntity {

    @Column(name = "delete_yn")
    private boolean delete;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void changeDelete() {
        this.delete = true;
    }

}

