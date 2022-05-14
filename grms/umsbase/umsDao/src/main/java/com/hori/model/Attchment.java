package com.hori.model;
// Generated 2017-1-11 9:01:12 by Hibernate Tools 4.3.1.Final


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * Attchment generated by hbm2java
 */
@Entity
@Table(name="attchment")
public class Attchment  implements java.io.Serializable {


     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
     private String name;
     private String location;
     private Integer size;
     private String type;  
     private Date createTime;

    public Attchment() {
    }

	
    public Attchment(String id) {
        this.id = id;
    }
    public Attchment(String id, String name, String location, Integer size, String type, Date createTime) {
       this.id = id;
       this.name = name;
       this.location = location;
       this.size = size;
       this.type = type;
       this.createTime = createTime;
    }
   
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "com.hori.hibernate.UUIDGenerator")
    @GeneratedValue(generator = "system-uuid")
    @Column(name="id", nullable=false, insertable=true, updatable=false, length=32)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    
    @Column(name="name", length=128)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    
    @Column(name="location", length=128)
    public String getLocation() {
        return this.location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }

    
    @Column(name="size")
    public Integer getSize() {
        return this.size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }

    
    @Column(name="type")
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time", length=19 ,insertable=false, updatable=false)
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}


