package com.vacation_bot.core.words;

import com.vacation_bot.core.BaseService;
import com.vacation_bot.domain.models.SentenceModel;
import com.vacation_bot.repositories.RepositoryFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Extracts classification words from database.
 */
public class WordsService extends BaseService {

    /**
     * Useless words that aren't necessary for classification.
     */
    private static final List<String> USELESS_WORDS = Arrays.asList( "the", "a", "to", "in", "at", "for", "on" );

    public WordsService( RepositoryFactory repositoryFactory ) { super( repositoryFactory ); }

    @Cacheable( "words" )
    @ServiceActivator
    public Map<String, List<String>> calculateWordsWorth() {
        List<SentenceModel> models = getSentenceModelRepository().findAll();
        Map<String, List<String>> map = new HashMap<>();
        for ( SentenceModel model : models ) {
            map.put( model.getType().toString(), extractWords( model.getExamples() ) );
        }
        return map;
    }

    private List<String> extractWords( List<String> sentenceExamples ) {
        return sentenceExamples.stream()
                .flatMap( (s) -> Arrays.stream( s.toLowerCase() // work with each sentence
                                                .replaceAll( "[\\p{P}&&[^_]]", "" ) // remove duplications exc6ept _
                                                .split(" " ) ) )
                .filter( el -> !USELESS_WORDS.contains( el ) ) // work with words from all sentences
                .unordered()  // remove duplicates from unordered collection goes faster
                .distinct()
                .collect( Collectors.toList() );
    }
}
