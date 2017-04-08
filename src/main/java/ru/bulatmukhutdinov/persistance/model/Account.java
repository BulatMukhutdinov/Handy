package ru.bulatmukhutdinov.persistance.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String ownTelegram;

    private String groupTelegram;

    private String website;

    private String userPicUri;

    private String email;

    private String phone;

    private String description;

    @Column(length = 60)
    private String password;

    private boolean enabled;

    private Integer age;

    private Double lengthOfService;

    private Integer workNumber;

    private Date lastWork;

    private String city;

    private Boolean isWizardVisited;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Service> services;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Photo> photos;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;


    public Account() {
        super();
        this.enabled = false;
        this.isWizardVisited = false;
    }

    public Boolean getWizardVisited() {
        return isWizardVisited;
    }

    public void setWizardVisited(Boolean wizardVisited) {
        isWizardVisited = wizardVisited;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(Integer workNumber) {
        this.workNumber = workNumber;
    }

    public Date getLastWork() {
        return lastWork;
    }

    public void setLastWork(Date lastWork) {
        this.lastWork = lastWork;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getLengthOfService() {
        return lengthOfService;
    }

    public void setLengthOfService(Double lengthOfService) {
        this.lengthOfService = lengthOfService;
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

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Set<Role> roles) {
        this.roles = roles;
    }


    public String getOwnTelegram() {
        return ownTelegram;
    }

    public void setOwnTelegram(String ownTelegram) {
        this.ownTelegram = ownTelegram;
    }

    public String getGroupTelegram() {
        return groupTelegram;
    }

    public void setGroupTelegram(String groupTelegram) {
        this.groupTelegram = groupTelegram;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUserPicUri() {
        return userPicUri;
    }

    public void setUserPicUri(String userPicUri) {
        this.userPicUri = userPicUri;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

}
