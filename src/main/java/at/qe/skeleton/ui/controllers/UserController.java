package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.configs.WebSecurityConfig;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.UserxRole;
import at.qe.skeleton.services.UserxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    @Autowired
    UserxService userxService;

    @Autowired
    WebSecurityConfig webSecurityConfig;

    //this is for a buttons "student mode", "admin mode" and its visibility
    //returns iknfo about user roles
    @GetMapping("/checkRoles")
    public int checkRoles(){
        //1 = student
        //2 = student + admin
        //3 = admin
        Userx userx = userxService.getAuthenticatedUser();
        String role = userxService.returnHighestRole(userx.getUsername());
        int returnCode;
        if(role.equals("admin")){
            if(userx.getRoles().contains(UserxRole.STUDENT)){
                //its and STUDENT and ADMIN
                returnCode = 2;
            }else{
                //its just ADMIN
                returnCode =  3;
            }
        }else{
            // its just STUDENT
            returnCode =  1;
        }
        return returnCode;
    }

    @GetMapping("/admin/loadUsers")
    public Collection<Userx> loadUsers(){
        return userxService.getAllUsersBesidesCurrentOne();
    }

    @PostMapping("/admin/newUser/{username}/{password}/{roles}/{firstName}/{lastName}/{email}")
    public void addNewUser(@PathVariable String username,@PathVariable String password,@PathVariable List<UserxRole> roles,@PathVariable String firstName,@PathVariable String lastName,@PathVariable String email){
        Set<UserxRole> rolesSet = new HashSet<>(roles);
        password = webSecurityConfig.passwordEncoder().encode(password);
        userxService.saveUser(new Userx(username,password,firstName,lastName,email,rolesSet));
    }

    @DeleteMapping("/admin/deleteUser/{username}")
    public void deleteUser(@PathVariable String username){
        userxService.deleteUser(userxService.getUserById(username));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/editUser/{username}/{roles}/{firstName}/{lastName}")
    public void editUser(@PathVariable String username,@PathVariable List<UserxRole> roles,@PathVariable String firstName,@PathVariable String lastName){
        Set<UserxRole> rolesSet = new HashSet<>(roles);
        Userx user = userxService.getUserById(username);
        user.setRoles(rolesSet);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userxService.saveUser(user);

    }

}
