package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Genre implements Comparable<Genre> {
    private Integer id;
    private String name;

    @Override
    public int compareTo(Genre o) {
        return this.id.compareTo(o.id);
    }
}
