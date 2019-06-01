package rocks.danielw.web.controller.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import rocks.danielw.config.constants.Endpoints;
import rocks.danielw.config.constants.ViewNames;
import rocks.danielw.service.user.UserService;
import rocks.danielw.web.dto.response.UserResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserController {

  private final UserService userService;
  private final ModelMapper mapper;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
    this.mapper = new ModelMapper();
  }

  @GetMapping({Endpoints.USERS_LIST})
  public ModelAndView showUsersList() {
    List<UserResponse> users = userService.getAllUsers()
            .stream()
            .map(userDto -> mapper.map(userDto, UserResponse.class))
            .collect(Collectors.toList());

    Map<String, Object> viewModel = new HashMap<>();
    viewModel.put("users", users);

    return new ModelAndView(ViewNames.USERS_LIST, viewModel);
  }

  @GetMapping({Endpoints.USERS_CREATE})
  public ModelAndView showCreateUserForm(Model model) {
    return new ModelAndView(ViewNames.USERS_CREATE);
  }

  @GetMapping({Endpoints.USERS_EDIT})
  public ModelAndView showEditUserForm(@PathVariable(name="id") long id, Model model) {
    return new ModelAndView(ViewNames.USERS_EDIT);
  }

}
