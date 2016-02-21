package su.asgor.controller;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import su.asgor.dao.UserRepository;
import su.asgor.dao.VerificationTokenRepository;
import su.asgor.model.User;
import su.asgor.model.VerificationToken;
import su.asgor.service.MailService;

@Controller
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VerificationTokenRepository tokenRepository;
	@Autowired
	private MailService mailService;
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<?> login( @RequestBody User user) {
    	User userInDb = userRepository.findByEmail(user.getEmail());
    	if((userInDb != null)&&(userInDb.getPassword().equals(user.getPassword()))&&(userInDb.getEnabled())) {
    		return new ResponseEntity<>(HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>(HttpStatus.CONFLICT);
    	}
    }
    @RequestMapping(value = "/logout",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<?> logout() {
    		return new ResponseEntity<>(HttpStatus.OK);
    }
    @RequestMapping(value = "/registrate",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<?> registrate( @RequestBody User user, HttpServletRequest request) {
    	String baseUrl = String.format("%s://%s:%d/",request.getScheme(),  request.getServerName(), request.getServerPort());
    	if(userRepository.findByEmail(user.getEmail())!=null) {
    		userRepository.save(user);
    		VerificationToken token = new VerificationToken(UUID.randomUUID().toString(), user);
    		tokenRepository.save(token);
    		mailService.sendConfirmRegistration(user.getEmail(), baseUrl+"#/verify/" + token.getToken());
    		return new ResponseEntity<>(HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>(HttpStatus.CONFLICT);
    	}
    }
    @RequestMapping(value = "/verify/{token}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<?> verify(@PathVariable String token) {
    	VerificationToken vt = tokenRepository.findByToken(token);
    	if((vt != null)&&(vt.getExpiryDate().after(new Date()))){
    		User user = vt.getUser();
    		user.setEnabled(true);
    		vt.setVerified(true);
    		userRepository.save(user);
    		tokenRepository.save(vt);
    		return new ResponseEntity<>(HttpStatus.OK);
    	}else{
    		return new ResponseEntity<>(HttpStatus.CONFLICT);
    	}
    }
    @RequestMapping(value = "/resend/{token}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<?> resend(@PathVariable String token, HttpServletRequest request) {
    	String baseUrl = String.format("%s://%s:%d/",request.getScheme(),  request.getServerName(), request.getServerPort());
    	VerificationToken vt = tokenRepository.findByToken(token);
    	if((vt != null)){
    		VerificationToken newToken = new VerificationToken(UUID.randomUUID().toString(), vt.getUser());
    		tokenRepository.delete(vt);
    		tokenRepository.save(newToken);
    		mailService.sendConfirmRegistration(newToken.getUser().getEmail(), baseUrl+"#/verify/" + newToken.getToken());
    		return new ResponseEntity<>(HttpStatus.OK);
    	}else{
    		return new ResponseEntity<>(HttpStatus.CONFLICT);
    	}
    }
    @RequestMapping(value = "/recovery",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<?> recover( @RequestBody String email, HttpServletRequest request) {
    	String baseUrl = String.format("%s://%s:%d/",request.getScheme(),  request.getServerName(), request.getServerPort());
    	User user  = userRepository.findByEmail(email);
    	if(user != null) {
    		VerificationToken token = new VerificationToken(UUID.randomUUID().toString(), user);
    		tokenRepository.save(token);
    		mailService.sendPasswordRecovery(email, baseUrl+"#/recovery/" + token.getToken());
    		return new ResponseEntity<>(HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>(HttpStatus.CONFLICT);
    	}
    }
    @RequestMapping(value = "/recovery/{token}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<?> recoverPassword(@PathVariable String token, @RequestBody String password) {
    	VerificationToken vt = tokenRepository.findByToken(token);
    	if((vt != null)&&(vt.getExpiryDate().after(new Date()))){
    		User user = vt.getUser();
    		user.setPassword(password);
    		vt.setVerified(true);
    		userRepository.save(user);
    		tokenRepository.save(vt);
    		return new ResponseEntity<>(HttpStatus.OK);
    	}else{
    		return new ResponseEntity<>(HttpStatus.CONFLICT);
    	}
    }
    @RequestMapping(value = "/resend-password/{token}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<?> resendPassword(@PathVariable String token, HttpServletRequest request) {
    	String baseUrl = String.format("%s://%s:%d/",request.getScheme(),  request.getServerName(), request.getServerPort());
    	VerificationToken vt = tokenRepository.findByToken(token);
    	if((vt != null)){
    		VerificationToken newToken = new VerificationToken(UUID.randomUUID().toString(), vt.getUser());
    		tokenRepository.delete(vt);
    		tokenRepository.save(newToken);
    		mailService.sendPasswordRecovery(newToken.getUser().getEmail(), baseUrl+"#/recovery/" + newToken.getToken());
    		return new ResponseEntity<>(HttpStatus.OK);
    	}else{
    		return new ResponseEntity<>(HttpStatus.CONFLICT);
    	}
    }
}