package com.Diplom.BackEnd.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Report extends SuperClass<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(columnDefinition = "json")
    private JsonNode data;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
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
