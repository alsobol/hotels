package hotels.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hotels.model.Hotel;
import hotels.repository.HotelRepository;
import hotels.service.HotelService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class HotelServiceImpl implements HotelService {

	private HotelRepository hotelRepository;

	@Autowired
	public HotelServiceImpl(HotelRepository hotelRepository) {
		this.hotelRepository = hotelRepository;
	}

	@Override
	public List<Hotel> getAll() {
		return hotelRepository.findAll();
	}

	@Override
	public Hotel getById(Long id) {
		return hotelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
	}

	@Override
	public void delete(Long id) {
		hotelRepository.deleteById(id);

	}

	@Override
	public void save(Hotel hotel) {
		hotelRepository.save(hotel);

	}

	@Override
	public List<Hotel> getByBrand(String barnd) {
		return hotelRepository.findByBrand(barnd);
	}

	@Override
	public List<Hotel> getByAmenities(String amenities) {
		return hotelRepository.findByAmenities(amenities);
	}

	@Override
	public Map<String, Integer> getHistogram(String param) {
		List<Hotel> hotels = hotelRepository.findAll();
		Map<String, Integer> histogram = new HashMap<>();
		for (Hotel hotel : hotels) {
			String key = "";
			switch (param) {
			case "brand":
				key = hotel.getBrand();
				break;
			case "city":
				key = hotel.getAddress().getCity();
				break;
			case "country":
				key = hotel.getAddress().getCountry();
				break;
			case "amenities":
				for (String amenity : hotel.getAmenities()) {
					histogram.put(amenity, histogram.getOrDefault(amenity, 0) + 1);
				}
				continue;
			}
			histogram.put(key, histogram.getOrDefault(key, 0) + 1);
		}
		return histogram;
	}

	@Override
	public List<Hotel> searchHotels(String name, String brand, String city, String country, String amenities) {
		List<Hotel> hotels = hotelRepository.findAll();
		return hotels.stream().filter(hotel -> (name == null || hotel.getName().equalsIgnoreCase(name)))
				.filter(hotel -> (brand == null || hotel.getBrand().equalsIgnoreCase(brand)))
				.filter(hotel -> (city == null || hotel.getAddress().getCity().equalsIgnoreCase(city)))
				.filter(hotel -> (country == null || hotel.getAddress().getCountry().equalsIgnoreCase(country)))
				.filter(hotel -> (amenities == null || hotel.getAmenities().contains(amenities)))
				.collect(Collectors.toList());
	}

}
