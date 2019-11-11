package com.ipipe724.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.ipipe724.model.Mechanicher;
import com.ipipe724.model.RequestRepair;
import com.ipipe724.model.User;
import com.ipipe724.model.ViewedRequests;
import com.ipipe724.service.MechanicerService;
import com.ipipe724.service.RequestRepairService;
import com.ipipe724.service.UserService;
import com.ipipe724.service.ViewedRequestsService;

@Controller
public class MechanicerController {
	
	@Autowired
	private MechanicerService mechService;
	
	@Autowired
	private RequestRepairService repairRequestService;
	
	
	@Autowired
    private ViewedRequestsService viewedRequestsService;
	
	@Autowired
    private UserService userService;
	
	
	@RequestMapping(value="/edit/mech", method = RequestMethod.GET)
    public String editMech(Model model, @RequestParam("id") Optional<Integer> id){
			    
		Mechanicher mech = mechService.findGrade(new Long(id.get()));
        
        model.addAttribute("mech", mech);
       
        return "work/addmech";
    }
	
	@RequestMapping(value="/add/mech", method = RequestMethod.GET)
    public String addMech(Model model){
       
		Mechanicher mech = new Mechanicher();
        model.addAttribute("mech", mech);
       
        return "work/addmech";
    }
	
	@RequestMapping(value = "/add/mech", method = RequestMethod.POST)
    public String addMechPost(@Valid Mechanicher mech, BindingResult bindingResult) {
		
		if(!userService.isUserHasRole("CUSTOMER")) {
			return "redirect:/access-denied";
		}
		
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       User user = userService.findUserByEmail(auth.getName());
       if (bindingResult.hasErrors()) {
           return "work/addmech";
       } else {
    	   mechService.saveGrade(mech);
       }
       return "redirect:/work/mechs";
	 }
	
	/* HERE Repair Request PARTS */
	
	@RequestMapping(value="/add/request/repair", method = RequestMethod.GET)
    public String newGrade(Model model){
		
		if(!userService.isUserHasRole("CUSTOMER")) {
			return "redirect:/access-denied";
		}
		
		RequestRepair request = new RequestRepair();
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
                
        //model.addAttribute("request", request);
        model.addAttribute("user",user);
      
        model.addAttribute("id",user.getId());
        
        return "work/customer";
    }
	
	@RequestMapping(value = "/add/fastrepair/request", method = RequestMethod.POST)
    public String addfastrepairPost(@Valid RequestRepair mech, 
    		@RequestParam("location") Optional<String> location,
    		@RequestParam("broken") Optional<String> broken,
    		@RequestParam("priority1") Optional<String> priority1,
    		@RequestParam("priority2") Optional<String> priority2,
    		@RequestParam("priority3") Optional<String> priority3,
    		@RequestParam("car") Optional<String> car,
    		@RequestParam("message") Optional<String> message,
    		@RequestParam("long") Optional<String> longe,
    		@RequestParam("lat") Optional<String> lat,
    		@RequestParam("id") Optional<String> id,
    		
    		BindingResult bindingResult) {
		
		
		
		if(!userService.isUserHasRole("CUSTOMER")) {
			return "redirect:/access-denied";
		}
		
       if (bindingResult.hasErrors()) {
           return "work/addmech";
       } else {
    	   
    	   System.out.println("\n\n THE RECEIVED DATA is -> "+id.get());
    	   
    	   User user = userService.findUser(Integer.parseInt(id.get()));
    	   
    	   RequestRepair requestRepair = new RequestRepair();
    	   if(location.isPresent())requestRepair.setLocation(location.get());
    	   if(car.isPresent())requestRepair.setCar(car.get());
    	   if(message.isPresent())requestRepair.setMessage(message.get());
    	   if(lat.isPresent())requestRepair.setLatitude(lat.get());
    	   if(longe.isPresent()) requestRepair.setLongitude(longe.get());
    	   
    	   if(priority1.isPresent()) requestRepair.setPriority("High");
    	   else if(priority2.isPresent()) requestRepair.setPriority("Medium");
    	   else if(priority3.isPresent()) requestRepair.setPriority("Low");
    	   else {requestRepair.setPriority("Default");}
    	   
    	   requestRepair.setUser(user);
    	   
//    	   //DO DISTANCE CALCULATION
//    	   if(requestRepair.getLatitude() != null && requestRepair.getLongitude() != null && requestRepair.getLatitude().length() > 0 && requestRepair.getLongitude().length() > 0) {
//    		   int latt = Integer.parseInt(requestRepair.getLatitude());
//    		   int lonn = Integer.parseInt(requestRepair.getLongitude());
//    		   int result = new CalculateDistance().distanceInKmBetweenEarthCoordinates(latt, lonn);
//    		   requestRepair.setDistance(result+"");
//    	   }   
//  	       //DO DISTANCE CALCULATION
//    	   
    	   
    	   repairRequestService.saveGrade(requestRepair);
    	   
       }
       return "work/generic";
	 }
	
	@RequestMapping(value="/repair/requests", method = RequestMethod.GET)
    public String requests(Model model, @RequestParam("page") Optional<Integer> pageNumber, 
    	      @RequestParam("size") Optional<Integer> pageSize){
		
		int currentPage = pageNumber.orElse(1);
        int size = pageSize.orElse(50);
        Page<RequestRepair> repair = null;
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
		
		if(userService.isUserHasRole("CUSTOMER")) {
			repair = repairRequestService.findAllByUser(currentPage-1, size, user.getId());
		}
		else {
			return "redirect:/access-denied";
		}

        model.addAttribute("repair", repair);
        
        return "work/userapp1";
    }
	
	
	
	/* it is for the mechanic, the data will be provided */
	
	@RequestMapping(value="/machanic/requests", method = RequestMethod.GET)
    public String requestsMech(Model model, @RequestParam("page") Optional<Integer> pageNumber, 
  	      @RequestParam("size") Optional<Integer> pageSize){
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     User user = userService.findUserByEmail(auth.getName());
	     
	     System.out.println("\n\n The user id is => "+user.getId()+":::::::");
		
		if(!userService.isUserHasRole("MECHANICHER")) {
			return "redirect:/access-denied";
		}
		
		int currentPage = pageNumber.orElse(1);
        int size = pageSize.orElse(50);
        
         
        //request the mechanikers data
        Page<RequestRepair> repair = repairRequestService.findAllByUserByMechanicker(currentPage-1, size, user.getId());
        model.addAttribute("repair", repair);
        
        return "work/userapp2";
    }
	
	
	
	
	@RequestMapping(value="/machanic/requests/accepted/true/", method = RequestMethod.GET)
    public String rmechanicAcceptedRequests(Model model, @RequestParam("page") Optional<Integer> pageNumber, 
  	      @RequestParam("size") Optional<Integer> pageSize){
		 
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User user = userService.findUserByEmail(auth.getName());
	     
	    System.out.println("\n\n The user id is => "+user.getId()+":::::::");
		
		if(!userService.isUserHasRole("MECHANICHER")) {
			return "redirect:/access-denied";
		}
		
		int currentPage = pageNumber.orElse(1);
        int size = pageSize.orElse(50);
        
        //request the mechanikers data
        Page<ViewedRequests> repair = viewedRequestsService.findAllAcceptedByMechanicker(currentPage-1, size, user.getId());
        model.addAttribute("repair", repair);
        
        return "work/user-applications";
    }
	
	@RequestMapping(value="/machanic/requests/action", method = RequestMethod.GET)
    public String requestsMechDoTask(Model model, @RequestParam("page") Optional<Integer> pageNumber, 
  	      @RequestParam("size") Optional<Integer> pageSize,
  	      @RequestParam("id") Optional<Long> id,
  	      @RequestParam("task") Optional<String> task){
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     User user = userService.findUserByEmail(auth.getName());
	    
	     
		if(!userService.isUserHasRole("MECHANICHER")) {
			return "redirect:/access-denied";
		}
		
		int currentPage = pageNumber.orElse(1);
        int size = pageSize.orElse(50);
        
        RequestRepair repair = repairRequestService.findGrade(id.get());
	    
	     ViewedRequests view = new ViewedRequests();
	     view.setUser(user);
	     view.setRequest(repair);
	     view.setStatus(task.get());
	     
	     
	     
	     view = viewedRequestsService.saveGrade(view);
	    
	    if(task.get().equals("accept")) {
	    	model.addAttribute("id",view.getId());
	    	return "work/elementMechanich";
	    }
	    
        return "redirect:/machanic/requests";
    }
	
	
	@RequestMapping(value="/notifications", method = RequestMethod.GET)
    public String notifications(Model model,
    		@RequestParam("page") Optional<Integer> pageNumber, 
    	    @RequestParam("size") Optional<Integer> pageSize){
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     User user = userService.findUserByEmail(auth.getName());
	    
	     
		if(!userService.isUserHasRole("CUSTOMER")) {
			return "redirect:/access-denied";
		}
		
		int currentPage = pageNumber.orElse(1);
        int size = pageSize.orElse(50);
        
        Page<ViewedRequests> page = viewedRequestsService.findAllNotificationsForUser(currentPage-1, size, user.getId());
	     
	    model.addAttribute("page",page);  
	    
        return "work/notifications";
    }
	
	
	
	@RequestMapping(value="/notifications/accept", method = RequestMethod.GET)
    public String notificationsAccept(Model model,
    		@RequestParam("page") Optional<Integer> pageNumber, 
    	    @RequestParam("size") Optional<Integer> pageSize,
    	    @RequestParam("id") Optional<Long> id){
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     User user = userService.findUserByEmail(auth.getName());
	    
	     
		if(!userService.isUserHasRole("CUSTOMER")) {
			return "redirect:/access-denied";
		}
		
		int currentPage = pageNumber.orElse(1);
        int size = pageSize.orElse(50);
        
        Page<ViewedRequests> page = viewedRequestsService.findAllNotificationsForUser(currentPage-1, size, user.getId());
	     
	    model.addAttribute("page",page);  
	    
        return "work/notifications";
    }
	
	
	
	@RequestMapping(value="/machanic/requests/accepted", method = RequestMethod.GET)
    public String requestsMechDoTaskAccept(Model model, 
    	  @RequestParam("page") Optional<Integer> pageNumber, 
  	      @RequestParam("size") Optional<Integer> pageSize,
  	      @RequestParam("id") Optional<Integer> id,
  	      @RequestParam("request_id") Optional<Integer> request_id){
		 
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User user = userService.findUserByEmail(auth.getName());
	     
	     

		int currentPage = pageNumber.orElse(1);
        int size = pageSize.orElse(50);
        
        
        
        Page<ViewedRequests> forDelete = viewedRequestsService.findAllForDeleteStatusUpdate(currentPage-1, size, request_id.get(), user.getId());
        
        ViewedRequests accepted = viewedRequestsService.findGrade(id.get());
        accepted.setStatus("userAccept");
        viewedRequestsService.saveGrade(accepted);
        for(ViewedRequests v : forDelete.getContent()) {
        	v.setStatus("userReject");
        	viewedRequestsService.saveGrade(v);
        }
	    
        return "redirect:/notifications";
    }
	
	/*
	@RequestMapping(value="/machanic/add/price", method = RequestMethod.GET)
    public String requestsMechDoTaskAddPrice(Model model, 
    	  @RequestParam("page") Optional<Integer> pageNumber, 
  	      @RequestParam("size") Optional<Integer> pageSize,
  	      @RequestParam("id") Optional<Integer> id){
		 
        return "/work/elementMechanich";
    }
	*/
	
	@RequestMapping(value="/add/price/number", method = RequestMethod.POST)
    public String requestsMechDoTaskAddPrice(Model model, 
    	  @RequestParam("page") Optional<Integer> pageNumber, 
  	      @RequestParam("size") Optional<Integer> pageSize,
  	      @RequestParam("id") Optional<Integer> id,
  	      @RequestParam("number") Optional<String> number,
	  	  @RequestParam("price") Optional<String> price,
	  	  @RequestParam("comment") Optional<String> comment){
		
		 ViewedRequests updateNeeded = viewedRequestsService.findGrade(id.get());
		 
		 updateNeeded.setDays(number.get());
		 updateNeeded.setComment(comment.get());
		 updateNeeded.setPrice(price.get());
		 
		 viewedRequestsService.saveGrade(updateNeeded);
		 
		 return "redirect:/machanic/requests";
    }
	
	@RequestMapping(value="/car/display", method = RequestMethod.GET)
    public String requestsMechDoTaskAddPrice(Model model){
		 
        return "work/index";
    }
}
