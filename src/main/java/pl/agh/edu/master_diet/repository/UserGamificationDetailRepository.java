package pl.agh.edu.master_diet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.agh.edu.master_diet.core.model.database.UserGamificationDetail;

public interface UserGamificationDetailRepository extends JpaRepository<UserGamificationDetail, Long> {
}
