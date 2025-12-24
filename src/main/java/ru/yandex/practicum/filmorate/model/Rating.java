package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor

@AllArgsConstructor
public class Rating implements Comparable<Rating> {
    private Integer id;
    private String name;

    @Override
    public int compareTo(Rating o) {
        return this.id.compareTo(o.id);
    }
}
