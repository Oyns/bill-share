package oyns.billshare.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import oyns.billshare.party.model.Party;

import java.util.UUID;

@Repository
public interface PartyRepository extends JpaRepository<Party, UUID> {
}
