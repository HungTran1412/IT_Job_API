package backend.main.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import backend.main.configuration.AppProperties;
import backend.main.utils.JwtUtils;
import backend.main.utils.SseUtils;

@RestController
@RequestMapping("/sse")
public class SseController {

    private final OrderController orderController;

	@Autowired
    private JobController jobController;
	
	@Autowired
	private ObjectProvider<SseUtils> objectProvider;
	
	
	@Autowired
	private SseUtils sseUtils;
	
	@Autowired
	private JwtUtils jwtUtils;

    @Autowired
    private AppProperties appProperties;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();


    SseController(OrderController orderController) {
        this.orderController = orderController;
    }


    @GetMapping("/subscribe/admin")
    public SseEmitter subscribeAdmin() throws InterruptedException {
    	String context = appProperties.getAdmin().getEmail();
        return sseUtils.subscribe(context);
    }

    @GetMapping("/subscribe/")
    public SseEmitter subscribe(@RequestParam String email) throws InterruptedException {
 
        System.out.println(email);
        return sseUtils.subscribe(email);
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

