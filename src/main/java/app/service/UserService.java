package app.service;


import app.exception.BadResourceException;
import app.model.Report;
import app.model.Roles;
import app.model.User;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import app.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;

@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public boolean isUserAlreadyPresent(User user) {
        boolean isUserAlreadyExists = false;
        User existingUser = userRepository.findByEmail(user.getEmail());
        // If user is found in database, then then user already exists.
        if (existingUser != null) {
            isUserAlreadyExists = true;
        }
        return isUserAlreadyExists;
    }

    public User findUserByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public User findByResetToken(String reset) {
        return userRepository.findByResetToken(reset);
    }

    public User editUserSettings(String name, String surname) {

        User user = getCurrentUser();
        user.setLastName(surname);
        user.setName(name);
        userRepository.save(user);
        return user;
    }

    public User setUser(User user, long userId) {
        user = userRepository.getOne(userId);
        Roles userRoles = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Roles>(Arrays.asList(userRoles)));
        return userRepository.save(user);
    }

    public User setAdmin(User user, long adminId) {
        user = userRepository.getOne(adminId);
        Roles userRoles = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Roles>(Arrays.asList(userRoles)));
        return userRepository.save(user);
    }

    public void savePassword(User user, String password) {
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getOne(long id) {
        return userRepository.getOne(id);
    }

    public User registerUser(User user, MultipartFile multipartFile) {
        user.setResetToken(UUID.randomUUID().toString().substring(0, 8));
        Roles userRoles = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Roles>(Arrays.asList(userRoles)));
        user.setPassword(encoder.encode(user.getPassword()));

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setPhotos(fileName);

        User savedUser = userRepository.save(user);

        String uploadDir = "user-photos/" + savedUser.getId();

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void deleteUserById(Long id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.deleteById(id);
        }
    }

    public User updateUser(User entity) {

        Optional<User> user = userRepository
                .findById(entity.getId());
        if (user.isPresent()) {
            User newEntity = user.get();
            newEntity.setEmail(entity.getEmail());
            newEntity.setName
                    (entity.getName());
            newEntity.setLastName
                    (entity.getLastName());
            newEntity = userRepository.save(newEntity);
            return newEntity;
        } else {
            entity = userRepository.save(entity);
            return entity;
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        } else {
            throw new UsernameNotFoundException("User is not authenticated; Found " + authentication.getPrincipal() + " of type " + authentication.getPrincipal().getClass() + "; Expected type User");
        }
    }

    public Object findByRole(Long Id) {
        return userRepository.findByRole(Id);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, AuthenticationServiceException {
        User user = userRepository.findByEmail(username);
        System.out.println(user.isEnabled() + ", enabled");
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        else if (!user.isEnabled()) {
            throw new AuthenticationServiceException("User is banned");
        }

        return user;

    }

    public User banUser(Long userId) {
        Optional<User> fetchReportedUser = userRepository.findById(userId);
        User user = fetchReportedUser.orElseThrow(() ->
                new ResourceNotFoundException("User with ID: " + userId+ " doesnt exist"));;
        user.setActive(false);
        return userRepository.save(user);
    }

    public Set<User> getUsersToRecommend() {
        Set<User> users = userRepository.getUsersToRecommend(getCurrentUser().getId());
        return users;
    }
}
