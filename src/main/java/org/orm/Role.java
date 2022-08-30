package org.orm;

import lombok.Data;
import org.orm.annotation.Column;
import org.orm.annotation.Table;

@Table(name = "role")
@Data
public class Role {
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
