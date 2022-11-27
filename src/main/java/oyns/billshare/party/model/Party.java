package oyns.billshare.party.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "parties")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "party_name", nullable = false, unique = true)
    private String name;

    @Column(name = "paid")
    private Boolean isPaid;

    @Column(name = "initiator_id", nullable = false, unique = true)
    private UUID initiator;

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
