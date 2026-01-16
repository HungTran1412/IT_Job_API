package backend.main.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseUtils {
	
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String context) throws InterruptedException {
    	
        SseEmitter emitter = new SseEmitter(0L); // không timeout
        emitters.put(context, emitter);

        emitter.onCompletion(() -> emitters.remove(context));
        emitter.onTimeout(() -> emitters.remove(context));
        emitter.onError(e -> emitters.remove(context));
        return emitter;
    }
	
    public void sendToUser(String username, Object data, Object id) {
        SseEmitter emitter = emitters.get(username);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                    .name("message")
                    .id(id.toString())
                    .data(data));
            } catch (Exception e) {
                emitters.remove(username);
            }
        }
    }

}
