package com.Diplom.BackEnd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import jdk.jfr.Timestamp;
import lombok.*;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "report")
@TypeDef(
        typeClass = JsonStringType.class,
        defaultForType = JsonNode.class
)
@ToString
public class Report extends SuperClass<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(columnDefinition = "json")
    private JsonNode data;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @Column
    @Enumerated(EnumType.STRING)
    private EReportStatus status;

    @Timestamp
    @CreatedDate
    @Column
    private Date created;

    @Timestamp
    @UpdateTimestamp
    @Column
    private Date updated;
}
