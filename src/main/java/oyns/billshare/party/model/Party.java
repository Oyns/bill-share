package oyns.billshare.party.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import oyns.billshare.user.model.User;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "parties")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    UUID id;

    @Column(name = "party_name", unique = true)
    String name;

    @Column(name = "paid")
    Boolean isPaid;

    @Column(name = "initiator_id", nullable = false, unique = true)
    UUID initiator;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "users_party", joinColumns = @JoinColumn(name = "party_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Party party = (Party) o;
        return id.equals(party.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
