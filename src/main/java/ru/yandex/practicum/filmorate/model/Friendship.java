package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    private Integer requesterId;
    private Integer addresseeId;
    private FriendshipStatus status;
}

