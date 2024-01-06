package com.CustomerApi.Customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CustomerApi.Customer.entities.Customer;
import com.CustomerApi.Customer.services.APIService;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class WebController {
    @Autowired
    private APIService apiService;
    private String token = "";

    @GetMapping("/login")
    public String showLoginPage() {
        return "login_page";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String loginId, @RequestParam String password, Model model) {
        token = apiService.getAccessToken(loginId, password);
        System.out.println(token);
        if (token != null) {
            token = token.replaceAll("\"", "");
            model.addAttribute("token", token);
        } else {
            model.addAttribute("errorMessage", "Invalid customername or password");
            return "redirect:/login?error=true";
        }
        getCustomerList(model);
        return "customerList"; // Returning to the home page i.e customerList
    }

    @GetMapping("/addCustomer")
    public String getMethodName() {
        return "add_customer";
    }

    @PostMapping("/addCustomer")
    public String addCustomer(@ModelAttribute Customer customer, Model model) {
        // Validate first and last name
        if (customer.getFirstName() == null || customer.getLastName() == null) {
            model.addAttribute("errorMessage", "First and last names cannot be null");
            return "add_customer"; // Redirect back to the "Add Customer" page with an error message
        }
        System.out.println(customer.toString());
        System.out.println("Calling add customer servcie");
        System.out.println(apiService.addCustomer(customer, token));

        getCustomerList(model);
        return "customerList";
    }

    @GetMapping("/listCustomer")
    public String getCustomerList(Model model) {
        List<Customer> customers = apiService.getCustomerList(token);
        model.addAttribute("customerList", customers);
        return "customerList";
    }

    @PostMapping("/deleteCustomer")
    public String deleteCustomer(Model model, @RequestParam("uuid") String uuid) {
        apiService.deleteCustomer(uuid, token);
        getCustomerList(model);
        return "customerList";
    }

    @PostMapping("/update")
    public String showUpdatePage(Model model, @RequestParam("uuid") String uuid) {
        Customer customer = apiService.getCustomerByUUID(uuid, token);
        model.addAttribute("customer1", customer);
        return "update_form";
    }

    @PostMapping("/updateCustomer")
    public String updateCustomer(Model model, @ModelAttribute Customer customer, @RequestParam("uuid") String uuid) {
        apiService.updateCustomer(customer, uuid, token);
        getCustomerList(model);
        return "customerList";
    }
}
