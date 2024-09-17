package com.example.NihonStudyGuide.entities;

import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

public class BaseEntity {
//    @Column(name = "active")
//    private Boolean active = true;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate = new Date();

    @LastModifiedDate
    @Column(name = "updated_date")
    private Date updatedDate = new Date();

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

}
