package backend.main.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import backend.main.configuration.AppProperties;
import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.utils.JwtUtils;
import backend.main.utils.SseUtils;

@RestController
@RequestMapping("/sse")
public class SseController {

	@Autowired
    private JobController jobController;
	
	@Autowired
	private ObjectProvider<SseUtils> objectProvider;
	
	@Autowired
	private JwtUtils jwtUtils;

    @Autowired
    private AppProperties appProperties;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();


    @GetMapping("/subscribe/admin")
    public SseEmitter subscribeAdmin() throws InterruptedException {
    	String context = appProperties.getAdmin().getEmail();
        return objectProvider.getObject().subscribe(context);
    }

    @GetMapping("/subscribe/")
    public SseEmitter subscribe() throws InterruptedException {
        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        if(context == null || context instanceof AnonymousAuthenticationToken) {
        	throw new AppException(Code.UNAUTHORIZED);
        }
 
        return objectProvider.getObject().subscribe(context.getName());
    }


    public void sendToUser(String username, Object data) {
        SseEmitter emitter = emitters.get(username);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                    .name("message")
                    .data(data));
            } catch (Exception e) {
                emitters.remove(username);
            }
        }
    }
    
    @PostMapping("/send/{username}")
    public void send(@PathVariable String username) {
        sendToUser(username, "HELLO" + username);
    }

}

