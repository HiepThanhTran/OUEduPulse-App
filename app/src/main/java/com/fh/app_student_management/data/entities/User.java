package com.fh.app_student_management.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.PasswordUtils;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "full_name")
    private String fullName;
    @ColumnInfo(name = "date_of_birth")
    private Date dob;
    private boolean gender;
    private String address;
    private String email;
    private String password;
    private Constants.Role role;
    @ColumnInfo(name = "faculty_id")
    private long facultyId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PasswordUtils.hashPassword(password);
    }

    public boolean checkPassword(String password) {
        return PasswordUtils.verifyPassword(password, this.password);
    }

    public Constants.Role getRole() {
        return this.role;
    }

    public void setRole(Constants.Role role) {
        this.role = role;
    }

    public long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(long facultyId) {
        this.facultyId = facultyId;
    }

    @NonNull
    @Override
    public String toString() {
        return this.fullName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }
}