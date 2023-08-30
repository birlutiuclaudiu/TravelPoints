package com.disi.travelpoints.model.entity;


import com.disi.travelpoints.model.enums.TokenType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "account_tokens")
public class AccountTokenValidation {

    private static final long serialVersionUID = 1L;
    public static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "token", updatable = false, nullable = false)
    @NotNull
    private String token;

    @OneToOne(targetEntity = TravelUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private TravelUser travelUser;

    private LocalDateTime expiryDate;

    @Column(name = "type", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private TokenType type;

    @Column(name = "new_password", nullable = true)
    private String newPassword;
}
