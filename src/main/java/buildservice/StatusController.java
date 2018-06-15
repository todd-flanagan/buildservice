package buildservice;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import java.util.List;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
public class StatusController {

    private final BuildService buildService;

    @Autowired
    public StatusController(BuildService buildService) {
        this.buildService = buildService;
    }

    @RequestMapping("/status")
    public String listBuilds(Model model) {
        //model.addAttribute("builds", Arrays.<Build>asList(new Build[]{new Build("toolbox1","ctf1"), new Build("toolbox2","ctf2")}));
        model.addAttribute("queuedbuilds", buildService.listQueuedBuilds());
        model.addAttribute("completedbuilds", buildService.listCompletedBuilds());

        return "statusTable";
    }
}
