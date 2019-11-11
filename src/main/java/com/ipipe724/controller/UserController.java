package com.ipipe724.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import com.ipipe724.model.Role;
import com.ipipe724.model.User;
import com.ipipe724.service.RoleService;
import com.ipipe724.service.UserService;




@Controller
public class UserController {

	@Autowired
    private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(value="/users", method = RequestMethod.GET)
    public String locations(Model model, @RequestParam("page") Optional<Integer> pageNumber, 
    	      @RequestParam("size") Optional<Integer> pageSize){
		
		int currentPage = pageNumber.orElse(1);
        int size = pageSize.orElse(50);
        
         
        Page<User> page = userService.getPageAllStatus(currentPage-1, size);
        
        model.addAttribute("page", page);
        
        int totalPages = page.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
        return "admin/users";
    }
	
	@RequestMapping(value="/users/hold", method = RequestMethod.GET)
    public String editLocation(Model model, @RequestParam("id") Optional<Integer> id){
		
			    
        User user = userService.findUser(id.get());
        if(user.getActive() == 1) {
        	userService.updateUserSatatus(user, 0);
        } else {
        	userService.updateUserSatatus(user, 1);
        }
       
        
        return "redirect:/users";
    }
	
	@RequestMapping(value="/edit/user", method = RequestMethod.GET)
    public String editUser(Model model, @RequestParam("id") Optional<Integer> id){
	        
	        User user = userService.findUser(id.get());
	        model.addAttribute("user", user);
	        List<Role> roles = (List<Role>) roleService.getAllRole();
	        model.addAttribute(roles);
	        return "registration";
	    }

}
