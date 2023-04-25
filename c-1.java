import java.util.Properties;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreAnnotations.*;

public class IntentIdentifier {
    public static void main(String[] args) {
        // Initialize the sentiment analysis pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Get input sentence from user
        System.out.print("Enter a sentence: ");
        String sentence = System.console().readLine();

        // Analyze sentiment and output intent
        Annotation annotation = pipeline.process(sentence);
        String sentiment = annotation.get(SentimentClass.class);
        switch(sentiment) {
            case "Positive":
                System.out.println("Intent: Positive");
                break;
            case "Negative":
                System.out.println("Intent: Negative");
                break;
            default:
                System.out.println("Intent: Neutral");
                break;
        }
    }
}
