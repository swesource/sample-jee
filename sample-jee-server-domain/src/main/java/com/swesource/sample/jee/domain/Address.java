package com.swesource.sample.jee.domain;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author arnie
 */
@Entity
@Audited
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int ZIP_LENGTH = 5;
    private static final int COUNTRY_MAX = 2;
    private static final int COUNTRY_MIN = 2;

    @Id
    @GeneratedValue
    private Long id;

    private String street;

    private String city;

    @Column(length = ZIP_LENGTH)
    @Digits(integer = ZIP_LENGTH, fraction = 0)
    private String zip;

    /* ISO 3166 A2 compliant country code */
    @Column(length = COUNTRY_MAX)
    @Size(min = COUNTRY_MIN, max = COUNTRY_MAX)
    private String country;

    @Temporal(value = TemporalType.DATE)
    private Date movingInDate;

    @Override
    public String toString() {
        return this.getClass().getName() + "@" + hashCode() + " [id = " + id + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getMovingInDate() {
        return movingInDate;
    }

    public void setMovingInDate(Date movingInDate) {
        this.movingInDate = movingInDate;
    }
}