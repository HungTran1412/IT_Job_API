package backend.main.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class SseController {

    private final JobController jobController;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    SseController(JobController jobController) {
        this.jobController = jobController;
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe() throws InterruptedException {
        String context = SecurityContextHolder.getContext().getAuthentication().getName();

        SseEmitter emitter = new SseEmitter(0L); // không timeout
        emitters.put(context, emitter);
        System.out.println(context);
        
        emitter.onCompletion(() -> emitters.remove(context));
        emitter.onTimeout(() -> emitters.remove(context));
        emitter.onError(e -> emitters.remove(context));
        return emitter;
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

