package hotels.service;

import java.util.List;
import java.util.Map;

import hotels.model.Hotel;

public interface HotelService extends BaseService<Hotel> {

	List<Hotel> getByBrand(String city);

	List<Hotel> getByAmenities(String city);

	public Map<String, Integer> getHistogram(String param);

	public List<Hotel> searchHotels(String name, String brand, String city, String country, String amenities);
}
