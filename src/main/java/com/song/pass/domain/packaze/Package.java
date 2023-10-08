package com.song.pass.domain.packaze;

import com.song.pass.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
@Table(name = "package")
public class Package extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer packageSeq;

    private String packageName;
    private Integer count;
    private Integer period;

    @Builder
    public Package(String packageName, Integer count, Integer period) {
        this.packageName = packageName;
        this.count = count;
        this.period = period;
    }
}
