package hello;

/**
 * @author Evgenii_Lartcev (created on 12/12/2016).
 */
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloController {

    private Facebook facebook;
    private ConnectionRepository connectionRepository;

    public HelloController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

    @GetMapping
    public String helloFacebook(Model model) {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "redirect:/connect/facebook";
        }
        /*
        an old version rises exception Error message is (#12) bio field is deprecated for versions v2.8 and higher
        final User userProfile = facebook.userOperations().getUserProfile();
         */
        //how solve this problem see http://stackoverflow.com/questions/39890885/error-message-is-12-bio-field-is-deprecated-for-versions-v2-8-and-higher
        //{ "id", "about", "age_range", "birthday", "context", "cover", "currency", "devices", "education", "email",
        // "favorite_athletes", "favorite_teams", "first_name", "gender", "hometown", "inspirational_people", "installed",
        // "install_type", "is_verified", "languages", "last_name", "link", "locale", "location", "meeting_for",
        // "middle_name", "name", "name_format", "political", "quotes", "payment_pricepoints", "relationship_status",
        // "religion", "security_settings", "significant_other", "sports", "test_group", "timezone", "third_party_id",
        // "updated_time", "verified", "video_upload_limits", "viewer_can_send_gift", "website", "work"}
        String [] fields = { "id", "email",  "first_name", "last_name" };
        User userProfile = facebook.fetchObject("me", User.class, fields);
        model.addAttribute("facebookProfile", userProfile);
        PagedList<Post> feed = facebook.feedOperations().getFeed();
        model.addAttribute("feed", feed);
        return "hello";
    }

}
