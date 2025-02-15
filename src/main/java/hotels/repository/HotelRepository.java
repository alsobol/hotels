package hotels.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hotels.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

	List<Hotel> findByBrand(String brand);

	List<Hotel> findByAmenities(String amenities);

}
