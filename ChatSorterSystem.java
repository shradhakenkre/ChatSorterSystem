import java.util.Scanner;
import java.util.regex.Pattern;

// Step 1: Abstraction - Define an interface for chat sorting
interface ChatSorter {
    String categorizeMessage(String message);
    void analyzeSentiment(String message);
}

// Step 2: Base Class - Encapsulation of ChatMessage
abstract class ChatMessage implements ChatSorter {
    private String sender;
    private String content;
    private String category;
    static int messageCount = 0;

    public ChatMessage(String sender, String content) 
    {
        if (sender == null || sender.isEmpty() || content == null || content.isEmpty()) 
        {

            throw new IllegalArgumentException("Sender and Content cannot be empty");
        }
        this.sender = sender;
        this.content = content;
        messageCount++;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }

    public String getContent() 
    {
        return content;
    }

    @Override
    public String toString() 
    {
        return "Sender: " + sender + " | Message: " + content + " | Category: " + category;
    }
}

// Step 3: Inheritance - WorkChat extending ChatMessage
class WorkChat extends ChatMessage {
    public WorkChat(String sender, String content) 
    {
        super(sender, content);
        setCategory(categorizeMessage(content));
    }

    @Override
    public String categorizeMessage(String message) 
    {
        if (Pattern.compile("\\b(meeting|deadline|project|client|report)\\b", Pattern.CASE_INSENSITIVE).matcher(message).find()) {
            return "Work";
        }
        return "General";
    }

    @Override
    public void analyzeSentiment(String message) 
    {
        System.out.println("Sentiment Analysis: Likely a work-related discussion.");
    }
}

// Step 4: Inheritance - PersonalChat extending ChatMessage
class PersonalChat extends ChatMessage {
    public PersonalChat(String sender, String content) 
    {
        super(sender, content);
        setCategory(categorizeMessage(content));
    }

    @Override
    public String categorizeMessage(String message) 
    {
        if (Pattern.compile("\\b(family|friend|birthday|weekend|vacation|dinner)\\b", Pattern.CASE_INSENSITIVE).matcher(message).find()) {
            return "Personal";
        }
        return "General";
    }

    @Override
    public void analyzeSentiment(String message) 
    {
        System.out.println("Sentiment Analysis: Personal or social conversation detected.");
    }
}

// Step 5: Inheritance - SpamChat extending ChatMessage
class SpamChat extends ChatMessage {
    public SpamChat(String sender, String content) 
    {
        super(sender, content);
        setCategory(categorizeMessage(content));
    }

    @Override
    public String categorizeMessage(String message) 
    {
        if (Pattern.compile("\\b(win|prize|free money|lottery|click here|congratulations)\\b", Pattern.CASE_INSENSITIVE).matcher(message).find()) {
            return "Spam";
        }
        return "General";
    }

    @Override
    public void analyzeSentiment(String message) 
    {
        System.out.println("Sentiment Analysis: Suspicious or spam content detected!");
    }
}

// Step 6: New Feature - HybridChat for messages containing multiple categories
class HybridChat extends ChatMessage 
{
    public HybridChat(String sender, String content) 
    {
        super(sender, content);
        setCategory(categorizeMessage(content));
    }

    @Override
    public String categorizeMessage(String message) 
    {
        boolean isWork = Pattern.compile("\\b(meeting|deadline|project|client|report)\\b", Pattern.CASE_INSENSITIVE).matcher(message).find();
        boolean isPersonal = Pattern.compile("\\b(family|friend|birthday|weekend|vacation|dinner)\\b", Pattern.CASE_INSENSITIVE).matcher(message).find();
        boolean isSpam = Pattern.compile("\\b(win|prize|free money|lottery|click here|congratulations)\\b", Pattern.CASE_INSENSITIVE).matcher(message).find();

        if (isWork && isSpam) return "Work-Spam";
        if (isPersonal && isSpam) return "Personal-Spam";
        if (isWork && isPersonal) return "Work-Personal";
        return "General";
    }

    @Override
    public void analyzeSentiment(String message) 
    {
        System.out.println("Sentiment Analysis: Mixed content detected.");
    }
}

// Step 7: Exception Handling and Execution
public class ChatSorterSystem {
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter sender name: ");
            String sender = scanner.nextLine().trim();

            System.out.print("Enter your message: ");
            String content = scanner.nextLine().trim();

            ChatMessage message;
            boolean isWork = Pattern.compile("\\b(meeting|deadline|project|client|report)\\b", Pattern.CASE_INSENSITIVE).matcher(content).find();
            boolean isPersonal = Pattern.compile("\\b(family|friend|birthday|weekend|vacation|dinner)\\b", Pattern.CASE_INSENSITIVE).matcher(content).find();
            boolean isSpam = Pattern.compile("\\b(win|prize|free money|lottery|click here|congratulations)\\b", Pattern.CASE_INSENSITIVE).matcher(content).find();

            if ((isWork && isSpam) || (isPersonal && isSpam) || (isWork && isPersonal)) {
                message = new HybridChat(sender, content);
            } else if (isWork) {
                message = new WorkChat(sender, content);
            } else if (isPersonal) {
                message = new PersonalChat(sender, content);
            } else if (isSpam) {
                message = new SpamChat(sender, content);
            } else {
                message = new PersonalChat(sender, content);
            }

            System.out.println("\nCategorized Message:");
            System.out.println(message);

            message.analyzeSentiment(content);

            System.out.println("Total Messages Processed: " + ChatMessage.messageCount);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
