package com.vacation_bot.core.process;

import com.vacation_bot.core.BaseService;
import com.vacation_bot.domain.CustomizedSentence;
import com.vacation_bot.domain.models.VacationModel;
import com.vacation_bot.repositories.RepositoryFactory;
import com.vacation_bot.shared.logging.VacationBotLoggingMessages;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.integration.annotation.ServiceActivator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Process the user request about vacation list.
 */
public class RequestVacationListService extends BaseService {

    private static final SimpleDateFormat format = new SimpleDateFormat( "dd.MM.yy" );

    public RequestVacationListService( final RepositoryFactory factory ) {
        super( factory );
    }

    @ServiceActivator
    CustomizedSentence retrieveVacationList( final CustomizedSentence customizedSentence ) {
        getLogger().info( VacationBotLoggingMessages.REQUEST_VACATION_LIST.getMessage() );

        String userName = customizedSentence.getPersons().isEmpty() ? customizedSentence.getUserExternalCode() : customizedSentence.getPersons().get( 0 );
        List<VacationModel> vacations = !customizedSentence.getDates().isEmpty() ? retrieveVacationsInPeriod( userName,
                customizedSentence.getDates().get( 0 ), customizedSentence.getDates().get( 1 ) )
                : retrieveSomeLastVacations(userName, customizedSentence.getNumbers().get(0));
        customizedSentence.setCurrentResponse( createVacationListDescription( vacations ) );
        return customizedSentence;
    }

    private List<VacationModel> retrieveVacationsInPeriod( final String userName, final String start, final String end ) {
        long startDate = 0;
        long endDate = 0;
        try {
            startDate = format.parse( start ).getTime();
            endDate = format.parse( end ).getTime();
        } catch ( ParseException ex) {
            getLogger().error( "Failed to parse date: " + ex.getMessage() );
        }
        return getVacationModelRepository()
                .findAllByUserIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual( userName, startDate, endDate );
    }

    private List<VacationModel> retrieveSomeLastVacations( final String userName, final String amount ) {
        final int parsedAmount = Integer.parseInt( amount );
        final Pageable page = PageRequest.of( 0, parsedAmount, Sort.Direction.DESC, VacationModel.FieldNames.END_DATE );
        return getVacationModelRepository().findAllByUserId( userName, page );
    }

    private String createVacationListDescription( final List<VacationModel> vacations ) {
        StringBuilder description = new StringBuilder();
        if (vacations.isEmpty()) {
            description.append( "You had no vacations in the specified period." );
        } else {
            description.append("Your vacations: \n");
            for (VacationModel item : vacations) {
                description.append("from ").append(format.format(new Date(item.getStartDate())))
                        .append(" to ").append(format.format(new Date(item.getEndDate())));
                description.append("\n");
            }
        }
        return description.toString();
    }
}
