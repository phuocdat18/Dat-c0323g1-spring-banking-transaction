package com.cg.controller;


import com.cg.model.Category;
import com.cg.model.Product;
import com.cg.model.dto.ProductDTO;
import com.cg.service.category.ICategoryService;
import com.cg.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cp/products")
public class ProductController {

//    private IProductService productService = new ProductServiceImpl();

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public String showListPage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "cp/product/list";
    }


    @GetMapping("/create")
    public String showCreatePage(Model model) {

        List<Category> categories = categoryService.findAll();

        model.addAttribute("success", false);
        model.addAttribute("message", "");

        model.addAttribute("product", new ProductDTO());

        model.addAttribute("categories", categories);

        return "cp/product/create";
    }

    @GetMapping("/{productId}")
    public String showUpdatePage(@PathVariable Long productId, Model model) {

        Optional<Product> productOptional = productService.findById(productId);

        if (productOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Sản phẩm không tồn tại");
        }
        else {
            model.addAttribute("error", false);
            model.addAttribute("message", "");

            model.addAttribute("product", productOptional.get());

            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
        }

        return "cp/product/edit";
    }

    @GetMapping("/price/{productId}")
    public String showUpdatePricePage(@PathVariable Long productId, Model model) {

        Optional<Product> productOptional = productService.findById(productId);

        if (productOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Sản phẩm không tồn tại");
        }
        else {
            model.addAttribute("error", false);
            model.addAttribute("message", "");

            model.addAttribute("product", productOptional.get());
        }

        return "cp/product/edit-price";
    }

    @GetMapping("/delete/{productId}")
    public String showDeletePage(@PathVariable Long productId, Model model) {
        Optional<Product> productOptional = productService.findById(productId);

        if (productOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Sản phẩm không tồn tại");
        }
        else {
            model.addAttribute("error", false);
            model.addAttribute("message", "");

            model.addAttribute("product", productOptional.get());
        }

        return "cp/product/delete";
    }



    @PostMapping("/create")
    public String create(Model model, @Validated @ModelAttribute ProductDTO productDTO, BindingResult bindingResult) {

        new ProductDTO().validate(productDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("error", true);
            model.addAttribute("product", productDTO);

            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
            return "cp/product/create";
        }

        BigDecimal price = BigDecimal.valueOf(Long.parseLong(productDTO.getPrice()));

        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        product.setUnit(productDTO.getUnit());
        product.setPrice(price);
        product.setCategory(productDTO.getCategory());

        productService.save(product);

        model.addAttribute("success", true);
        model.addAttribute("message", "Thêm sản phẩm thành công");

        model.addAttribute("product", new ProductDTO());

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        return "cp/product/create";
    }

    @PostMapping("/{productId}")
    public RedirectView update(@PathVariable Long productId, @ModelAttribute Product product, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOptional = productService.findById(productId);

        if (productOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Sản phẩm không tồn tại");
        }
        else {
            product.setId(productId);
            productService.save(product);
        }

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin sản phẩm thành công");

        return new RedirectView("/cp/products");
    }

    @PostMapping("/price/{productId}")
    public RedirectView updatePrice(@PathVariable Long productId, @ModelAttribute Product product, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOptional = productService.findById(productId);

        if (productOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Sản phẩm không tồn tại");
        }
        else {
            Product productUpdate = productOptional.get();
            productUpdate.setPrice(product.getPrice());
            productService.updatePrice(productUpdate);
        }

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin giá sản phẩm thành công");

        return new RedirectView("/cp/products");
    }

    @PostMapping("/delete/{productId}")
    public RedirectView delete(@PathVariable Long productId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOptional = productService.findById(productId);

        if (productOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Sản phẩm không tồn tại");
        }
        else {
            productService.delete(productOptional.get());
        }

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công");

        return new RedirectView("/cp/products");
    }



}
