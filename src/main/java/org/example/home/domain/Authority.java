//package org.example.home.domain;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Data
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "authorities")
//public class Authority {
//
//    @Id
//    private long id;
//
//    @Column(name = "authority", length = 50, nullable = false)
//    private String authority;
//
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_login")
//    private AppUser appUser;
//}
