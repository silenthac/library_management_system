    package com.prashant.library.library_management.entity;


    import jakarta.persistence.*;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;

    import java.time.LocalDate;


    @Entity
    @Table(name="users")
    public class User {
        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        private long id;
        @NotBlank(message = "Full Name is Required")
        private String name;

        @Email(message = "EMAIL should be valid")
        @Column(unique = true,nullable = false)
        private String email;
        @NotBlank(message = "Password is required")
        private String password;

        @Enumerated(EnumType.STRING)
        private Role role;




        @OneToOne(mappedBy = "bookedBy", cascade = CascadeType.ALL)
        private Seat seat;

        public Seat getSeat() {
            return seat;
        }

        public void setSeat(Seat seat) {
            this.seat = seat;
        }

        private LocalDate registrationDate;
        private boolean feesPaid;
        private boolean isActive;
        private int absentCount;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getFullName() { return name; }
        public void setFullName(String fullName) { this.name = fullName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }

        public LocalDate getRegistrationDate() { return registrationDate; }
        public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

        public boolean isFeesPaid() { return feesPaid; }
        public void setFeesPaid(boolean feesPaid) { this.feesPaid = feesPaid; }

        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }

        public int getAbsentCount() { return absentCount; }
        public void setAbsentCount(int absentCount) { this.absentCount = absentCount; }
    }
