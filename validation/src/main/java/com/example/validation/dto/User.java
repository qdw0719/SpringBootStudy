package com.example.validation.dto;

import com.example.validation.annotation.YearMonth;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class User {

    @NotBlank
    private String name;

    @Min(value = 19, message = "성인만 이용하실 수 있습니다.")
    private int age;

    @Email(message = "이메일 주소의 양식을 확인해주세요.")
    private String email;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "핸드폰 번호의 양식을 확인해주세요. (ex. 01x-xxxx-xxxx)")
    private String phone;

    @YearMonth
    private String yearMonth;

//    @AssertTrue(message = "날짜는 월 단위 까지만 입력하실 수 있습니다. 예시의 형식을 맞춰주세요 (ex. yyyyMM)")
//    private boolean isYearMonthValidation() {
//        System.out.println("assert true call");
//        try {
//            LocalDate localDate = LocalDate.parse(this.yearMonth + "01", DateTimeFormatter.ofPattern("yyyyMM"));
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", yearMonth='" + yearMonth + '\'' +
                '}';
    }

}
