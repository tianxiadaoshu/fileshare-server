package licx.fileshare.Controller;

import licx.fileshare.Domain.TextShare;
import licx.fileshare.Service.TextShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TextShareController {
    @Autowired
    private TextShareService textShareService;

    @GetMapping("/textshare")
    public String getIndexTextShare(Model model){
        List<TextShare> textShareList = textShareService.getAllTexts();
        model.addAttribute("textList", textShareList);
        return "text-share";
    }

    @DeleteMapping("/textshare/text")
    @ResponseBody
    public Map<String, String> deleteTextByID(String id){
        Map<String, String> result = new HashMap<>();
        result.put("back_message", textShareService.deleteTextByID(Integer.parseInt(id))?"true":"false");
        return result;
    }
    @GetMapping("/textshare/text")
    @ResponseBody
    public TextShare getTextByID(String id){
        return textShareService.getTextByID(Integer.parseInt(id));
    }
    @PutMapping("/textshare/text")
    @ResponseBody
    public boolean modifyTextByID(String id, String shareText){
        return textShareService.modifyTextByID(Integer.parseInt(id), shareText);
    }
    @PostMapping("/textshare/text")
    @ResponseBody
    public TextShare addText(String shareText){
        return textShareService.addText(shareText);
    }
}
