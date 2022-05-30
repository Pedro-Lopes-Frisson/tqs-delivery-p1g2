package ua.tqs.delivera.ControllerTests;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.tqs.delivera.controllers.DeliveraController;
import ua.tqs.delivera.services.RiderService;

@WebMvcTest(DeliveraController.class)
class DeliveraControllerTests {

    @Autowired
	private MockMvc mvnForTests;

	@MockBean
	private RiderService service;

    @Test
	void whenPostRider_thenCreateRider() throws Exception{

		// when(service.save(Mockito.any())).thenReturn(car1);

		// mvnForTests.perform(MockMvcRequestBuilders.post("/api/car")
		// 	.contentType(MediaType.APPLICATION_JSON)
		// 	.content(lab3_2.CarService.JSONUtil.toJson(car1)))
		// 	.andExpect(MockMvcResultMatchers.status().isCreated())
		// 	.andExpect(MockMvcResultMatchers.jsonPath("$.maker", Matchers.is(car1.getMaker())));
		// verify(service, times(1)).save(Mockito.any());
	}
    
}
