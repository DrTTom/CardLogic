package de.tautenhahn.collection.generic.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.question.Question;
import lombok.Data;

/**
 * Contains all data created by a search process. That data contains:
 * <ul>
 * <li>Questions for searched object with checked values</li>
 * <li>number of probably/possibly matches</li>
 * <li>best matches, this list may be truncated if too long</li>
 * </ul>
 *
 * @author TT
 */
@Data
public class SearchResult
{

    private final String type;

    private final String primKeyOfEditedObject;

    private final boolean forSubmit;

    private List<Question> questions;

    private String message;

    private int numberTotal;

    private int numberMatching;

    private int numberPossible;

    private List<DescribedObject> matches = new ArrayList<>();

    private Map<String, Map<String, String>> translations;

    /**
     * Creates new object setting some context data which allows state-free front ends.
     *
     * @param type type of described objects(s)
     * @param primKeyOfEditedObject optional, if specified primKey value of the existing object which is currently
     *     edited.
     * @param forSubmit true in case user already requested submitting of entered data but had to do some
     *     corrections before.
     */
    public SearchResult(String type, String primKeyOfEditedObject, boolean forSubmit)
    {
        this.type = type;
        this.primKeyOfEditedObject = primKeyOfEditedObject;
        this.forSubmit = forSubmit;
    }
}
