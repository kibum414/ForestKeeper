package com.ssafy.forestkeeper.domain.dao.plogging;

import com.ssafy.forestkeeper.domain.dao.BaseEntity;
import com.ssafy.forestkeeper.domain.dao.mountain.Mountain;
import com.ssafy.forestkeeper.domain.dao.user.User;
import com.ssafy.forestkeeper.domain.enums.CommunityCode;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Matching extends BaseEntity {

    @Column(name = "match_title")
    private String title;

    @Column(name = "match_content")
    @Lob
    private String content;

    @Column(name = "match_create_time")
    private LocalDateTime createTime;

    @Column(name = "plogging_date")
    private LocalDateTime ploggingDate;

    @Column(name = "community_views")
    private long views;

    @Column(name = "is_closed")
    private boolean isClosed;

    @Column(name = "total")
    private int total;

    @Column(name = "participant")
    private int participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id")
    private Mountain mountain;

//     글 수정
    public void changeMatch(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 조회수 증가
    public void increaseViews() {
        this.views += 1;
    }
}
