package org.sid.Inventoryservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import javax.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.hateoas.PagedModel;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	@Bean
     CommandLineRunner start(ProductRepository p, RepositoryRestConfiguration Rrc){
		Rrc.exposeIdsFor(Product.class);

		return args ->{

			p.save(new Product(null,"ordi",788,12));
			p.save(new Product(null,"SMARTPHONE",1788,120));
			p.save(new Product(null,"IMPRIMENTE",18,129));
			p.findAll().forEach(s->{
				System.out.println(s.toString());


			});
		};

	 };
}

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor @ToString
class Product{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private double price;
	private double quantity;


}
@RepositoryRestResource
interface ProductRepository extends JpaRepository<Product,Long> {

}
@Controller
class ProductController{
	@Autowired
	private KeycloakRestTemplate keycloakRestTemplate;
	@Autowired
	private ProductRepository productRepository;
	@GetMapping("/prods")
	public String prods(Model model){
		model.addAttribute("products",productRepository.findAll());
		return "prods";
	}
	@GetMapping("/")
	public String index(){
		return "index";
	}

	@GetMapping("/client")
	public String costumers(){
		PagedModel forObject =keycloakRestTemplate.getForObject("http://localhost:8083/costumers",PagedModel.class);

		return "client";
	}
}

@Controller
class SecurityController{
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) throws ServletException {
		request.logout();
		return "redirect:/";
	}


}


