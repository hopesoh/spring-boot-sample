package br.com.casadocodigo.loja.controllers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casadocodigo.loja.daos.ProductDAO;
import br.com.casadocodigo.loja.infra.FileSaver;
import br.com.casadocodigo.loja.models.BookType;
import br.com.casadocodigo.loja.models.Product;

@Controller
@Transactional
@RequestMapping("/produtos")
public class ProductsController {
	
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private FileSaver fileSaver;
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView("products/list");
		modelAndView.addObject("products", productDAO.list());
		return modelAndView;
	}
	
	@RequestMapping("/{id}")
	public ModelAndView show(@PathVariable("id") Integer id) {
		ModelAndView modelAndView = new ModelAndView("products/show");
		Product product = productDAO.find(id);
		modelAndView.addObject("product", product);
		return modelAndView;
	}
	
	@RequestMapping("/form")
	public ModelAndView form(@ModelAttribute Product product){
		ModelAndView modelAndView = new ModelAndView("products/form");
		modelAndView.addObject("types", BookType.values());
		return modelAndView;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@CacheEvict(value="books", allEntries=true)
	public ModelAndView save(MultipartFile summary, @ModelAttribute("product") @Valid Product product, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()){
			return form(product);
		}
		String webPath = fileSaver.write("uploaded-images",summary);
		product.setSummaryPath(webPath);
		productDAO.save(product);
		redirectAttributes.addFlashAttribute("success", "Produto cadastrado com sucesso!!!!");
		return new ModelAndView("redirect:/produtos");
	}
	
}
