package ru.bulatmukhutdinov.dto;

import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.persistance.model.Service;
import ru.bulatmukhutdinov.validation.PasswordMatches;
import ru.bulatmukhutdinov.validation.ValidEmail;
import ru.bulatmukhutdinov.validation.ValidPassword;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PasswordMatches
public class AccountDto {
    @NotNull
    @Size(min = 1)
    private String firstName;

    @NotNull
    @Size(min = 1)
    private String lastName;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

    @ValidEmail
    @NotNull
    @Size(min = 1)
    private String email;

    private String phone;

    private String description;

    private List<ServiceDto> serviceDtos;

    private String userPicUri;

    private int age;

    private Double lengthOfService;

    private Integer workNumber;

    private String lastWork;

    private String city;

    public AccountDto(String firstName, String lastName, String password, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public AccountDto() {
    }

    public AccountDto(Account account) {
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.email = account.getEmail();
        this.phone = account.getPhone();
        this.description = account.getDescription();
        this.serviceDtos = new ArrayList<>();
        this.userPicUri = account.getUserPicUri();
        this.age = account.getAge();
        this.lengthOfService = account.getLengthOfService();
        this.workNumber = account.getWorkNumber();
        this.lastWork = stringifyDate(account.getLastWork());
        this.city = account.getCity();
        for (Service service : account.getServices()) {
            serviceDtos.add(new ServiceDto(service.getDescription()));
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String stringifyDate(Date date) {
        if (date == null) {
            return "-";
        }
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String reportDate = df.format(date);
        return reportDate;
    }

    public Integer getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(Integer workNumber) {
        this.workNumber = workNumber;
    }

    public String getLastWork() {
        return lastWork;
    }

    public void setLastWork(String lastWork) {
        this.lastWork = lastWork;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getLengthOfService() {
        return lengthOfService;
    }

    public void setLengthOfService(Double lengthOfService) {
        this.lengthOfService = lengthOfService;
    }

    public String getUserPicUri() {
        return userPicUri;
    }

    public void setUserPicUri(String userPicUri) {
        this.userPicUri = userPicUri;
    }

    public List<ServiceDto> getServiceDtos() {
        return serviceDtos;
    }

    public void setServiceDtos(List<ServiceDto> serviceDtos) {
        this.serviceDtos = serviceDtos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    private Integer role;

    public Integer getRole() {
        return role;
    }

    public void setRole(final Integer role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(final String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }


}
