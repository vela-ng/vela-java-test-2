package com.unbabel.challenge.controller;

import com.unbabel.challenge.dto.ClientRequest;
import com.unbabel.challenge.dto.ClientResponse;
import com.unbabel.challenge.dto.UnBabelRequest;
import com.unbabel.challenge.model.Language;
import com.unbabel.challenge.model.Message;
import com.unbabel.challenge.repositories.LanguageRepository;
import com.unbabel.challenge.repositories.MessageRepository;
import com.unbabel.challenge.service.TranslationService;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    TranslationService translationService;

    @Value("${java.challenge.company}")
    private String company; // Reads this value from Spring properties file

    /**
     * Accepts get requests to the "/" url, generates random messages
     * and renders them in thymeleaf template (index.html).
     * @param model inject objects into thymeleaf template
     * @return generated html page using thymeleaf
     */
    @GetMapping("/")
    public String main(Model model) {
        generateNMessages(10);
        //set variables to be used in thymeleaf template
        model.addAttribute("company", company);
        model.addAttribute("messages", messageRepository.findAll());
        model.addAttribute("languages", getAvailableLanguages());

        return "index"; //thymeleaf template name (index -> index.html)
    }


    /**
     * Deletes all messages and inserts new random ones
     * @param repetitions number of times to loop
     */
    private void generateNMessages(int repetitions) {
        messageRepository.deleteAll(); //clean all random str
        for (int i = 0; i < repetitions; i++) {
            messageRepository.save(new Message(RandomStringUtils.randomAlphabetic(30)));
        }
    }

    private List<Language> getAvailableLanguages(){
        return languageRepository.findAll();
    }

    @PostMapping("/translate")
    @ResponseBody
    public ResponseEntity<ClientResponse> executeTranslation(@RequestBody ClientRequest clientRequest){
        UnBabelRequest unBabelRequest = translationService.convertToUnBabelRequest(clientRequest);

        return new ResponseEntity<>(translationService.translate(unBabelRequest), HttpStatus.OK);
    }
}
