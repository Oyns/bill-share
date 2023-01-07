package oyns.billshare.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;
import oyns.billshare.user.model.User;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    UUID id;

    @Column(name = "item_name", nullable = false)
    String name;

    @PositiveOrZero
    @Column(name = "price")
    Double price;

    @Positive
    @Column(name = "amount")
    Integer amount;

    @Column(name = "equally")
    Boolean equally;

    @PositiveOrZero
    @Column(name = "discount")
    Double discount;
    @Column(name = "user_id")
    @JoinTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    UUID user;
    @Column(name = "created_on", columnDefinition = "TIMESTAMP")
    LocalDateTime createdOn;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_items", joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    Set<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
