package net.oscer.config.provider;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.util.BirthDate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kz
 */
public class AuthProfile implements Serializable {

    private static final long serialVersionUID = -4044464088178845414L;

    public AuthProfile() {

    }

    public AuthProfile(Profile profile) {
        if (profile == null) {
            return;
        }
        this.email = profile.getEmail();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.country = profile.getCountry();
        this.language = profile.getLanguage();
        this.fullName = profile.getFullName();
        this.displayName = profile.getDisplayName();
        BirthDate birthDate = profile.getDob();
        if (birthDate != null) {
            this.birthday = new Date(birthDate.getYear(), birthDate.getMonth(), birthDate.getDay());
        }
        this.gender = profile.getGender();
        this.location = profile.getLocation();
        this.validatedId = profile.getValidatedId();
        this.profileImageURL = profile.getProfileImageURL();
        this.providerId = profile.getProviderId();
    }

    /**
     * Email
     */
    private String email;

    /**
     * First Name
     */
    private String firstName;

    /**
     * Last Name
     */
    private String lastName;

    /**
     * Country
     */
    private String country;

    /**
     * Language
     */
    private String language;

    /**
     * Full Name
     */
    private String fullName;

    /**
     * Display Name
     */
    private String displayName;

    /**
     * Date of Birth
     */
    private Date birthday;

    /**
     * Gender
     */
    private String gender;

    /**
     * Location
     */
    private String location;

    /**
     * Validated Id
     */
    private String validatedId;

    /**
     * profile image URL
     */
    private String profileImageURL;

    /**
     * provider id with this profile associates
     */
    private String providerId;

    /**
     * Retrieves the first name
     *
     * @return String the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Updates the first name
     *
     * @param firstName the first name of user
     */
    public AuthProfile setFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * Retrieves the last name
     *
     * @return String the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Updates the last name
     *
     * @param lastName the last name of user
     */
    public AuthProfile setLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Returns the email address.
     *
     * @return email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the email
     *
     * @param email the email of user
     */
    public AuthProfile setEmail(final String email) {
        this.email = email;
        return this;
    }

    /**
     * Retrieves the validated id
     *
     * @return String the validated id
     */
    public String getValidatedId() {
        return validatedId;
    }

    /**
     * Updates the validated id
     *
     * @param validatedId the validated id of user
     */
    public AuthProfile setValidatedId(final String validatedId) {
        this.validatedId = validatedId;
        return this;
    }

    /**
     * Retrieves the display name
     *
     * @return String the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Updates the display name
     *
     * @param displayName the display name of user
     */
    public AuthProfile setDisplayName(final String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Retrieves the country
     *
     * @return String the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Updates the country
     *
     * @param country the country of user
     */
    public AuthProfile setCountry(final String country) {
        this.country = country;
        return this;
    }

    /**
     * Retrieves the language
     *
     * @return String the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Updates the language
     *
     * @param language the language of user
     */
    public AuthProfile setLanguage(final String language) {
        this.language = language;
        return this;
    }

    /**
     * Retrieves the full name
     *
     * @return String the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Updates the full name
     *
     * @param fullName the full name of user
     */
    public AuthProfile setFullName(final String fullName) {
        this.fullName = fullName;
        return this;
    }

    /**
     * Retrieves the date of birth
     *
     * @return the date of birth different providers may use different formats
     */
    public Date getBirthday() {
        return this.birthday;
    }

    /**
     * Updates the date of birth
     *
     * @param birthday the date of birth of user
     */
    public AuthProfile setBirthday(final Date birthday) {
        this.birthday = birthday;
        return this;
    }

    /**
     * Retrieves the gender
     *
     * @return String the gender - could be "Male", "M" or "male"
     */
    public String getGender() {
        return gender;
    }

    /**
     * Updates the gender
     *
     * @param gender the gender of user
     */
    public AuthProfile setGender(final String gender) {
        this.gender = gender;
        return this;
    }

    /**
     * Retrieves the location
     *
     * @return String the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Updates the location
     *
     * @param location the location of user
     */
    public AuthProfile setLocation(final String location) {
        this.location = location;
        return this;
    }

    /**
     * Retrieves the profile image URL
     *
     * @return String the profileImageURL
     */
    public String getProfileImageURL() {
        return profileImageURL;
    }

    /**
     * Updates the profile image URL
     *
     * @param profileImageURL profile image URL of user
     */
    public AuthProfile setProfileImageURL(final String profileImageURL) {
        this.profileImageURL = profileImageURL;
        return this;
    }

    /**
     * Retrieves the provider id with this profile associates
     *
     * @return the provider id
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * Updates the provider id
     *
     * @param providerId the provider id
     */
    public AuthProfile setProviderId(final String providerId) {
        this.providerId = providerId;
        return this;
    }

    @Override
    public String toString() {
        return "AuthProfile{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", language='" + language + '\'' +
                ", fullName='" + fullName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", birthday=" + birthday +
                ", gender='" + gender + '\'' +
                ", location='" + location + '\'' +
                ", validatedId='" + validatedId + '\'' +
                ", profileImageURL='" + profileImageURL + '\'' +
                ", providerId='" + providerId + '\'' +
                '}';
    }
}
