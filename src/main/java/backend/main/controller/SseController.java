package backend.main.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import backend.main.utils.JwtUtils;
import backend.main.utils.SseUtils;

@RestController
@RequestMapping("/sse")
public class SseController {

	@Autowired
    private JobController jobController;
	
	@Autowired
	private SseUtils sseUtils;
	
	@Autowired
	private JwtUtils jwtUtils;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();


    @GetMapping("/subscribe/admin")
    public SseEmitter subscribeAdmin(@CookieValue(value = "nimda", required = false) String token) throws InterruptedException {
    	String context = jwtUtils.extractEmail(token);
        return sseUtils.subscribe(context);
    }
    
    @GetMapping("/subscribe/")
    public SseEmitter subscribe(@CookieValue(value = "jwt", required = false) String token) throws InterruptedException {
    	String context = jwtUtils.extractEmail(token);
        return sseUtils.subscribe(context);
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

