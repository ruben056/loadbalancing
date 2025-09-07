package be.drs.api.iface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ApiRestController {

    int counter = 0;

    @Autowired
    private Environment environment;


    @GetMapping("/api/count")
    public String count(){
        return getActiveProfileName() + " : " + counter++;
    }

    public String getActiveProfileName() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            return activeProfiles[0];
        }
        return "No active profile found";
    }
}
