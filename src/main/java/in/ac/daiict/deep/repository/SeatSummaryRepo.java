package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.SeatSummary;
import in.ac.daiict.deep.entity.compositekeys.SeatSummaryPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatSummaryRepo extends JpaRepository<SeatSummary, SeatSummaryPK> {
}
