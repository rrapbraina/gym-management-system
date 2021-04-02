package com.ubt.gymmanagementsystem.controllers.administration;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.services.administration.RoleService;
import com.ubt.gymmanagementsystem.entities.administration.daos.RoleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    @PostAuthorize("hasAuthority('READ_ROLE')")
    public String roles(Model model){
        model.addAttribute("roles", roleService.getAll());
        return "administration/roles/roles";
    }

    @GetMapping("/addRole")
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public String addRole(Model model){
        return "administration/roles/addRole";
    }

    @PostMapping(value = "/addRole", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public ModelAndView addRole(@ModelAttribute RoleDAO roleDAO){

        try {
            boolean created = roleService.save(roleDAO);

            ModelAndView modelAndView = new ModelAndView("administration/roles/roles");
            modelAndView.addObject("isCreated", created);
            modelAndView.addObject("roles", roleService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/roles/addRole");
            modelAndView.addObject("roleDAO", roleDAO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editRole/{id}")
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public String editRole(@PathVariable Long id, Model model){

        model.addAttribute("roleDAO", roleService.prepareRoleDAO(id));
        return "administration/roles/editRole";
    }

    @PutMapping(value = "/editRole", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public ModelAndView editRole(@ModelAttribute RoleDAO roleDAO){

        try {
            boolean updated = roleService.update(roleDAO);

            ModelAndView modelAndView = new ModelAndView("administration/roles/roles");
            modelAndView.addObject("isUpdated", updated);
            modelAndView.addObject("roles", roleService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/roles/editRole");
            modelAndView.addObject("roleDAO", roleDAO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disableRole/{id}")
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public String disableRole(@PathVariable Long id, Model model){

        boolean disabled = roleService.disable(id);
        model.addAttribute("isDisabled", disabled);
        model.addAttribute("roles", roleService.getAll());
        return "administration/roles/roles";
    }

    @GetMapping("/enableRole/{id}")
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public String enableRole(@PathVariable Long id, Model model){

        boolean enabled = roleService.enable(id);
        model.addAttribute("isEnabled", enabled);
        model.addAttribute("roles", roleService.getAll());
        return "administration/roles/roles";
    }
}