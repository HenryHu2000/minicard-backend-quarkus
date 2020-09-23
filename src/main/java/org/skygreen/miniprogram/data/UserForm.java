package org.skygreen.miniprogram.data;

import javax.ws.rs.FormParam;

public class UserForm {

  @FormParam("name")
  private String name;

  @FormParam("title")
  private String title;

  @FormParam("organization")
  private String organization;

  @FormParam("address")
  private String address;

  @FormParam("postcode")
  private String postcode;

  @FormParam("telephone")
  private String telephone;

  @FormParam("mobile")
  private String mobile;

  @FormParam("fax")
  private String fax;

  @FormParam("email")
  private String email;

  @FormParam("website")
  private String website;

  public UserForm() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

}
