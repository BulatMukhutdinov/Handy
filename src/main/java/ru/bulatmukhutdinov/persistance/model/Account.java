package ru.bulatmukhutdinov.persistance.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jboss.aerogear.security.otp.api.Base32;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String description;

    private Integer price;

    private String ownTelegram;

    private String groupTelegram;

    private String website;

    private String userPicUri;

    private String email;

    @Column(length = 60)
    private String password;

    private boolean enabled;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Service> services;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;


    public Account() {
        super();
        this.enabled = false;
        this.description = "";
        this.price = 0;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (enabled != account.enabled) return false;
        if (id != null ? !id.equals(account.id) : account.id != null) return false;
        if (firstName != null ? !firstName.equals(account.firstName) : account.firstName != null) return false;
        if (lastName != null ? !lastName.equals(account.lastName) : account.lastName != null) return false;
        if (description != null ? !description.equals(account.description) : account.description != null) return false;
        if (price != null ? !price.equals(account.price) : account.price != null) return false;
        if (ownTelegram != null ? !ownTelegram.equals(account.ownTelegram) : account.ownTelegram != null) return false;
        if (groupTelegram != null ? !groupTelegram.equals(account.groupTelegram) : account.groupTelegram != null)
            return false;
        if (website != null ? !website.equals(account.website) : account.website != null) return false;
        if (userPicUri != null ? !userPicUri.equals(account.userPicUri) : account.userPicUri != null) return false;
        if (email != null ? !email.equals(account.email) : account.email != null) return false;
        if (password != null ? !password.equals(account.password) : account.password != null) return false;
        if (services != null ? !services.equals(account.services) : account.services != null) return false;
        return roles != null ? roles.equals(account.roles) : account.roles == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (ownTelegram != null ? ownTelegram.hashCode() : 0);
        result = 31 * result + (groupTelegram != null ? groupTelegram.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (userPicUri != null ? userPicUri.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (services != null ? services.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }
}
