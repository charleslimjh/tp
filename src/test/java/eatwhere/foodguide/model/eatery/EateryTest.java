package eatwhere.foodguide.model.eatery;

import static eatwhere.foodguide.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static eatwhere.foodguide.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static eatwhere.foodguide.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static eatwhere.foodguide.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static eatwhere.foodguide.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import eatwhere.foodguide.testutil.Assert;
import eatwhere.foodguide.testutil.PersonBuilder;
import eatwhere.foodguide.testutil.TypicalEateries;

public class EateryTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Eatery eatery = new PersonBuilder().build();
        Assert.assertThrows(UnsupportedOperationException.class, () -> eatery.getTags().remove(0));
    }

    @Test
    public void isSameEatery() {
        // same object -> returns true
        assertTrue(TypicalEateries.ALICE.isSameEatery(TypicalEateries.ALICE));

        // null -> returns false
        assertFalse(TypicalEateries.ALICE.isSameEatery(null));

        // same name, all other attributes different -> returns true
        Eatery editedAlice = new PersonBuilder(TypicalEateries.ALICE)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(TypicalEateries.ALICE.isSameEatery(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(TypicalEateries.ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(TypicalEateries.ALICE.isSameEatery(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Eatery editedBob = new PersonBuilder(TypicalEateries.BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(TypicalEateries.BOB.isSameEatery(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(TypicalEateries.BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(TypicalEateries.BOB.isSameEatery(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Eatery aliceCopy = new PersonBuilder(TypicalEateries.ALICE).build();
        assertTrue(TypicalEateries.ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(TypicalEateries.ALICE.equals(TypicalEateries.ALICE));

        // null -> returns false
        assertFalse(TypicalEateries.ALICE.equals(null));

        // different type -> returns false
        assertFalse(TypicalEateries.ALICE.equals(5));

        // different eatery -> returns false
        assertFalse(TypicalEateries.ALICE.equals(TypicalEateries.BOB));

        // different name -> returns false
        Eatery editedAlice = new PersonBuilder(TypicalEateries.ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(TypicalEateries.ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(TypicalEateries.ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(TypicalEateries.ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(TypicalEateries.ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(TypicalEateries.ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(TypicalEateries.ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(TypicalEateries.ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(TypicalEateries.ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(TypicalEateries.ALICE.equals(editedAlice));
    }
}
