package hotels.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hotels.model.Hotel;
import hotels.service.HotelService;

@RestController
@RequestMapping("/property-view/hotels")
public class HotelController {

	private HotelService hotelService;

	@Autowired
	public HotelController(HotelService hotelService) {
		this.hotelService = hotelService;
	}

	/*
	 * @RequestMapping(value = "", method = RequestMethod.GET, produces =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<List<Hotel>>
	 * getAllHotels() {
	 *
	 * List<Hotel> hotels = hotelService.getAll();
	 *
	 * if (hotels.isEmpty()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
	 *
	 * return new ResponseEntity<>(hotels, HttpStatus.OK); }
	 */
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<List<Map<String, Object>>> getHotels() {
	        List<Map<String, Object>> hotelsList = hotelService.getAll().stream().map(hotel -> {
	            Map<String, Object> hotelInfo = new HashMap<>();
	            hotelInfo.put("id", hotel.getId());
	            hotelInfo.put("name", hotel.getName());
	            hotelInfo.put("description", hotel.getDescription());
	            hotelInfo.put("address", hotel.getAddress());
	            hotelInfo.put("phone", hotel.getContacts().getPhone());
	            return hotelInfo;
	        }).collect(Collectors.toList());
	        return new ResponseEntity<>(hotelsList, HttpStatus.OK);
	    }

	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hotel> getProduct(@PathVariable("id") Long hotelId) {
		if (hotelId == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Hotel hotel = hotelService.getById(hotelId);
		if (hotel == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(hotel, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hotel> saveProduct(@RequestBody Hotel hotel) {

		if (hotel == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		hotelService.save(hotel);

		return new ResponseEntity<>(hotel, HttpStatus.CREATED);
	}

	@RequestMapping(value = "{id}/amenities", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hotel> saveAmenities(@PathVariable("id") Long id, @RequestBody List<String> amenities) {
		Hotel hotel = hotelService.getById(id);
		if (hotel != null) {
			hotel.getAmenities().addAll(amenities);
			hotelService.save(hotel);
		}

		return new ResponseEntity<>(hotel, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Hotel>> searchHotels(@RequestParam(required = false) String name,
			@RequestParam(required = false) String brand, @RequestParam(required = false) String city,
			@RequestParam(required = false) String country, @RequestParam(required = false) String amenity) {

		return new ResponseEntity<>(hotelService.searchHotels(name, brand, city, country, amenity), HttpStatus.OK);
	}

	@RequestMapping(value = "/histogram/{param}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Integer>> getHistogram(@PathVariable String param) {
		return new ResponseEntity<>(hotelService.getHistogram(param), HttpStatus.OK);
	}


}
