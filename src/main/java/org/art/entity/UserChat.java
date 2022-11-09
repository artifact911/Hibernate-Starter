package org.art.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity(name = "users_chat")
public class UserChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // не обязательно, тк само получилост бы такое название
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    // instant заюзали для примера показать, что он маппится на timestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private String createdBy;

    public void setUser(User user) {
        this.user = user;
        this.user.getUserChats().add(this);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
        this.chat.getUserChats().add(this);
    }
}